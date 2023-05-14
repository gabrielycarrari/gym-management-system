package src.javafxmvc.model.domain;

public class Exercicio {
    private int idExercicio; 
    private String tipo; 
    private String musculo; 
    private int numSeries; 
    private int numRepeticoes; 

    public Exercicio(){
    }

    public Exercicio(int idExercicio, String tipo, String musculo, int numSeries, int numRepeticoes) {
        this.idExercicio = idExercicio;
        this.tipo = tipo;
        this.musculo = musculo;
        this.numSeries = numSeries;
        this.numRepeticoes = numRepeticoes; 
    }

    public int getIdExercicio() {
        return idExercicio;
    }

    public void setIdExercicio(int idExercicio) {
        this.idExercicio = idExercicio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMusculo() {
        return musculo;
    }

    public void setMusculo(String musculo) {
        this.musculo = musculo;
    }

    public int getNumSeries() {
        return numSeries;
    }

    public void setNumSeries(int numSeries) {
        this.numSeries = numSeries;
    }

    public int getNumRepeticoes() {
        return numRepeticoes;
    }

    public void setNumRepeticoes(int numRepeticoes) {
        this.numRepeticoes = numRepeticoes;
    }

    @Override
    public String toString() {
        return "Exercicio [idExercicio=" + idExercicio + ", tipo=" + tipo + ", musculo=" + musculo + ", numSeries="
                + numSeries + ", numRepeticoes=" + numRepeticoes + "]";
    }
}
