package core;

public class FreeExercise extends Exercise {
    private String description;

    public FreeExercise(int exerciseID, String name, String description) {
        super(exerciseID, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}