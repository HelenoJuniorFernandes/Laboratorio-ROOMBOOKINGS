package view;

import controller.GerenciarSalaController;
import model.Sala;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarSalaView extends JFrame {

    private final GerenciarSalaController gerenciarSalaController = new GerenciarSalaController();

    private JTextField capacidadeField;
    private JComboBox<String> tipoCombo;
    private JTable tabelaSalas;
    private DefaultTableModel tabelaModel;
    private JScrollPane scrollPane;

    private JPanel painelFormulario;
    private JPanel painelCentral;

    public GerenciarSalaView() {
        setTitle("Gerenciar Salas");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));

        add(criarMenuVertical(), BorderLayout.WEST);

        painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(new Color(245, 245, 245));
        add(painelCentral, BorderLayout.CENTER);

        painelFormulario = criarPainelFormulario();
        scrollPane = criarPainelTabela();

        painelFormulario.setVisible(false);
        scrollPane.setVisible(false);

        painelCentral.add(painelFormulario, BorderLayout.NORTH);
        painelCentral.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Trecho atualizado do método criarMenuVertical()

    private JPanel criarMenuVertical() {
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(7, 1, 5, 5));  // aumentei para 7 linhas
        menu.setBackground(new Color(230, 230, 250));
        menu.setBorder(new EmptyBorder(15, 15, 15, 15));

        JButton btnCadastrar = new JButton("Cadastrar Sala");
        JButton btnBuscar = new JButton("Buscar Sala");
        JButton btnListar = new JButton("Listar Salas");
        JButton btnExcluir = new JButton("Excluir Sala"); // Novo botão

        estilizarBotao(btnCadastrar);
        estilizarBotao(btnBuscar);
        estilizarBotao(btnListar);
        estilizarBotao(btnExcluir);

        btnCadastrar.addActionListener(e -> mostrarFormulario());
        btnBuscar.addActionListener(e -> buscarSala());
        btnListar.addActionListener(e -> listarSalas());
        btnExcluir.addActionListener(e -> excluirSala());  // Ação do botão excluir

        menu.add(btnCadastrar);
        menu.add(btnBuscar);
        menu.add(btnListar);
        menu.add(btnExcluir);  // adiciona no menu

        return menu;
    }


    private JPanel criarPainelFormulario() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Nova Sala"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        capacidadeField = new JTextField(10);
        tipoCombo = new JComboBox<>(new String[]{"STANDARD", "PREMIUM", "VIP"});

        JButton btnSalvar = new JButton("Salvar Sala");
        estilizarBotao(btnSalvar);
        btnSalvar.addActionListener(e -> cadastrarSala());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Capacidade:"), gbc);

        gbc.gridx = 1;
        formPanel.add(capacidadeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Tipo de Sala:"), gbc);

        gbc.gridx = 1;
        formPanel.add(tipoCombo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnSalvar, gbc);

        return formPanel;
    }

    private JScrollPane criarPainelTabela() {
        String[] colunas = {"Código", "Capacidade", "Tipo", "Recursos"};
        tabelaModel = new DefaultTableModel(colunas, 0);
        tabelaSalas = new JTable(tabelaModel);
        tabelaSalas.setFillsViewportHeight(true);

        JScrollPane sp = new JScrollPane(tabelaSalas);
        sp.setBorder(new EmptyBorder(10, 10, 10, 10));
        return sp;
    }

    private void mostrarFormulario() {
        painelFormulario.setVisible(true);
        scrollPane.setVisible(false);
        painelCentral.revalidate();
        painelCentral.repaint();
    }

    private void cadastrarSala() {
        try {
            int capacidade = Integer.parseInt(capacidadeField.getText().trim());
            String tipo = tipoCombo.getSelectedItem().toString();

            Sala sala = gerenciarSalaController.cadastrarSala(capacidade, tipo);
            JOptionPane.showMessageDialog(this, "✅ Sala cadastrada com código: " + sala.getId());

            capacidadeField.setText("");
            tipoCombo.setSelectedIndex(0);
            painelFormulario.setVisible(false);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Capacidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarSala() {
        String input = JOptionPane.showInputDialog(this, "Digite o código da sala:");
        if (input != null && !input.isBlank()) {
            try {
                int codigo = Integer.parseInt(input);
                Sala sala = gerenciarSalaController.buscarPorId(codigo);
                if (sala != null) {
                    String recursos = sala.getRecursos().isEmpty()
                            ? "Nenhum"
                            : sala.getRecursos().stream()
                            .map(r -> r.getNome())
                            .reduce((a, b) -> a + ", " + b).orElse("");

                    JOptionPane.showMessageDialog(this,
                            "Sala encontrada:\nCódigo: " + sala.getId() +
                                    "\nCapacidade: " + sala.getCapacidade() +
                                    "\nTipo: " + sala.getTipoSala().getNome() +
                                    "\nRecursos: " + recursos);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Sala não encontrada.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Código inválido.");
            }
        }

        painelFormulario.setVisible(false);
        scrollPane.setVisible(false);
    }

    private void listarSalas() {
        List<Sala> salas = gerenciarSalaController.listarSalas();
        tabelaModel.setRowCount(0);

        for (Sala s : salas) {
            String tipo = s.getTipoSala().getNome();
            String recursos = s.getRecursos().isEmpty()
                    ? "Nenhum"
                    : s.getRecursos().stream()
                    .map(r -> r.getNome())
                    .reduce((a, b) -> a + ", " + b).orElse("");
            tabelaModel.addRow(new Object[]{s.getId(), s.getCapacidade(), tipo, recursos});
        }

        scrollPane.setVisible(true);
        painelFormulario.setVisible(false);
        painelCentral.revalidate();
        painelCentral.repaint();
    }
    private void excluirSala() {
        String input = JOptionPane.showInputDialog(this, "Digite o código da sala para excluir:");

        if (input != null && !input.isBlank()) {
            try {
                int id = Integer.parseInt(input.trim());

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Deseja realmente excluir a sala de código: " + id + "?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean sucesso = gerenciarSalaController.excluirSala(id);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this, "✅ Sala excluída com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Não é possível excluir. Existem reservas associadas.");
                    }
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Código inválido.");
            }
        }
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setBackground(new Color(60, 120, 200));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
    }
}
