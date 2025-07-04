package deilymed.database;

import deilymed.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultaDAO {
    private final MedicoDAO medicoDAO;
    private final PacienteDAO pacienteDAO;

    public ConsultaDAO() {
        this.medicoDAO = new MedicoDAO();
        this.pacienteDAO = new PacienteDAO();
    }

    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consultas(paciente_id, medico_id, data_hora, status) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, consulta.getPaciente().getId());
            pstmt.setInt(2, consulta.getMedico().getId());
            pstmt.setString(3, consulta.getDataHora().toString());
            pstmt.setString(4, consulta.getStatus().name());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar consulta: " + e.getMessage(), e);
        }
    }

    public Optional<Consulta> buscarPorId(int id) {
        String sql = "SELECT * FROM consultas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairConsultaDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar consulta por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Busca todas as consultas registradas no banco de dados.
     * @return uma Lista de objetos Consulta, ordenada pela data.
     */
    public List<Consulta> buscarTodos() {
        String sql = "SELECT * FROM consultas ORDER BY data_hora ASC"; // Ordenar por data é uma boa prática
        List<Consulta> consultas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Reutiliza a lógica de extração que já criamos
                consultas.add(extrairConsultaDoResultSet(rs));
            }

        } catch (SQLException e) {
            // Mantém o padrão de tratamento de erros da classe
            throw new RuntimeException("Erro ao buscar todas as consultas: " + e.getMessage(), e);
        }

        return consultas;
    }

    public void atualizarStatus(int consultaId, StatusConsulta novoStatus) {
        String sql = "UPDATE consultas SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, novoStatus.name());
            pstmt.setInt(2, consultaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status da consulta: " + e.getMessage(), e);
        }
    }

    private Consulta extrairConsultaDoResultSet(ResultSet rs) throws SQLException {
        int medicoId = rs.getInt("medico_id");
        int pacienteId = rs.getInt("paciente_id");

        Medico medico = medicoDAO.buscarPorId(medicoId)
                .orElseThrow(() -> new SQLException("Inconsistência de dados: Médico com ID " + medicoId + " não encontrado."));

        Paciente paciente = (Paciente) pacienteDAO.buscarPorId(pacienteId)
                .orElseThrow(() -> new SQLException("Inconsistência de dados: Paciente com ID " + pacienteId + " não encontrado."));

        LocalDateTime dataHora = LocalDateTime.parse(rs.getString("data_hora"));
        StatusConsulta status = StatusConsulta.valueOf(rs.getString("status"));

        Consulta consulta = new Consulta(medico, paciente, dataHora);
        consulta.setId(rs.getInt("id"));
        consulta.setStatus(status);

        return consulta;
    }
}