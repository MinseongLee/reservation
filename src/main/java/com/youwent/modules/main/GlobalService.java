package com.youwent.modules.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
public class GlobalService {
    public LocalTime stringToLocalTime(String time) {
        if (isLocalTime(time)) {
            String[] times = splitTime(time);
            int hour = Integer.parseInt(times[0]);
            int minutes = Integer.parseInt(times[1]);
            return LocalTime.of(hour, minutes);
        }
        log.error("failed to format LocalTime. so, init hour(0) and minute(0)");
        return LocalTime.of(0,0);
    }
    public boolean isLocalTime(String time) {
        String[] times = splitTime(time);
        int hour = Integer.parseInt(times[0]);
        int minutes = Integer.parseInt(times[1]);
        return 0 <= hour && hour <= 23 && 0 <= minutes && minutes <= 59;
    }
    private String[] splitTime(String time) { return time.split(":"); }
}
