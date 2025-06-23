package view;

import controller.GerenciarReservaController;
import model.Reserva;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GerenciarReservaView extends JFrame {

    private final GerenciarReservaController controller = new GerenciarReservaController();
    private JTable tabelaReservas;
    private DefaultTableModel tabelaModel;
    private JScrollPane scrollPane;
    private JPanel painelCentral;

    public GerenciarReservaView() {
        setTitle("Gerenciar Reservas");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        add(criarMenuLateral(), BorderLayout.WEST);
        painelCentral = criarPainelCentral();
        add(painelCentral, BorderLayout.CENTER);

        painelCentral.add(criarPainelTabela(), BorderLayout.CENTER);
        scrollPane.setVisible(false);

        setVisible(true);
    }

    private JPanel criarMenuLateral() {
        JPanel menu = new JPanel(new GridLayout(7, 1, 10, 10));
        menu.setBackground(new Color(230, 230, 250));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        Font fonte = new Font("SansSerif", Font.BOLD, 13);
        Color corBotao = new Color(60, 120, 200);

        menu.add(criarBotao("Criar Reserva", corBotao, Color.WHITE, fonte, this::criarReserva));
        menu.add(criarBotao("Cancelar Reserva", corBotao, Color.WHITE, fonte, this::cancelarReserva));
        menu.add(criarBotao("Consultar Reserva", corBotao, Color.WHITE, fonte, this::consultarReserva));
        menu.add(criarBotao("Verificar Valor", corBotao, Color.WHITE, fonte, this::verificarValor));
        menu.add(criarBotao("Listar Reservas", corBotao, Color.WHITE, fonte, this::listarReservas));
        menu.add(criarBotao("Voltar", new Color(200, 200, 200), Color.BLACK, fonte, this::dispose));

        return menu;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Menu de Reservas", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(30, 30, 30));
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        painel.add(titulo, BorderLayout.NORTH);
        return painel;
    }

    private JScrollPane criarPainelTabela() {
        String[] colunas = {"Código", "Cliente", "Sala", "Início", "Fim", "Cancelada"};
        tabelaModel = new DefaultTableModel(colunas, 0);
        tabelaReservas = new JTable(tabelaModel);
        tabelaReservas.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(tabelaReservas);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        return scrollPane;
    }

    private JButton criarBotao(String texto, Color bg, Color fg, Font fonte, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.setBackground(bg);
        botao.setForeground(fg);
        botao.setFont(fonte);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(200, 40));
        botao.addActionListener(e -> acao.run());
        return botao;
    }

    private void criarReserva() {
        JTextField docField = new JTextField();
        JTextField salaField = new JTextField();
        JTextField inicioField = new JTextField();
        JTextField duracaoField = new JTextField();

        Object[] msg = {
                "CPF ou CNPJ do Cliente:", docField,
                "Código da Sala:", salaField,
                "Data e Hora de Início (dd/MM/yyyy HH:mm):", inicioField,
                "Duração (em horas):", duracaoField
        };

        int opt = JOptionPane.showConfirmDialog(this, msg, "Criar Reserva", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            String resultado = controller.criarReservaComValidacao(
                    docField.getText(), salaField.getText(), inicioField.getText(), duracaoField.getText());

            if (resultado.startsWith("OK:")) {
                String codigo = resultado.split(":")[1];
                JOptionPane.showMessageDialog(this, "✅ Reserva criada! Código: " + codigo);
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        scrollPane.setVisible(false);
    }

    private void cancelarReserva() {
        String input = JOptionPane.showInputDialog(this, "Código da reserva:");
        if (input != null) {
            try {
                int codigo = Integer.parseInt(input);
                boolean sucesso = controller.cancelarReserva(codigo);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "✅ Reserva cancelada!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "❌ Cancelamento não permitido.\nVerifique se a reserva existe e se há mais de 24 horas de antecedência.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Código inválido.");
            }
        }
        scrollPane.setVisible(false);
    }

    private void consultarReserva() {
        String input = JOptionPane.showInputDialog(this, "Código da reserva:");
        if (input != null) {
            try {
                int codigo = Integer.parseInt(input);
                var reserva = controller.listarReservas().stream()
                        .filter(r -> r.getCodigo() == codigo)
                        .findFirst().orElse(null);

                if (reserva != null) {
                    JTextArea textArea = new JTextArea(reserva.toString());
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                            "Detalhes da Reserva", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Reserva não encontrada.");
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Código inválido.");
            }
        }
        scrollPane.setVisible(false);
    }

    private void verificarValor() {
        String input = JOptionPane.showInputDialog(this, "Código da reserva:");
        if (input != null) {
            try {
                int codigo = Integer.parseInt(input);
                var reserva = controller.listarReservas().stream()
                        .filter(r -> r.getCodigo() == codigo)
                        .findFirst().orElse(null);

                if (reserva != null) {
                    JOptionPane.showMessageDialog(this,
                            "💰 Valor da reserva: R$ " + String.format("%.2f", reserva.calcularCusto()));
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Reserva não encontrada.");
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Código inválido.");
            }
        }
        scrollPane.setVisible(false);
    }

    private void listarReservas() {
        List<Reserva> reservas = controller.listarReservas();
        tabelaModel.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Reserva r : reservas) {
            tabelaModel.addRow(new Object[]{
                    r.getCodigo(),
                    r.getCliente().getNome(),
                    r.getSala().getId(),
                    r.getInicio().format(dtf),
                    r.getFim().format(dtf),
                    r.isCancelada() ? "Sim" : "Não"
            });
        }

        scrollPane.setVisible(true);
        painelCentral.revalidate();
        painelCentral.repaint();
    }
}
