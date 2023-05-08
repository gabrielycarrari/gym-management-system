package src.javafxmvc.model.domain;

import java.io.Serializable;

public class Funcionario implements Serializable {
    private int idFuncionario;
    private String nome;
    private String cpf;
    private String endereco;
    private String tipo;
    private String usuario;
    private String senha;

    public Funcionario() {
    }

    public Funcionario(int idFuncionario, String nome, String cpf, String endereco, String tipo, String usuario,
            String senha) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.tipo = tipo;
        this.usuario = usuario;
        this.senha = senha;
    }
    
    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Funcionario [idFuncionario=" + idFuncionario + ", nome=" + nome + ", cpf=" + cpf + ", endereco="
                + endereco + ", tipo=" + tipo + "]";
    }
}
