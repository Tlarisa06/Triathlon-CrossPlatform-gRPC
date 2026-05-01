package ro.mpp.triathlon.model;

//import model.Entity;

public class Participant extends Entity {
    private String name;
    private Integer totalPoints;

    public Participant(int id, String name, Integer totalPoints) {
        super(id);
        this.name = name;
        this.totalPoints = totalPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
}
