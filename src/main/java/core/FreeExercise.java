package core;

/**
 * exercise with description
 * @author OE
 */
public class FreeExercise extends Exercise {
    private String description;

    /**
     * constructor
     * @param exerciseID id of object
     * @param name
     * @param description explains usages
     */
    public FreeExercise(int exerciseID, String name, String description) {
        super(exerciseID, name);
        this.description = description;
    }

    /**
     * constructor without id for controller
     * @param name
     * @param description
     */
    public FreeExercise(String name, String description) {
        super(name);
        this.description = description;
    }

    /**
     *
     * @return freexercises description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description changes description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}