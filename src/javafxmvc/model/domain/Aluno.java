package src.javafxmvc.model.domain;

public class Aluno {
    private int idAluno;
    private String nome;
    private String cpf;
    private String endereco;
    private String genero;
    private int pontos;
    private int plano_id;

    public Aluno() {
    }

    public Aluno(int idAluno, String nome, String cpf, String endereco, String genero, int pontos, int plano_id) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.genero = genero;
        this.pontos = pontos;
        this.plano_id = plano_id;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getPlano_id() {
        return plano_id;
    }

    public void setPlano_id(int plano_id) {
        this.plano_id = plano_id;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
