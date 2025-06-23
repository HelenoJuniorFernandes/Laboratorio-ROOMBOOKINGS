package view;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    public MenuPrincipalView() {
        setTitle("RoomBookings - Menu Principal");
        setSize(520, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color azul = new Color(60, 120, 200);
        Color fundo = new Color(245, 245, 250);
        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 26);
        Font fonteSecao = new Font("Segoe UI", Font.BOLD, 18);
        Font fonteBotao = new Font("Segoe UI", Font.PLAIN, 16);

        // Painel principal com scroll
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(fundo);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título
        JLabel titulo = new JLabel("RoomBookings");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(fonteTitulo);
        titulo.setForeground(new Color(30, 30, 30));
        painelConteudo.add(titulo);
        painelConteudo.add(Box.createVerticalStrut(30));

        // Sessão de gerenciamento
        painelConteudo.add(criarBotao("Gerenciar Salas", azul, fonteBotao, GerenciarSalaView::new));
        painelConteudo.add(Box.createVerticalStrut(10));
        painelConteudo.add(criarBotao("Gerenciar Clientes", azul, fonteBotao, GerenciarClienteView::new));
        painelConteudo.add(Box.createVerticalStrut(10));
        painelConteudo.add(criarBotao("Gerenciar Reservas", azul, fonteBotao, GerenciarReservaView::new));
        painelConteudo.add(Box.createVerticalStrut(25));

        JLabel relatorioTitulo = new JLabel("Relatórios");
        relatorioTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        relatorioTitulo.setFont(fonteSecao);
        relatorioTitulo.setForeground(new Color(60, 60, 60));
        painelConteudo.add(relatorioTitulo);
        painelConteudo.add(Box.createVerticalStrut(15));

        painelConteudo.add(criarBotao("Total Arrecadado", azul, fonteBotao, RelatorioArrecadacaoView::new));
        painelConteudo.add(Box.createVerticalStrut(10));
        painelConteudo.add(criarBotao("Salas Mais Reservadas", azul, fonteBotao, RelatorioSalasMaisReservadasView::new));
        painelConteudo.add(Box.createVerticalStrut(10));
        painelConteudo.add(criarBotao("Média de Horas por Cliente", azul, fonteBotao, RelatorioMediaHorasPorClienteView::new));
        painelConteudo.add(Box.createVerticalStrut(25));

        // Rodapé
        painelConteudo.add(criarBotao("Sair", new Color(200, 50, 50), fonteBotao, () -> System.exit(0)));

        JScrollPane scrollPane = new JScrollPane(painelConteudo);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        getContentPane().setBackground(fundo);
        setVisible(true);
    }

    private JButton criarBotao(String texto, Color bg, Font fonte, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.setBackground(bg);
        botao.setForeground(Color.WHITE);
        botao.setFont(fonte);
        botao.setFocusPainted(false);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(320, 45));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botao.addActionListener(e -> acao.run());
        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPrincipalView::new);
    }
}
