package deilymed.database;

import deilymed.model.Especialidade;
import deilymed.model.Medico;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Usado para um retorno mais seguro

public class MedicoDAO {

    // Injeção de dependência pelo construtor é uma prática melhor para testabilidade,
    // mas manter o new aqui é aceitável para este projeto.
    private final EspecialidadeDAO especialidadeDAO;

    public MedicoDAO() {
        this.especialidadeDAO = new EspecialidadeDAO();
    }

    public void salvar(Medico medico) {
        if (medico.getEspecialidade().getId() == 0) {
            // MELHORIA: Lançar uma exceção é mais informativo do que imprimir no console.
            throw new IllegalArgumentException("A especialidade do médico precisa ser salva e ter um ID válido.");
        }

        String sql = "INSERT INTO medicos(nome, cpf, crm, especialidade_id) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, medico.getNome());
            pstmt.setString(2, medico.getCpf());
            pstmt.setString(3, medico.getCrm());
            pstmt.setInt(4, medico.getEspecialidade().getId());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    medico.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            // MELHORIA: Lançar uma exceção em vez de "engolir" o erro.
            throw new RuntimeException("Erro ao salvar médico: " + e.getMessage(), e);
        }
    }

    // MELHORIA: Usar Optional para evitar NullPointerExceptions.
    public Optional<Medico> buscarPorId(int id) {
        String sql = "SELECT m.*, e.nome as especialidade_nome FROM medicos m LEFT JOIN especialidades e ON m.especialidade_id = e.id WHERE m.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retorna um Optional contendo o médico, se encontrado.
                    return Optional.of(extrairMedicoDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médico por ID: " + e.getMessage(), e);
        }
        // Retorna um Optional vazio se não encontrar nada.
        return Optional.empty();
    }

    public List<Medico> buscarTodos() {
        String sql = "SELECT m.*, e.nome as especialidade_nome FROM medicos m LEFT JOIN especialidades e ON m.especialidade_id = e.id";
        List<Medico> medicos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                medicos.add(extrairMedicoDoResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os médicos: " + e.getMessage(), e);
        }
        return medicos;
    }

    // NOVO: Método para buscar os horários de um médico específico.
    // Por enquanto, ele retorna uma lista vazia, mas aqui você colocaria a lógica
    // para consultar a tabela de horários.
    private List<LocalDateTime> buscarHorariosPorMedicoId(int medicoId) {
        // String sql = "SELECT data_hora FROM medico_horarios WHERE medico_id = ?";
        // ... lógica para buscar os horários ...
        return new ArrayList<>(); // Placeholder
    }

    // CORREÇÃO PRINCIPAL: O método agora busca os horários antes de criar o objeto.
    private Medico extrairMedicoDoResultSet(ResultSet rs) throws SQLException {
        // PASSO 1: Extrair a especialidade
        Especialidade especialidade = new Especialidade(
                rs.getInt("especialidade_id"),
                rs.getString("especialidade_nome")
        );

        int medicoId = rs.getInt("id");

        // PASSO 2: Buscar a lista de horários para este médico.
        List<LocalDateTime> horarios = buscarHorariosPorMedicoId(medicoId);

        // PASSO 3: Chamar o construtor correto com todos os 6 argumentos.
        return new Medico(
                medicoId,
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("crm"),
                especialidade,
                horarios // Argumento que estava faltando
        );
    }
}