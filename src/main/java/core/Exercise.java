package core;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Exercise {
    private int exerciseID;
    private String name;
    private Collection<ExerciseGroup> groups;

    public Exercise (int exerciseID, String name) {
        this.exerciseID = exerciseID;
        this.name = name;
        this.groups = new ArrayList<ExerciseGroup>();
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ExerciseGroup> getGroups() {
        return groups;
    }
    public boolean hasGroup(ExerciseGroup group) {
        return this.groups.contains(group);
    }

    public void addGroup(ExerciseGroup group) {
        if (! this.groups.contains(group)) {
            this.groups.add(group);
        }
        if (! group.hasExercise(this)) {
            group.addExercise(this);
        }

    }
    public void removeGroup(ExerciseGroup group) {
        if (this.groups.contains(group)) {
            this.groups.remove(group);
        }
        if (group.hasExercise(this)) {
            group.removeExercise(this);
        }
    }
}