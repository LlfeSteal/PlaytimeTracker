package io.github.llfesteal.PlaytimeTracker.utils;

public final class StringUtils {

    public static boolean isUUID(String candidate) {
        return candidate.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
    }
}
