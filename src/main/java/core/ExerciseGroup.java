package core;
import core.Exercise;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An exercisegroup contains exercises who has something in common
 * @author OE
 */
public class ExerciseGroup {
    private int exerciseGroupID;
    private String name;
    private Collection<Exercise> exercises;

    /**
     * Constructor
     * @param exerciseGroupID
     * @param name
     */
    public ExerciseGroup(int exerciseGroupID, String name) {
        this.exerciseGroupID = exerciseGroupID;
        this.name = name;
        this.exercises = new ArrayList<Exercise>();
    }

    /**
     * constructor without id, for use in controller
     * @param name
     */
    public ExerciseGroup(String name) {
        this.name = name;
        this.exercises = new ArrayList<Exercise>();
    }

    /**
     *
     * @return objects id
     */
    public int getExerciseGroupID() {
        return exerciseGroupID;
    }

    /**
     *
     * @param exerciseGroupID set objects id
     */
    public void setExerciseGroupID(int exerciseGroupID) {
        this.exerciseGroupID = exerciseGroupID;
    }

    /**
     *
     * @return object name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name set objects name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return list of exercises in group
     */
    public Collection<Exercise> getExercises() {
        return exercises;
    }

    /**
     *
     * @param exercise
     * @return true if exercise in group
     */
    public boolean hasExercise(Exercise exercise) {
        return this.exercises.contains(exercise);
    }

    /**
     *
     * @param exercise adds exercise to group if not already in it
     */
    public void addExercise(Exercise exercise) {
        if (! this.exercises.contains(exercise)) {
            this.exercises.add(exercise);
        }
        if (! exercise.hasGroup(this)) {
            exercise.addGroup(this);
        }
    }

    /**
     *
     * @param exercise remove exercise from group if exercise in group
     */
    public void removeExercise(Exercise exercise) {
        if (this.exercises.contains(exercise)) {
            this.exercises.remove(exercise);
        }
        if (exercise.hasGroup(this)) {
            exercise.removeGroup(this);
        }
    }
}