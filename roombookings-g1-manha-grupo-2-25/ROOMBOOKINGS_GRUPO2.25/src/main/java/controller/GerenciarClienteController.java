package controller;

import dao.ClienteDAO;
import model.Cliente;
import model.ClienteCorporativo;
import model.ClienteNormal;

import java.util.List;

public class GerenciarClienteController {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public boolean salvarCliente(Cliente cliente) {
        return clienteDAO.salvar(cliente);
    }

    public Cliente buscarPorDocumento(String documento) {
        return clienteDAO.buscarPorDocumento(documento);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listar();
    }

    public String cadastrarCliente(String nome, String documento, boolean corporativo) {
        Cliente cliente = corporativo
                ? new ClienteCorporativo(nome, documento)
                : new ClienteNormal(nome, documento);

        boolean sucesso = clienteDAO.salvar(cliente);
        return sucesso ? null : "❌ Cliente já cadastrado.";
    }
    public boolean excluirCliente(String documento) {
        ClienteDAO dao = new ClienteDAO();
        return dao.excluir(documento);
    }

}
