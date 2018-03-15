package core;

/**
 * exercise on machine
 * @author OE
 */
public class MachineExercise extends Exercise {
    private int kilograms;
    private int sets;
    private Machine machine;

    /**
     * constructor with all parameters
     * @param exerciseID
     * @param name
     * @param kilograms
     * @param sets
     * @param machine
     */
    public MachineExercise(int exerciseID, String name, int kilograms, int sets, Machine machine) {
        super(exerciseID, name);
        this.kilograms = kilograms;
        this.sets = sets;
        this.machine = machine;
    }

    /**
     * constructor without id and machine
     * @param name
     * @param kilograms
     * @param sets
     */
    public MachineExercise(String name, int kilograms, int sets) {
        super(name);
        this.kilograms = kilograms;
        this.sets = sets;
    }

    /**
     *
     * @return kilogram
     */
    public int getKilograms() {
        return kilograms;
    }

    /**
     *
     * @param kilograms setter
     */
    public void setKilograms(int kilograms) {
        this.kilograms = kilograms;
    }

    /**
     *
     * @return sets
     */
    public int getSets() {
        return sets;
    }

    /**
     *
     * @param sets setter
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     *
     * @return machine
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     *
     * @param machine setter
     */
    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}