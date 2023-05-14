package src.javafxmvc.model.domain;

import java.time.LocalDate;

public class TreinoExercicio {
    private int idTreinoExercicio; 
    private int treino_id;
    private int exercicio_id; 
    private Treino treino;
    private Exercicio exercicio;

    public TreinoExercicio(){
    }

    public TreinoExercicio(int idTreinoExercicio, int treino_id, int exercicio_id) {
        this.idTreinoExercicio = idTreinoExercicio;
        this.treino_id = treino_id;
        this.exercicio_id = exercicio_id;
    }

    public int getIdTreinoExercicio() {
        return idTreinoExercicio;
    }

    public void setIdTreinoExercicio(int idTreinoExercicio) {
        this.idTreinoExercicio = idTreinoExercicio;
    }

    public int getIdTreino() {
        return treino_id;
    }

    public void setIdTreino(int treino_id) {
        this.treino_id = treino_id;
    }

    public int getIdExercicio() {
        return exercicio_id;
    }

    public void setIdExercicio(int exercicio_id) {
        this.exercicio_id = exercicio_id;
    }

    public String getExercicioTipo() {
        return exercicio.getTipo();
    }

    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
    }

    public String getTreinoNomeAluno() {
        return treino.getAlunoNome();
    }

    public String getTreinoNomeFuncionario() {
        return treino.getFuncionarioNome();
    }

    public LocalDate getTreinoDataInicio() {
        return treino.getDataInicio();
    }

    public LocalDate getTreinoDataFinal() {
        return treino.getDataFinal();
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
    }


    @Override
    public String toString() {
        return "Treino [ idTreinoExercicio = " + idTreinoExercicio + ", treino_id=" + treino_id + ", exercicio_id=" + exercicio_id+ "]";
    }
}
