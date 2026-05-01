package ro.mpp.triathlon.model;

//import model.Entity;

public class Referee extends Entity {
    private String name;
    private String password;
    private Integer id_event;

    public Referee(int id, String name, String password, Integer id_event) {
        super(id);
        this.name = name;
        this.password = password;
        this.id_event = id_event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdEvent() {
        return id_event;
    }

    public void setIdEvent(int id_event) {
        this.id_event = id_event;
    }
}
