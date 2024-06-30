package models;

import java.time.LocalDate;
import java.util.List;

public class Rover {
    private int id;
    private String name;
    private String landing_date;
    private String launch_date;
    private String status;
    private List<Camera> cameras;

    public Rover(int id, String name, String landing_date, String launch_date, String status, List<Camera> cameras) {
        this.id = id;
        this.name = name;
        this.landing_date = landing_date;
        this.launch_date = launch_date;
        this.status = status;
        this.cameras = cameras;
    }

    public Rover(){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLanding_date() {
        return landing_date;
    }

    public String getLaunch_date() {
        return launch_date;
    }

    public String getStatus() {
        return status;
    }

    public List<Camera> getCameras() {
        return cameras;
    }
}
