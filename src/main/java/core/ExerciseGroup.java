package core;
import core.Exercise;

import java.util.ArrayList;
import java.util.Collection;

public class ExerciseGroup {
    private int exerciseGroupID;
    private String name;
    private Collection<Exercise> exercises;

    public ExerciseGroup(int exerciseGroupID, String name) {
        this.exerciseGroupID = exerciseGroupID;
        this.name = name;
        this.exercises = new ArrayList<Exercise>();
    }

    public int getExerciseGroupID() {
        return exerciseGroupID;
    }

    public void setExerciseGroupID(int exerciseGroupID) {
        this.exerciseGroupID = exerciseGroupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Exercise> getExercises() {
        return exercises;
    }

    public boolean hasExercise(Exercise exercise) {
        return this.exercises.contains(exercise);
    }
    public void addExercise(Exercise exercise) {
        if (! this.exercises.contains(exercise)) {
            this.exercises.add(exercise);
        }
        if (! exercise.hasGroup(this)) {
            exercise.addGroup(this);
        }
    }
    public void removeExercise(Exercise exercise) {
        if (this.exercises.contains(exercise)) {
            this.exercises.remove(exercise);
        }
        if (exercise.hasGroup(this)) {
            exercise.removeGroup(this);
        }
    }
}