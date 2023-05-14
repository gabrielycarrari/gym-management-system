package src.javafxmvc.model.domain;

import java.time.LocalDate;

public class Treino {
    private int idTreino; 
    private LocalDate dataInicio;
    private LocalDate dataFinal; 
    private int aluno_id;
    private String aluno_nome;
    private int funcionario_id; 
    private String funcionario_nome;

    public Treino(){
    }

    public Treino(int idTreino, LocalDate dataInicio, LocalDate dataFinal) {
        this.idTreino = idTreino;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
    }

    public int getIdTreino() {
        return idTreino;
    }

    public void setIdTreino(int idTreino) {
        this.idTreino = idTreino;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getAlunoNome() {
        return aluno_nome;
    }

    public void setIdAluno(int aluno_id) {
        this.aluno_id = aluno_id;
    }

    public int getIdAluno() {
        return aluno_id;
    }

    public void setAlunoNome(String aluno_nome) {
        this.aluno_nome = aluno_nome;
    }

    public String getFuncionarioNome() {
        return funcionario_nome;
    }

    public void setFuncionarioNome(String funcionario_nome) {
        this.funcionario_nome = funcionario_nome;
    }

    public int getIdFuncionario() {
        return funcionario_id;
    }

    public void setIdFuncionario(int funcionario_id) {
        this.funcionario_id = funcionario_id;
    }

    @Override
    public String toString() {
        return "Treino [ idTreino = " + idTreino + ", dataInicio=" + dataInicio + ", dataFinal=" + dataFinal + ", aluno_id=" + aluno_id + " aluno_nome=" + aluno_nome + ", funcionario_id=" + funcionario_id + ", funcionario_nome=" + funcionario_nome+ "]";
    }
}
