package tasks;

import java.time.LocalDate;

public class Milestone implements Reschedulable{
    private String title;
    private LocalDate dueDate;
    private boolean isDone;

    public Milestone(String title, LocalDate dueDate){
        this.title = title;
        this.dueDate = dueDate;
        this.isDone = false;
    }
    public LocalDate getDueDate() {
        return dueDate;
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

    @Override
    public void reschedule(LocalDate newDate) {
        dueDate = newDate;
    }
}
