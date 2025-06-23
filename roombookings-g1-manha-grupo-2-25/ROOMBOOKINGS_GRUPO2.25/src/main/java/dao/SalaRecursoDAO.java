package dao;

import model.SalaRecurso;
import util.ConexaoPostgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaRecursoDAO {

    public void adicionarRecurso(int salaId, int recursoId) {
        String sql = "INSERT INTO sala_recurso (sala_id, recurso_id) VALUES (?, ?)";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, salaId);
            stmt.setInt(2, recursoId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar recurso à sala: " + e.getMessage());
        }
    }

    public List<SalaRecurso> listarRecursosPorSala(int salaId) {
        List<SalaRecurso> recursos = new ArrayList<>();
        String sql = """
            SELECT r.id, r.recurso_nome
            FROM sala_recurso sr
            JOIN recursosala r ON sr.recurso_id = r.id
            WHERE sr.sala_id = ?
        """;

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, salaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("recurso_nome");
                recursos.add(new SalaRecurso(id, nome));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar recursos da sala: " + e.getMessage());
        }

        return recursos;
    }

    public void removerTodosPorSala(int salaId) {
        String sql = "DELETE FROM sala_recurso WHERE sala_id = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, salaId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao remover recursos da sala: " + e.getMessage());
        }
    }
}
