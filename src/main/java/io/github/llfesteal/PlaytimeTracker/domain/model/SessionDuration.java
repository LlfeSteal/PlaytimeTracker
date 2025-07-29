package io.github.llfesteal.PlaytimeTracker.domain.model;

import java.time.Duration;

public class SessionDuration {

    private final long days;
    private final long hours;
    private final long minutes;
    private final long seconds;

    public SessionDuration(Duration duration) {
        days =  duration.toDays();
        hours =  duration.toHoursPart();
        minutes =  duration.toMinutesPart();
        seconds =  duration.toSecondsPart();
    }

    public String format(String format) {
        return String.format(format, days, hours, minutes, seconds);
    }
}
