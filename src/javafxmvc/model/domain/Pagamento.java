package src.javafxmvc.model.domain;

import java.time.LocalDate;

public class Pagamento {
    private int idPagamento;
    private LocalDate data;
    private float valor;
    private int aluno_id;

    public Pagamento() {
    }

    public Pagamento(int idPagamento, LocalDate data, float valor, int aluno_id) {
        this.idPagamento = idPagamento;
        this.data = data;
        this.valor = valor;
        this.aluno_id = aluno_id;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate localDate) {
        this.data = localDate;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(int aluno_id) {
        this.aluno_id = aluno_id;
    }
    
    @Override
    public String toString() {
        return "Pagamento [idPagamento=" + idPagamento + ", data=" + data + ", valor=" + valor + ", aluno_id=" + aluno_id
                + "]";
    }

}
