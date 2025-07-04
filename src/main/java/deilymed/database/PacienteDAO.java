package deilymed.database;

import deilymed.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteDAO {
    public void salvar(Paciente paciente) {
        String sql = "INSERT INTO pacientes(nome, cpf, telefone, email) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getCpf());
            pstmt.setString(3, paciente.getTelefone());
            pstmt.setString(4, paciente.getEmail());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                paciente.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar paciente: " + e.getMessage());
        }
    }

    public Optional<Object> buscarPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(extrairPacienteDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Paciente> buscarTodos() {
        String sql = "SELECT * FROM pacientes";
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pacientes.add(extrairPacienteDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    public void atualizar(Paciente paciente) {
        String sql = "UPDATE pacientes SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getCpf());
            pstmt.setString(3, paciente.getTelefone());
            pstmt.setString(4, paciente.getEmail());
            pstmt.setInt(5, paciente.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar paciente: " + e.getMessage());
        }
    }

    // Método auxiliar para não repetir código
    private Paciente extrairPacienteDoResultSet(ResultSet rs) throws SQLException {
        return new Paciente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("telefone"),
                rs.getString("email")
        );
    }
}