package controller;

import dao.SalaDAO;
import dao.TipoSalaDAO;
import model.Sala;
import model.TipoSala;

import java.util.List;

public class GerenciarSalaController {

    private final SalaDAO salaDAO = new SalaDAO();
    private final TipoSalaDAO tipoSalaDAO = new TipoSalaDAO();

    public Sala cadastrarSala(int capacidade, String tipoNome) {
        // Buscar o tipo de sala no banco de dados
        TipoSala tipoSala = tipoSalaDAO.buscarPorNome(tipoNome.toUpperCase());

        if (tipoSala == null) {
            throw new IllegalArgumentException("Tipo de sala inválido: " + tipoNome);
        }

        // Criar e salvar a sala
        Sala sala = new Sala(capacidade, tipoSala);
        salaDAO.inserir(sala);
        return sala;
    }

    public Sala buscarPorId(int id) {
        return salaDAO.buscarPorId(id);
    }

    public List<Sala> listarSalas() {
        return salaDAO.listarTodas();
    }
    public boolean excluirSala(int id) {
        SalaDAO dao = new SalaDAO();
        return dao.excluir(id);
    }

}
