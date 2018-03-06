package core;

import java.util.Collection;
import java.util.Date;

public class Workout {

    private int workoutID;
    private Date timestamp;
    private int duration;

    /* 1-10 */
    private int form;
    private int performance;

    private String note;

    private Collection<Exercise> workoutExercise;

    public Workout(int workoutID, Date timestamp, int duration, int form, int performance, String note, Collection<Exercise> workoutExercises){
        this.workoutID = workoutID;
        this.timestamp = timestamp;
        this.duration = duration;
        this.form = form;
        this.performance = performance;
        this.note = note;
        this.workoutExercise = workoutExercises;
    }
    public Workout(int workoutID, Collection<Exercise> workoutExercise) {
        this.workoutID = workoutID;
        this.workoutExercise = workoutExercise;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public int getPerformance() {
        return performance;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Collection<Exercise> getWorkoutExercise() {
        return workoutExercise;
    }

    public void addExercise(Exercise exercise) {
        if (! this.workoutExercise.contains(exercise)) {
            this.workoutExercise.add(exercise);
        }
    }
    public void removeExercise(Exercise exercise) {
        if (this.workoutExercise.contains(exercise)) {
            this.workoutExercise.remove(exercise);
        }
    }
}



