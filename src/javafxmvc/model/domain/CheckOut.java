package src.javafxmvc.model.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class CheckOut {
    private int idCheckOut;
    private LocalDate data;
    private LocalTime hora;
    private int checkIn_id;
    
    public CheckOut() {
    }

    public CheckOut(int idCheckOut, LocalDate data, LocalTime hora, int checkIn_id) {
        this.idCheckOut = idCheckOut;
        this.data = data;
        this.hora = hora;
        this.checkIn_id = checkIn_id;
    }

    public int getIdCheckOut() {
        return idCheckOut;
    }

    public void setIdCheckOut(int idCheckOut) {
        this.idCheckOut = idCheckOut;
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

    public int getCheckIn_id() {
        return checkIn_id;
    }

    public void setCheckIn_id(int checkIn_id) {
        this.checkIn_id = checkIn_id;
    }

    @Override
    public String toString() {
        return "CheckOut [idCheckOut=" + idCheckOut + ", data=" + data + ", hora=" + hora + ", checkIn_id=" + checkIn_id
                + "]";
    }

}
