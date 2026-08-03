package tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;

public class MeetingTask extends Task implements Reschedulable{
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;

    public MeetingTask(String title, LocalDateTime startTime, LocalDateTime endTime, String location) {
        super(title);
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }

    @Override
    public int calculateUrgency() {
        LocalDateTime now = LocalDateTime.now();

        // ended: past endTime
        if (now.isAfter(endTime)) return 0;

        // in progress: between start and end (inclusive of start)
        if (!now.isBefore(startTime)) return 4;   // now >= startTime and (from above) now <= endTime

        // before it starts: ramp up as it approaches
        long minutesRemaining = MINUTES.between(now, startTime);
        if (minutesRemaining > 1440) return 1;   // more than a day out
        if (minutesRemaining > 60)   return 2;   // within the last day
        return 3;                                // within the final hour
    }

    @Override
    public void reschedule(LocalDate newDate) {
        // assumes same-day meeting
        LocalTime originalStartTime = startTime.toLocalTime();
        startTime = newDate.atTime(originalStartTime);

        LocalTime originalEndTime = endTime.toLocalTime();
        endTime = newDate.atTime(originalEndTime);
    }
}
