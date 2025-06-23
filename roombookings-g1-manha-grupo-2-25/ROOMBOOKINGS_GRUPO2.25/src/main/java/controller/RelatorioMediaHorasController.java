package controller;

import dao.RelatorioDAO;
import java.util.List;

public class RelatorioMediaHorasController {

    private final RelatorioDAO dao = new RelatorioDAO();

    public List<Object[]> gerar(String cpfFiltro) {
        return dao.gerarMediaHorasPorCliente(cpfFiltro);
    }
}
