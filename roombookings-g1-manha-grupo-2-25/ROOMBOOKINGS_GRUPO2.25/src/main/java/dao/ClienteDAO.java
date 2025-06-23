package dao;

import model.Cliente;
import model.ClienteCorporativo;
import model.ClienteNormal;
import util.ConexaoPostgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO Cliente (documento, nome, corporativo) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getDocumento());
            stmt.setString(2, cliente.getNome());
            stmt.setBoolean(3, cliente.isCorporativo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    public Cliente buscarPorDocumento(String documento) {
        String sql = "SELECT * FROM Cliente WHERE documento = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                boolean corporativo = rs.getBoolean("corporativo");

                if (corporativo) {
                    return new ClienteCorporativo(nome, documento);
                } else {
                    return new ClienteNormal(nome, documento);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }

        return null;
    }

    public boolean salvar(Cliente cliente) {
        if (buscarPorDocumento(cliente.getDocumento()) == null) {
            inserir(cliente);
            return true;
        }
        return false;
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";

        try (Connection conn = ConexaoPostgres.obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String documento = rs.getString("documento");
                boolean corporativo = rs.getBoolean("corporativo");

                Cliente cliente = corporativo
                        ? new ClienteCorporativo(nome, documento)
                        : new ClienteNormal(nome, documento);

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    public boolean possuiReservas(String documento) {
        String sql = "SELECT 1 FROM reserva WHERE cliente_documento = ? LIMIT 1";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Erro ao verificar reservas do cliente: " + e.getMessage());
            return true; // Por segurança, assume que tem reserva se der erro
        }
    }

    public boolean excluir(String documento) {
        if (possuiReservas(documento)) {
            return false;
        }

        String sql = "DELETE FROM cliente WHERE documento = ?";

        try (Connection conn = ConexaoPostgres.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
            return false;
        }
    }

}
