package tasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskDay implements Comparable<TaskDay>{
    private LocalDate date;
    private final List<Task> tasks = new ArrayList<>();   // never null

    public TaskDay(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public int compareTo(TaskDay t) {
        return date.compareTo(t.getDate());
    }
}
