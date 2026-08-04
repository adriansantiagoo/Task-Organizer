package tasks;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SimpleTask extends Task implements Reschedulable{
    private LocalDate dueDate;

    public SimpleTask(String title, LocalDate dueDate) {
        super(title);
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public int calculateUrgency() {
        final LocalDate today = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(today, dueDate);
        if (daysRemaining >= 7) return 1;
        if (daysRemaining >= 3) return 2;
        if (daysRemaining >= 1) return 3;
        return 4;
    }

    @Override
    public LocalDate getScheduledDate() {
        return dueDate;
    }

    @Override
    public void reschedule(LocalDate newDate) {
        dueDate = newDate;
    }
}
