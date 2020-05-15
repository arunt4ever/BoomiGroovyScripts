import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterwithZone {
    public static void main(String[] args) {
        //Input & Output
        String InDateTime = "20191017 184657.000";
        String InDateFormat = "yyyyMMdd HHmmss.SSS";
        String useCurrentDateTime = "true";
        String InDateZone = "UTC";
        String OutDateZone = "EST5EDT";
        String OutDateFormat = "yyyyMMdd HHmmss.SSS";
        String OutDateTime;


        // Set the Source Date Format;
        DateTimeFormatter InFormatter = DateTimeFormatter.ofPattern(InDateFormat);

        ZoneId sourceZone = ZoneId.of(InDateZone);

        if (useCurrentDateTime.trim()
                              .toUpperCase()
                              .equals("TRUE")) {
            InDateTime = LocalDateTime.now(sourceZone)
                    .format(InFormatter);
            System.out.println("InDateTime:"+InDateTime);
        }

        LocalDateTime sourceDateTime = LocalDateTime.parse(InDateTime, InFormatter);
        ZonedDateTime zonedSource = ZonedDateTime.of(sourceDateTime, sourceZone);

        ZoneId destZone = ZoneId.of(OutDateZone);

        ZonedDateTime destDateTime = zonedSource.withZoneSameInstant(destZone);

        DateTimeFormatter OutFormatter = DateTimeFormatter.ofPattern(OutDateFormat);

        OutDateTime = destDateTime.format(OutFormatter);
        System.out.println("OutDateTime:"+OutDateTime);

    }
}
