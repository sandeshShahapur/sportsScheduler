package com.sandeshshahapur.sportsScheduler.controller;

import com.sandeshshahapur.sportsScheduler.service.SportsSchedulerService;
import com.sandeshshahapur.sportsScheduler.Model.SportsScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins="https://sandeshshahapur.github.io/")
@RestController
public class SportsSchedulerController {

    @Autowired
    private SportsSchedulerService sportsSchedulerService;

    @PostMapping("/generateStandardSchedule")
    public ResponseEntity<?> generateStandardSchedule(@RequestBody SportsScheduleRequest request) {
        try {
            // Call the service method to generate the schedule
            List<List<String[]>> schedules = sportsSchedulerService.standardSchedule(
                    request.getTeams(),
                    request.getMatchesBtwTeams(),
                    request.getDayNumMatches(),
                    request.getTeamMatchesGap(),
                    request.getTeamDaysGap(),
                    request.isAlwEmtDay(),
                    request.isAlwTeamPairRecur(),
                    request.getOpSchedCnt()
            );

            // Return the schedule as a response
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
