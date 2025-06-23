package view;

import controller.RelatorioMediaHorasController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RelatorioMediaHorasPorClienteView extends JFrame {

    private final RelatorioMediaHorasController controller = new RelatorioMediaHorasController();
    private JTable tabela;
    private DefaultTableModel tabelaModel;
    private JScrollPane scrollPane;
    private JTextField campoDocumento;
    private JPanel painelCentral;

    public RelatorioMediaHorasPorClienteView() {
        setTitle("Relatório: Média de Horas por Cliente");
        setSize(750, 500);
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
        menu.setBorder(new EmptyBorder(20, 15, 20, 15));

        JButton btnFiltrar = new JButton("Filtrar / Atualizar");
        estilizarBotao(btnFiltrar);
        btnFiltrar.addActionListener(e -> gerarRelatorio());

        JButton btnFechar = new JButton("Voltar");
        estilizarBotao(btnFechar);
        btnFechar.setBackground(new Color(200, 200, 200));
        btnFechar.setForeground(Color.BLACK);
        btnFechar.addActionListener(e -> dispose());

        menu.add(btnFiltrar);
        menu.add(btnFechar);

        return menu;
    }

    private JPanel criarFiltro() {
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtro.setBackground(new Color(245, 245, 245));

        campoDocumento = new JTextField(15);
        filtro.add(new JLabel("Filtrar por CPF/CNPJ (opcional):"));
        filtro.add(campoDocumento);

        return filtro;
    }

    private JScrollPane criarTabela() {
        String[] colunas = {"Cliente", "Documento", "Tipo", "Média de Horas"};
        tabelaModel = new DefaultTableModel(colunas, 0);
        tabela = new JTable(tabelaModel);
        tabela.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private void gerarRelatorio() {
        String filtro = campoDocumento.getText().trim();
        tabelaModel.setRowCount(0);

        List<Object[]> dados = controller.gerar(filtro);

        if (dados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Nenhuma reserva encontrada para o CPF/CNPJ informado.");
            scrollPane.setVisible(false);
            return;
        }

        for (Object[] linha : dados) {
            tabelaModel.addRow(linha);
        }

        scrollPane.setVisible(true);
        painelCentral.revalidate();
        painelCentral.repaint();
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setBackground(new Color(60, 120, 200));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
