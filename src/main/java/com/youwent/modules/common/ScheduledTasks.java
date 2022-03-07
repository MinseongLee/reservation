package com.youwent.modules.common;

import com.youwent.modules.facility.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final FacilityService facilityService;

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledUpdateNowReserveCnt() {
        facilityService.updateAllNowReserveCnt();
    }
}
