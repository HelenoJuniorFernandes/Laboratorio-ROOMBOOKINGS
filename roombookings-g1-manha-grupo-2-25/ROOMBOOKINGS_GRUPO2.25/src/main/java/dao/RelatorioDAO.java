package dao;

import util.ConexaoPostgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    /**
     * Relatório 1: Total arrecadado por período (MM/YYYY)
     */
    public List<Object[]> gerarArrecadacaoPorPeriodo() {
        List<Object[]> resultados = new ArrayList<>();

        String sql = """
            SELECT TO_CHAR(inicio, 'MM/YYYY') AS periodo,
                   COUNT(*) AS total_reservas,
                   SUM(
                       CASE
                           WHEN cliente.corporativo THEN 
                               (EXTRACT(EPOCH FROM (fim - inicio)) / 3600) * 50 * (1 +
                                   CASE tipo.nome
                                       WHEN 'STANDARD' THEN 0
                                       WHEN 'PREMIUM' THEN 0.15
                                       WHEN 'VIP' THEN 0.30
                                   END
                               ) * 0.9
                           ELSE 
                               (EXTRACT(EPOCH FROM (fim - inicio)) / 3600) * 50 * (1 +
                                   CASE tipo.nome
                                       WHEN 'STANDARD' THEN 0
                                       WHEN 'PREMIUM' THEN 0.15
                                       WHEN 'VIP' THEN 0.30
                                   END
                               )
                       END
                   ) AS total_arrecadado
            FROM reserva
            JOIN cliente ON reserva.cliente_documento = cliente.documento
            JOIN sala ON reserva.sala_id = sala.id
            JOIN tiposala tipo ON sala.tipo_id = tipo.id
            WHERE NOT cancelada
            GROUP BY TO_CHAR(inicio, 'MM/YYYY')
            ORDER BY periodo;
        """;

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String periodo = rs.getString("periodo");
                String total = String.format("R$ %.2f", rs.getDouble("total_arrecadado"));
                int quantidade = rs.getInt("total_reservas");
                resultados.add(new Object[]{periodo, total, quantidade});
            }

        } catch (SQLException e) {
            System.err.println("Erro no relatório de arrecadação: " + e.getMessage());
        }

        return resultados;
    }

    /**
     * Relatório 2: Média de horas por cliente
     */
    public List<Object[]> gerarMediaHorasPorCliente(String cpfFiltro) {
        List<Object[]> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT
                c.nome,
                c.documento,
                CASE WHEN c.corporativo THEN 'PJ' ELSE 'PF' END AS tipo,
                ROUND(AVG(EXTRACT(EPOCH FROM (r.fim - r.inicio)) / 3600), 2) AS media_horas
            FROM reserva r
            JOIN cliente c ON c.documento = r.cliente_documento
            WHERE r.cancelada = FALSE
        """);

        if (cpfFiltro != null && !cpfFiltro.isBlank()) {
            sql.append(" AND c.documento = ? ");
        }

        sql.append("""
            GROUP BY c.nome, c.documento, c.corporativo
            ORDER BY c.nome;
        """);

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (cpfFiltro != null && !cpfFiltro.isBlank()) {
                stmt.setString(1, cpfFiltro.trim());
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("nome"),
                        rs.getString("documento"),
                        rs.getString("tipo"),
                        rs.getDouble("media_horas")
                });
            }

        } catch (Exception e) {
            System.err.println("Erro no relatório de média por cliente: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Relatório 3: Salas mais reservadas por período (MM/YYYY)
     */
    public List<Object[]> gerarSalasMaisReservadasPorPeriodo() {
        List<Object[]> resultados = new ArrayList<>();

        String sql = """
            SELECT TO_CHAR(inicio, 'MM/YYYY') AS periodo,
                   sala.id AS sala_id,
                   COUNT(*) AS total_reservas
            FROM reserva
            JOIN sala ON reserva.sala_id = sala.id
            WHERE NOT cancelada
            GROUP BY periodo, sala.id
            ORDER BY periodo, total_reservas DESC;
        """;

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String periodo = rs.getString("periodo");
                int salaId = rs.getInt("sala_id");
                int total = rs.getInt("total_reservas");
                resultados.add(new Object[]{periodo, salaId, total});
            }

        } catch (SQLException e) {
            System.err.println("Erro no relatório de salas mais reservadas: " + e.getMessage());
        }

        return resultados;
    }
}
