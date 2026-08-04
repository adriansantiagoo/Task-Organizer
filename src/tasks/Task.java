package tasks;

import java.time.LocalDate;

public abstract class Task {
    private final int id;
    private String title;
    private boolean isDone;
    private static int nextId = 0;

    public Task(String title) {
        this.id = nextId++;
        this.title = title;
        this.isDone = false;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isDone() {
        return isDone;
    }
    public void setDone(boolean done) {
        isDone = done;
    }

    public abstract int calculateUrgency();
    public abstract LocalDate getScheduledDate();
}
