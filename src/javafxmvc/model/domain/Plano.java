package src.javafxmvc.model.domain;

public class Plano {
    private int idPlano;
    private String tipo;
    private float preco;

    public Plano() {
    }

    public Plano(int idPlano, String tipo, float preco) {
        this.idPlano = idPlano;
        this.tipo = tipo;
        this.preco = preco;
    }

    public int getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(int idPlano) {
        this.idPlano = idPlano;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Plano [idPlano=" + idPlano + ", tipo=" + tipo + ", preco=" + preco + "]";
    }
}