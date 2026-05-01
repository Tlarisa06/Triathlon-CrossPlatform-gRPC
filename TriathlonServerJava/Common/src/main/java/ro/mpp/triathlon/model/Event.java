package ro.mpp.triathlon.model;

//import model.Entity;

public class Event extends Entity {
    private Integer idRef;
    private Integer idPart;
    private Integer points;

    public Event(int id, Integer idRef, Integer idPart, Integer points) {
        super(id);
        this.idRef = idRef;
        this.idPart = idPart;
        this.points = points;
    }

    public Integer getIdRef() {
        return idRef;
    }

    public void setIdRef(Integer idRef) {
        this.idRef = idRef;
    }

    public Integer getIdPart() {
        return idPart;
    }

    public void setIdPart(Integer idPart) {
        this.idPart = idPart;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
