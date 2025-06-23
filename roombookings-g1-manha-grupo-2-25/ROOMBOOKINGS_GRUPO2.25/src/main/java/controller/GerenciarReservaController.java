package controller;

import dao.ClienteDAO;
import dao.ReservaDAO;
import dao.SalaDAO;
import model.Cliente;
import model.Reserva;
import model.Sala;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GerenciarReservaController {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final SalaDAO salaDAO = new SalaDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public List<Reserva> listarReservas() {
        return reservaDAO.listarTodas();
    }

    public boolean cancelarReserva(int codigo) {
        return reservaDAO.cancelar(codigo);
    }


    public String criarReservaComValidacao(String documento, String codSalaStr, String inicioStr, String duracaoStr) {
        try {
            int codSala = Integer.parseInt(codSalaStr.trim());
            LocalDateTime inicio = LocalDateTime.parse(inicioStr.trim(), formatter);
            int duracao = Integer.parseInt(duracaoStr.trim());
            LocalDateTime fim = inicio.plusHours(duracao);

            Cliente cliente = clienteDAO.buscarPorDocumento(documento.trim());
            Sala sala = salaDAO.buscarPorId(codSala);

            if (cliente == null || sala == null) {
                return "❌ Cliente ou sala não encontrados.";
            }

            boolean conflito = reservaDAO.existeConflito(sala, inicio, fim);
            if (conflito) {
                return "❌ Já existe uma reserva para esta sala neste horário.";
            }

            Reserva reserva = new Reserva(cliente, sala, inicio, fim);
            reservaDAO.inserir(reserva); // novo método correto
            return "OK:" + reserva.getCodigo();

        } catch (Exception e) {
            return "❌ Erro nos dados: " + e.getMessage();
        }
    }


}
