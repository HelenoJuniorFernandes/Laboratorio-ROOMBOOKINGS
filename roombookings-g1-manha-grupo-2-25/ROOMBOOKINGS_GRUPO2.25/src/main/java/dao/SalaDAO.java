package dao;

import model.Sala;
import model.SalaRecurso;
import model.TipoSala;
import util.ConexaoPostgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {

    private final TipoSalaDAO tipoSalaDAO = new TipoSalaDAO();

    public void inserir(Sala sala) {
        String sql = "INSERT INTO Sala (capacidade, tipo_id) VALUES (?, ?)";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, sala.getCapacidade());
            stmt.setInt(2, sala.getTipoSala().getId());

            stmt.executeUpdate();

            // Pega o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int salaId = rs.getInt(1);
                sala.setId(salaId);

                // Adiciona os recursos automáticos conforme tipo de sala
                SalaRecursoDAO salaRecursoDAO = new SalaRecursoDAO();
                List<SalaRecurso> recursos = recursosPadraoParaTipo(sala.getTipoSala().getNome());

                for (SalaRecurso recurso : recursos) {
                    salaRecursoDAO.adicionarRecurso(salaId, recurso.getId());
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir sala: " + e.getMessage());
        }
    }

    public Sala buscarPorId(int id) {
        String sql = "SELECT * FROM Sala WHERE id = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int capacidade = rs.getInt("capacidade");
                int tipoId = rs.getInt("tipo_id");
                TipoSala tipoSala = tipoSalaDAO.buscarPorId(tipoId);

                Sala sala = new Sala(capacidade, tipoSala);
                sala.setId(id);

                SalaRecursoDAO salaRecursoDAO = new SalaRecursoDAO();
                List<SalaRecurso> recursos = salaRecursoDAO.listarRecursosPorSala(id);
                recursos.forEach(sala::adicionarRecurso);

                return sala;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar sala: " + e.getMessage());
        }

        return null;
    }

    public List<Sala> listarTodas() {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM Sala";

        try (Connection conn = ConexaoPostgres.obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int capacidade = rs.getInt("capacidade");
                int tipoId = rs.getInt("tipo_id");
                TipoSala tipoSala = tipoSalaDAO.buscarPorId(tipoId);

                Sala sala = new Sala(capacidade, tipoSala);
                sala.setId(id);

                SalaRecursoDAO salaRecursoDAO = new SalaRecursoDAO();
                List<SalaRecurso> recursos = salaRecursoDAO.listarRecursosPorSala(id);
                recursos.forEach(sala::adicionarRecurso);

                salas.add(sala);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar salas: " + e.getMessage());
        }

        return salas;
    }

    // ✅ Método atualizado: case-insensitive
    private List<SalaRecurso> recursosPadraoParaTipo(String tipoNome) {
        List<SalaRecurso> recursos = new ArrayList<>();

        if (tipoNome.equalsIgnoreCase("PREMIUM")) {
            recursos.add(new SalaRecurso(1, "Projetor"));
            recursos.add(new SalaRecurso(3, "Ar-condicionado"));
        } else if (tipoNome.equalsIgnoreCase("VIP")) {
            recursos.add(new SalaRecurso(1, "Projetor"));
            recursos.add(new SalaRecurso(2, "Computador"));
            recursos.add(new SalaRecurso(3, "Ar-condicionado"));
        }
        // STANDARD → sem recursos

        return recursos;
    }
    public boolean possuiReservas(int id) {
        String sql = "SELECT 1 FROM reserva WHERE sala_id = ? LIMIT 1";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Erro ao verificar reservas da sala: " + e.getMessage());
            return true; // Por segurança, assume que tem reserva se der erro
        }
    }

    public boolean excluir(int id) {
        if (possuiReservas(id)) {
            return false;
        }

        String sql = "DELETE FROM sala WHERE id = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir sala: " + e.getMessage());
            return false;
        }
    }

}
