package controller;

import dao.RelatorioDAO;
import java.util.List;

public class RelatorioSalasMaisReservadasController {

    private final RelatorioDAO dao = new RelatorioDAO();

    public List<Object[]> gerar() {
        return dao.gerarSalasMaisReservadasPorPeriodo();
    }
}
