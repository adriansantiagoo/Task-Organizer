package tasks;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChecklistTask extends Task implements Reschedulable{
    private final List<ChecklistItem> items = new ArrayList<>();
    private LocalDate dueDate;

    public ChecklistTask(String title, LocalDate dueDate) {
        super(title);
        this.dueDate = dueDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public List<ChecklistItem> getItems(){
        return Collections.unmodifiableList(items);
    }

    public void addItem(String description){
        items.add(new ChecklistItem(description));
    }

    public void markItem(int index, boolean done){
        items.get(index).setDone(done);
    }

    public long getUndoneItems(){
        return items.stream()
                .filter(i -> !i.isDone())
                .count();
    }

    public double calculateUrgencyByPercentage(){
        double fractionUndone = (double) getUndoneItems() / items.size();

        if (fractionUndone > 0.75) return 2.0;
        if (fractionUndone > 0.5)  return 1.5;
        if (fractionUndone > 0.25) return 1.0;
        if (getUndoneItems() == 0) return 0;
        return 0.5;
    }

    @Override
    public int calculateUrgency() {
        if (items.isEmpty()) return 0;

        long daysUntilDueDate = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

        double deadlineWeight;
        if (daysUntilDueDate > 15)      deadlineWeight = 0.5;
        else if (daysUntilDueDate > 7)  deadlineWeight = 1.0;
        else if (daysUntilDueDate > 3)  deadlineWeight = 1.5;
        else                            deadlineWeight = 2.0;   // ≤ 3 days = most urgent

        double urgency = deadlineWeight + calculateUrgencyByPercentage();
        return (int) Math.round(urgency);
    }

    @Override
    public void reschedule(LocalDate newDate) {
        dueDate = newDate;
    }
}
