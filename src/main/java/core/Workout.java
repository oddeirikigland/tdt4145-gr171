package core;

import java.sql.Date;

public class Workout {

    private int workoutID;
    private Date timestamp;
    private int duration;

    /* 1-10 */
    private int form;
    private int performance;

    private String note;

    /**
     * Constructor used by DatabaseController
     * @param workoutID
     * @param timestamp
     * @param duration
     * @param form
     * @param performance
     * @param note
     */
    public Workout (
            int workoutID, Date timestamp,
            int duration, int form,
            int performance, String note
            ) {
        this.workoutID = workoutID;
        this.timestamp = timestamp;
        this.duration = duration;
        this.form = form;
        this.performance = performance;
        this.note = note;
    }

    /**
     * Constructor used to make non-persisted java-objects
     * @param timestamp
     * @param duration
     * @param form
     * @param performance
     * @param note
     */
    public Workout (Date timestamp, int duration, int form, int performance, String note) {
            this.timestamp = timestamp;
            this.duration = duration;
            this.form = form;
            this.performance = performance;
            this.note = note;
        }

    public int getDuration() {
        return duration;
    }

    public int getForm() {
        return form;
    }

    public int getPerformance() {
        return performance;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getNote() {
        return note;
    }
}


