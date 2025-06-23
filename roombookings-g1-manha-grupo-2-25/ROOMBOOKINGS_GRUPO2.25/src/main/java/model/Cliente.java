package model;

public abstract class Cliente {
    protected String nome;
    protected String documento;

    public Cliente(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public abstract boolean isCorporativo();

    @Override
    public abstract String toString();
}