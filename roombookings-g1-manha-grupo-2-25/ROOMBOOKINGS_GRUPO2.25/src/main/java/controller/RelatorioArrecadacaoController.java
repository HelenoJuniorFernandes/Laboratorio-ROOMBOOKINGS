package controller;

import dao.RelatorioDAO;
import java.util.List;

public class RelatorioArrecadacaoController {

    private final RelatorioDAO dao = new RelatorioDAO();

    public List<Object[]> gerar() {
        return dao.gerarArrecadacaoPorPeriodo();
    }
}
