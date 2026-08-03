package tasks;

import java.time.LocalDate;

public interface Reschedulable {
    void reschedule(LocalDate newDate);
}
