package br.com.jessebot.botinter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
Date sequence generator for a Crontab pattern, allowing clients to specify a pattern that the sequence matches.
The pattern is a list of six single space-separated fields:
 representing second, minute, hour, day, month, weekday.
 Month and weekday names can be given as the first three letters of the English names.

Example patterns:

"0 0 * * * *" = the top of every hour of every day.
"* /10 * * * * *" = every ten seconds.
"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
"0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.
"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
"0 0 0 25 12 ?" = every Christmas Day at midnight
 */

@Component
public class SchedulerGeral {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final String TIME_ZONE = "America/Sao_Paulo";

    @Scheduled(cron = "0 0/15 * * * *", zone = TIME_ZONE)
    public void meiaNoite() throws IOException {
        PostaTweet postaTweet = new PostaTweet();
        postaTweet.executar();
    }
}
