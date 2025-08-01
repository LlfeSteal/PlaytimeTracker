package io.github.llfesteal.PlaytimeTracker.utils;

import java.time.Duration;

public final class TimeUtils {
    public static String format(Duration duration, String format) {
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format(format, days, hours, minutes, seconds);
    }
}
