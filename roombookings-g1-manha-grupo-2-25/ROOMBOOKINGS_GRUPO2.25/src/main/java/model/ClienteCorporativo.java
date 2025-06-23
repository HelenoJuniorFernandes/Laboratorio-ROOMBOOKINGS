package model;

public class ClienteCorporativo extends Cliente {

    public ClienteCorporativo(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public boolean isCorporativo() {
        return true;
    }

    @Override
    public String toString() {
        return "Cliente Corporativo - Nome: " + nome + ", Documento: " + documento;
    }
}
