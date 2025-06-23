package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Reserva {
    private int codigo;
    private Cliente cliente;
    private Sala sala;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private boolean cancelada;

    public Reserva(Cliente cliente, Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        this.cliente = cliente;
        this.sala = sala;
        this.inicio = inicio;
        this.fim = fim;
        this.cancelada = false;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public double calcularCusto() {
        double horas = (double) Duration.between(inicio, fim).toMinutes() / 60;
        double valor = sala.calcularCusto(horas);
        if (cliente.isCorporativo()) {
            valor *= 0.9;
        }
        return valor;
    }

    public double calcularReembolso() {
        if (!cancelada)
            return 0;
        return sala.calcularReembolso(calcularCusto());
    }

    public boolean cancelar() {
        LocalDateTime agora = LocalDateTime.now();
        if (agora.plusHours(24).isBefore(inicio)) {
            cancelada = true;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reserva #").append(codigo).append("\n");
        sb.append("Cliente: ").append(cliente.getNome())
                .append(" (").append(cliente.getDocumento()).append(")\n");
        sb.append("Sala: ").append(sala.getId()).append("\n");
        sb.append("Início: ").append(inicio).append("\n");
        sb.append("Fim: ").append(fim).append("\n");
        sb.append("Status: ").append(cancelada ? "Cancelada" : "Ativa").append("\n");
        sb.append("Custo: R$ ").append(String.format("%.2f", calcularCusto())).append("\n");
        if (cancelada) {
            sb.append("Reembolso: R$ ").append(String.format("%.2f", calcularReembolso())).append("\n");
        }
        return sb.toString();
    }

}