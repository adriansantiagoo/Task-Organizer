package tasks;

import java.time.LocalDate;

public class UndoAction {
    private ActionType type;
    private Task task;
    private LocalDate previousDate;

    public UndoAction(ActionType type, Task task, LocalDate previousDate){
        this.type = type;
        this.task = task;
        this.previousDate = previousDate;
    }
    public UndoAction(ActionType type, Task task){
        this(type, task, null);
    }
    public ActionType getType() {
        return type;
    }
    public Task getTask() {
        return task;
    }
    public LocalDate getPreviousDate() {
        return previousDate;
    }
}
