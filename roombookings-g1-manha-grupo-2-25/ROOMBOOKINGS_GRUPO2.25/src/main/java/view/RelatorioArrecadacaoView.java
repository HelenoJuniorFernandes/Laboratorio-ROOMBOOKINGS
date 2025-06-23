package view;

import controller.RelatorioArrecadacaoController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.TreeSet;

public class RelatorioArrecadacaoView extends JFrame {

    private final RelatorioArrecadacaoController controller = new RelatorioArrecadacaoController();
    private JTable tabela;
    private DefaultTableModel tabelaModel;
    private JScrollPane scrollPane;
    private JComboBox<String> comboMes;
    private JComboBox<String> comboAno;
    private JPanel painelCentral;

    public RelatorioArrecadacaoView() {
        setTitle("Relatório: Arrecadação por Período");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(criarMenuLateral(), BorderLayout.WEST);

        painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(new Color(245, 245, 245));
        add(painelCentral, BorderLayout.CENTER);

        painelCentral.add(criarFiltro(), BorderLayout.NORTH);
        painelCentral.add(criarTabela(), BorderLayout.CENTER);
        scrollPane.setVisible(false);

        setVisible(true);
    }

    private JPanel criarMenuLateral() {
        JPanel menu = new JPanel(new GridLayout(3, 1, 10, 10));
        menu.setBackground(new Color(230, 230, 250));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton btnGerar = new JButton("Gerar Relatório");
        estilizarBotao(btnGerar);
        btnGerar.addActionListener(e -> gerarRelatorio());

        JButton btnFechar = new JButton("Voltar");
        estilizarBotao(btnFechar);
        btnFechar.setBackground(new Color(200, 200, 200));
        btnFechar.setForeground(Color.BLACK);
        btnFechar.addActionListener(e -> dispose());

        menu.add(btnGerar);
        menu.add(btnFechar);

        return menu;
    }

    private JPanel criarFiltro() {
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtro.setBackground(new Color(245, 245, 245));

        comboMes = new JComboBox<>(new String[]{
                "Todos", "01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"
        });

        comboAno = new JComboBox<>(getAnosDisponiveis());

        filtro.add(new JLabel("Mês:"));
        filtro.add(comboMes);
        filtro.add(new JLabel("Ano:"));
        filtro.add(comboAno);

        return filtro;
    }

    private JScrollPane criarTabela() {
        String[] colunas = {"Período", "Total Arrecadado", "Reservas"};
        tabelaModel = new DefaultTableModel(colunas, 0);
        tabela = new JTable(tabelaModel);
        tabela.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private void gerarRelatorio() {
        String mes = (String) comboMes.getSelectedItem();
        String ano = (String) comboAno.getSelectedItem();

        List<Object[]> dados = controller.gerar();
        tabelaModel.setRowCount(0);

        for (Object[] linha : dados) {
            String periodo = (String) linha[0];
            String[] partes = periodo.split("/");
            String m = partes[0];
            String a = partes[1];

            if ((!mes.equals("Todos") && !mes.equals(m)) || (!ano.equals("Todos") && !ano.equals(a))) {
                continue;
            }

            tabelaModel.addRow(linha);
        }

        scrollPane.setVisible(true);
        painelCentral.revalidate();
        painelCentral.repaint();
    }

    private String[] getAnosDisponiveis() {
        TreeSet<String> anos = new TreeSet<>();
        for (Object[] linha : controller.gerar()) {
            String periodo = (String) linha[0];
            String[] partes = periodo.split("/");
            if (partes.length == 2) anos.add(partes[1]);
        }

        String[] opcoes = new String[anos.size() + 1];
        opcoes[0] = "Todos";
        int i = 1;
        for (String ano : anos) opcoes[i++] = ano;
        return opcoes;
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setBackground(new Color(60, 120, 200));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
