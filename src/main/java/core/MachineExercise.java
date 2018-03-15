package core;


public class MachineExercise extends Exercise {
    private int kilograms;
    private int sets;
    private Machine machine;

    public MachineExercise(int exerciseID, String name, int kilograms, int sets, Machine machine) {
        super(exerciseID, name);
        this.kilograms = kilograms;
        this.sets = sets;
        this.machine = machine;
    }

    public MachineExercise(String name, int kilograms, int sets) {
        super(name);
        this.kilograms = kilograms;
        this.sets = sets;
    }

    public int getKilograms() {
        return kilograms;
    }

    public void setKilograms(int kilograms) {
        this.kilograms = kilograms;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}