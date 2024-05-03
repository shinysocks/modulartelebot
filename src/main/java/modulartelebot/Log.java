package modulartelebot;

import java.time.LocalDateTime;

public class Log {
    private static final String DEFAULT = "\u001b[0m";
    public static enum FLAVOR {
        ERR,
        SUCCESS,
        INFO
    }

    public static void log(String message, FLAVOR flavor) {
        LocalDateTime now = LocalDateTime.now();
        String time = String.format("%s-%s-%s [%s:%s:%s] ", now.getDayOfMonth(), now.getMonthValue(), now.getYear(),
                now.getHour(), now.getMinute(), now.getSecond());
        String color;
        switch (flavor) {
            case ERR:
                color = "\u001b[31;1m";
                break;
            case SUCCESS:
                color = "\u001b[32;1m";
                break;
            case INFO:
                color = "\u001b[34;1m";
                break;
            default:
                color = "";
                break;
        }

        System.out.println(color + time + message + DEFAULT);
    }
}
