package src.javafxmvc.model.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class CheckIn {
    private int idCheckIn;
    private LocalDate data;
    private LocalTime hora;
    private int aluno_id;

    public CheckIn() {
    }

    public CheckIn(int idCheckIn, LocalDate data, LocalTime hora, int checkIn_id) {
        this.idCheckIn = idCheckIn;
        this.data = data;
        this.hora = hora;
    }

    public int getIdCheckIn() {
        return idCheckIn;
    }

    public void setIdCheckIn(int idCheckIn) {
        this.idCheckIn = idCheckIn;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(int aluno_id) {
        this.aluno_id = aluno_id;
    }

    @Override
    public String toString() {
        return "CheckIn [idCheckIn=" + idCheckIn + ", data=" + data + ", hora=" + hora + ", aluno_id=" + aluno_id + "]";
    }
    
}


