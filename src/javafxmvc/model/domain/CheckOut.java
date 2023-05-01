package src.javafxmvc.model.domain;

import java.util.Date;

public class CheckOut {
    private int idCheckOut;
    private Date data;
    private Date hora; //ver certinho como usa hora
    private int checkIn_id;
    
    public CheckOut() {
    }

    public CheckOut(int idCheckOut, Date data, Date hora, int checkIn_id) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
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
