package dao;

import model.Cliente;
import model.Reserva;
import model.Sala;
import util.ConexaoPostgres;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final SalaDAO salaDAO = new SalaDAO();

    public void inserir(Reserva reserva) {
        String sql = "INSERT INTO Reserva (cliente_documento, sala_id, inicio, fim, cancelada) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, reserva.getCliente().getDocumento());
            stmt.setInt(2, reserva.getSala().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(reserva.getInicio()));
            stmt.setTimestamp(4, Timestamp.valueOf(reserva.getFim()));
            stmt.setBoolean(5, reserva.isCancelada());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                reserva.setCodigo(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir reserva: " + e.getMessage());
        }
    }

    public boolean existeConflito(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT COUNT(*) FROM Reserva " +
                "WHERE sala_id = ? AND cancelada = false AND " +
                "(inicio < ? AND fim > ?)";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sala.getId());
            stmt.setTimestamp(2, Timestamp.valueOf(fim));
            stmt.setTimestamp(3, Timestamp.valueOf(inicio));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar conflito de reserva: " + e.getMessage());
        }

        return true; // se der erro, assume conflito por segurança
    }

    public boolean cancelar(int reservaId) {
        String select = "SELECT inicio FROM reserva WHERE id = ?";
        String update = "UPDATE reserva SET cancelada = TRUE WHERE id = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmtSelect = conn.prepareStatement(select)) {

            stmtSelect.setInt(1, reservaId);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                LocalDateTime inicio = rs.getTimestamp("inicio").toLocalDateTime();
                LocalDateTime agora = LocalDateTime.now();

                if (agora.plusHours(24).isBefore(inicio)) {
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(update)) {
                        stmtUpdate.setInt(1, reservaId);
                        int afetados = stmtUpdate.executeUpdate();
                        return afetados > 0;
                    }
                } else {
                    System.err.println("❌ Cancelamento não permitido: falta menos de 24 horas.");
                    return false;
                }
            } else {
                System.err.println("❌ Reserva não encontrada.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao cancelar reserva: " + e.getMessage());
            return false;
        }
    }



    public List<Reserva> listarTodas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reserva";

        try (Connection conn = ConexaoPostgres.obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String documento = rs.getString("cliente_documento");
                int salaId = rs.getInt("sala_id");
                LocalDateTime inicio = rs.getTimestamp("inicio").toLocalDateTime();
                LocalDateTime fim = rs.getTimestamp("fim").toLocalDateTime();
                boolean cancelada = rs.getBoolean("cancelada");

                Cliente cliente = clienteDAO.buscarPorDocumento(documento);
                Sala sala = salaDAO.buscarPorId(salaId);

                Reserva reserva = new Reserva(cliente, sala, inicio, fim);
                reserva.setCodigo(id);

                if (cancelada) {
                    reserva.cancelar(); // marca como cancelada
                }

                reservas.add(reserva);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar reservas: " + e.getMessage());
        }

        return reservas;
    }
}
