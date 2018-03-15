package core;

/**
 * Machine is where you do your MachineExercises
 * @author OE
 */
public class Machine {
    private int machineID;
    private String name;
    private String description;

    /**
     * constructor
     * @param machineID
     * @param name
     */
    public Machine(int machineID, String name) {
        this.machineID = machineID;
        this.name = name;
    }

    /**
     * constructor for controller
     * @param name
     */
    public Machine(String name) {
        this.name = name;
        //this.description = description;
    }

    /**
     *
     * @return machineid
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     *
     * @param machineID setter
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name setter
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description setter
     */
    public void setDescription(String description) {
        this.description = description;
    }
}