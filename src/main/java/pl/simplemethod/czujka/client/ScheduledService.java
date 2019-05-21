package pl.simplemethod.czujka.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledService {
    private static final Logger logger = LogManager.getLogger(ScheduledService.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
    private ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
    private ZonedDateTime nextStartDate = currentDate.withHour(3).withMinute(33).withSecond(0);


    private class DropLists implements Runnable
    {
        @Override
        public void run() {
            logger.error("Wykonanie zadania");
            nextStartDate = nextStartDate.plusDays(1);
            logger.error("Następne start: "+ nextStartDate.format(formatter));
        }
    }

    ScheduledService()
    {
        logger.warn("Aktualna data: "+ currentDate.format(formatter));
        logger.warn("Planowany start: "+ nextStartDate.format(formatter));

        if(currentDate.compareTo(nextStartDate) > 0)
        {
            nextStartDate = nextStartDate.plusDays(1);
            logger.warn("Nowe okno czasowe: "+ nextStartDate.format(formatter));
        }

        Duration duration = Duration.between(currentDate, nextStartDate);
        long delay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new DropLists(), delay,  TimeUnit.DAYS.toSeconds(1),TimeUnit.SECONDS);
    }

}
