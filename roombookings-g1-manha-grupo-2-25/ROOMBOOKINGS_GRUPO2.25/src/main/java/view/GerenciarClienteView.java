package view;

import controller.GerenciarClienteController;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarClienteView extends JFrame {

    private final GerenciarClienteController gerenciarClienteController = new GerenciarClienteController();

    private JTextField nomeField;
    private JTextField docField;
    private JCheckBox corporativoBox;
    private JTable tabelaClientes;
    private DefaultTableModel tabelaModel;
    private JScrollPane scrollPane;

    private JPanel painelFormulario;
    private JPanel painelCentral;

    public GerenciarClienteView() {
        setTitle("Gerenciar Clientes");
        setSize(700, 550);
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

    private JPanel criarMenuVertical() {
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(7, 1, 5, 5)); // Aumentei para 7 linhas
        menu.setBackground(new Color(230, 230, 250));
        menu.setBorder(new EmptyBorder(15, 15, 15, 15));

        JButton btnCadastrar = new JButton("Cadastrar Cliente");
        JButton btnBuscar = new JButton("Buscar Cliente");
        JButton btnListar = new JButton("Listar Clientes");
        JButton btnExcluir = new JButton("Excluir Cliente"); // Novo botão

        estilizarBotao(btnCadastrar);
        estilizarBotao(btnBuscar);
        estilizarBotao(btnListar);
        estilizarBotao(btnExcluir);

        btnCadastrar.addActionListener(e -> mostrarFormulario());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnListar.addActionListener(e -> listarClientes());
        btnExcluir.addActionListener(e -> excluirCliente()); // Ação do excluir

        menu.add(btnCadastrar);
        menu.add(btnBuscar);
        menu.add(btnListar);
        menu.add(btnExcluir); // Novo botão

        return menu;
    }


    private JPanel criarPainelFormulario() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Novo Cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        nomeField = new JTextField(15);
        docField = new JTextField(15);
        corporativoBox = new JCheckBox("Corporativo");

        JButton btnSalvar = new JButton("Salvar Cliente");
        estilizarBotao(btnSalvar);
        btnSalvar.addActionListener(e -> cadastrarCliente());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("CPF/CNPJ:"), gbc);

        gbc.gridx = 1;
        formPanel.add(docField, gbc);

        gbc.gridx = 2;
        formPanel.add(corporativoBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnSalvar, gbc);

        return formPanel;
    }

    private JScrollPane criarPainelTabela() {
        String[] colunas = {"Nome", "Documento", "Tipo"};
        tabelaModel = new DefaultTableModel(colunas, 0);
        tabelaClientes = new JTable(tabelaModel);
        tabelaClientes.setFillsViewportHeight(true);

        JScrollPane sp = new JScrollPane(tabelaClientes);
        sp.setBorder(new EmptyBorder(10, 10, 10, 10));
        return sp;
    }

    private void mostrarFormulario() {
        painelFormulario.setVisible(true);
        scrollPane.setVisible(false);
        painelCentral.revalidate();
        painelCentral.repaint();
    }

    private void cadastrarCliente() {
        String nome = nomeField.getText().trim();
        String doc = docField.getText().trim();
        boolean corp = corporativoBox.isSelected();

        String erro = gerenciarClienteController.cadastrarCliente(nome, doc, corp);
        if (erro == null) {
            JOptionPane.showMessageDialog(this, "✅ Cliente cadastrado com sucesso!");
            nomeField.setText("");
            docField.setText("");
            corporativoBox.setSelected(false);
            painelFormulario.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, erro, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCliente() {
        String doc = JOptionPane.showInputDialog(this, "Digite o CPF ou CNPJ:");
        if (doc != null && !doc.isBlank()) {
            Cliente cliente = gerenciarClienteController.buscarPorDocumento(doc);
            if (cliente != null) {
                JOptionPane.showMessageDialog(this, "Cliente encontrado:\n\nNome: " +
                        cliente.getNome() + "\nDocumento: " + cliente.getDocumento());
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cliente não encontrado.");
            }
        }
        painelFormulario.setVisible(false);
        scrollPane.setVisible(false);
    }

    private void listarClientes() {
        List<Cliente> clientes = gerenciarClienteController.listarClientes();
        tabelaModel.setRowCount(0);

        for (Cliente c : clientes) {
            String tipo = c.isCorporativo() ? "Corporativo" : "Pessoa Física";
            tabelaModel.addRow(new Object[]{c.getNome(), c.getDocumento(), tipo});
        }

        scrollPane.setVisible(true);
        painelFormulario.setVisible(false);
        painelCentral.revalidate();
        painelCentral.repaint();
    }
    private void excluirCliente() {
        String doc = JOptionPane.showInputDialog(this, "Digite o CPF ou CNPJ do cliente para excluir:");

        if (doc != null && !doc.isBlank()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir o cliente com documento: " + doc + "?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean sucesso = gerenciarClienteController.excluirCliente(doc.trim());
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "✅ Cliente excluído com sucesso.");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Não é possível excluir. Existem reservas associadas.");
                }
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
