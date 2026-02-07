package com.smeportal.profileservice.scheduler;

import com.smeportal.profileservice.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@EnableScheduling
public class ProfilePurgeScheduler {

    @Autowired
    private ProfileService profileService;

    @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
    public void purgeOldDeletedProfiles() {
        profileService.purgeDeletedProfiles(Duration.ofDays(7)); // Purge profiles deleted more than 7 days ago
    }

//    @Scheduled(cron = "*/10 * * * * *")
//    public void purgeOldDeletedProfiles() {
//        profileService.purgeDeletedProfiles(Duration.ofSeconds(600)); // Purge profiles deleted more than 30 seconds ago
//    }
}
