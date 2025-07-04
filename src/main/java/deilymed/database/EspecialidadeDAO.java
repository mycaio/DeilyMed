package deilymed.database;



import deilymed.model.Especialidade;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeDAO {

    public void salvar(Especialidade especialidade) {
        String sql = "INSERT INTO especialidades(nome) VALUES(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, especialidade.getNomeEspec());
            pstmt.executeUpdate();

            // Pega o ID gerado pelo banco e atribui ao objeto
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                especialidade.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar especialidade: " + e.getMessage());
        }
    }

    public Especialidade buscarPorId(int id) {
        String sql = "SELECT * FROM especialidades WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Especialidade(rs.getInt("id"), rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar especialidade por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Especialidade> buscarTodos() {
        String sql = "SELECT * FROM especialidades";
        List<Especialidade> especialidades = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                especialidades.add(new Especialidade(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as especialidades: " + e.getMessage());
        }
        return especialidades;
    }
}