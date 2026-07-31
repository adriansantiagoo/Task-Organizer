package tasks;

public class ChecklistItem {
    private String description;
    private boolean isDone;

    public ChecklistItem(String description){
        this.description = description;
        this.isDone = false;
    }
    public String getDescription() {
        return description;
    }
    public boolean isDone() {
        return isDone;
    }
    public void setDone(boolean done) {
        isDone = done;
    }
}
