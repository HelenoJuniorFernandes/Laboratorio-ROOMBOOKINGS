package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sala {
    public static final double VALOR_BASE_HORA = 50.0;

    private int id;
    private int capacidade;
    private TipoSala tipoSala;
    private List<SalaRecurso> salaRecursos;

    public Sala(int capacidade, TipoSala tipoSala) {
        this.capacidade = capacidade;
        this.tipoSala = tipoSala;
        this.salaRecursos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public TipoSala getTipoSala() {
        return tipoSala;
    }

    public List<SalaRecurso> getRecursos() {
        return salaRecursos;
    }

    public void adicionarRecurso(SalaRecurso salaRecurso) {
        this.salaRecursos.add(salaRecurso);
    }

    public double calcularCusto(double horas) {
        return VALOR_BASE_HORA * horas * (1 + obterAdicionalCusto());
    }

    public double calcularReembolso(double valorPago) {
        return valorPago * obterPercentualReembolso();
    }

    private double obterAdicionalCusto() {
        return switch (tipoSala.getNome().toUpperCase()) {
            case "STANDARD" -> 0.0;
            case "PREMIUM" -> 0.15;
            case "VIP" -> 0.30;
            default -> 0.0;
        };
    }

    private double obterPercentualReembolso() {
        return switch (tipoSala.getNome().toUpperCase()) {
            case "STANDARD" -> 0.60;
            case "PREMIUM" -> 0.40;
            case "VIP" -> 0.30;
            default -> 0.0;
        };
    }

    @Override
    public String toString() {
        String recursosString = salaRecursos.stream()
                .map(SalaRecurso::toString)
                .collect(Collectors.joining(", "));
        return String.format("ID: %d | Capacidade: %d | Tipo: %s | Recursos: [%s]",
                id, capacidade, tipoSala.getNome(), recursosString);
    }
}
