package model;

public class TipoSala {
    private int id;
    private String nome;
    private int capacidade;

    public TipoSala(int id, String nome, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
    }

    public TipoSala(String nome, int capacidade) {
        this.nome = nome;
        this.capacidade = capacidade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    @Override
    public String toString() {
        return nome + " (" + capacidade + ")";
    }
}
