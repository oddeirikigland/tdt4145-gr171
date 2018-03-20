package core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract class used in FreeExercise and MachinExercise class
 * they have common name and id, an exercise can be part of zero to many exercisegroups
 * @author OE
 */
public abstract class Exercise {
    private int exerciseID;
    private String name;
    private Collection<ExerciseGroup> groups;

    /**
     * Constructor who taked in id, name and initializes list of exercisegrups
     * @param exerciseID unique ID for exercise
     * @param name name of exercise
     */
    public Exercise (int exerciseID, String name) {
        this.exerciseID = exerciseID;
        this.name = name;
        this.groups = new ArrayList<ExerciseGroup>();
    }

    /**
     * constructor without id, needed for create,
     * beacuse database has autoincrement for id
     * @param name
     */
    public Exercise(String name) {
        this.name = name;
        this.groups = new ArrayList<ExerciseGroup>();
    }

    /**
     *
     * @return objects id
     */
    public int getExerciseID() {
        return exerciseID;
    }

    /**
     *
     * @param exerciseID sets exercise id
     */
    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    /**
     *
     * @return objects name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name sets exercise name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return exercisegroups exercise belongs to
     */
    public Collection<ExerciseGroup> getGroups() {
        return groups;
    }

    /**
     *
     * @param group
     * @return true if exercise is part of group
     */
    public boolean hasGroup(ExerciseGroup group) {
        return this.groups.contains(group);
    }

    /**
     *
     * @param group adds group to exercise if not already registered
     */
    public void addGroup(ExerciseGroup group) {
        if (! this.groups.contains(group)) {
            this.groups.add(group);
        }
        if (! group.hasExercise(this)) {
            group.addExercise(this);
        }
    }

    /**
     *
     * @param group remove group from exercise if exist in list
     */
    public void removeGroup(ExerciseGroup group) {
        if (this.groups.contains(group)) {
            this.groups.remove(group);
        }
        if (group.hasExercise(this)) {
            group.removeExercise(this);
        }
    }
}