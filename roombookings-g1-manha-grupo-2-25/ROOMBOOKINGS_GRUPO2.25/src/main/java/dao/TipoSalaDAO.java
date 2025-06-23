package dao;

import model.TipoSala;
import util.ConexaoPostgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoSalaDAO {

    public void inserir(TipoSala tipoSala) {
        String sql = "INSERT INTO TipoSala (nome, capacidade) VALUES (?, ?)";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoSala.getNome());
            stmt.setInt(2, tipoSala.getCapacidade());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir tipo de sala: " + e.getMessage());
        }
    }

    public TipoSala buscarPorNome(String nome) {
        String sql = "SELECT * FROM TipoSala WHERE nome = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int capacidade = rs.getInt("capacidade");
                return new TipoSala(id, nome, capacidade);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tipo de sala por nome: " + e.getMessage());
        }

        return null;
    }

    public TipoSala buscarPorId(int id) {
        String sql = "SELECT * FROM TipoSala WHERE id = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                int capacidade = rs.getInt("capacidade");
                return new TipoSala(id, nome, capacidade);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tipo de sala por ID: " + e.getMessage());
        }

        return null;
    }

    public List<TipoSala> listarTodos() {
        List<TipoSala> tipos = new ArrayList<>();
        String sql = "SELECT * FROM TipoSala";

        try (Connection conn = ConexaoPostgres.obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int capacidade = rs.getInt("capacidade");
                tipos.add(new TipoSala(id, nome, capacidade));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar tipos de sala: " + e.getMessage());
        }

        return tipos;
    }
}
