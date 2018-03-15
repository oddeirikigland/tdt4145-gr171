package core;


public class Machine {
    private int machineID;
    private String name;
    private String description;

    public Machine(int machineID, String name) {
        this.machineID = machineID;
        this.name = name;
    }

    public Machine(String name) {
        this.name = name;
    }

    public int getMachineID() {
        return machineID;
    }
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}