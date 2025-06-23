package model;

public class ClienteNormal extends Cliente {

    public ClienteNormal(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public boolean isCorporativo() {
        return false;
    }

    @Override
    public String toString() {
        return "Cliente Normal - Nome: " + nome + ", Documento: " + documento;
    }
}
