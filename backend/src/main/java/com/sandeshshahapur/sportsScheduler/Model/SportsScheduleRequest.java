package com.sandeshshahapur.sportsScheduler.Model;

import lombok.Data;
import lombok.Getter;

// Define a POJO (Plain Old Java Object) to represent the request body
@Data
public class SportsScheduleRequest {
    private String[] teams;
    private long matchesBtwTeams;
    private long[] dayNumMatches;
    private long teamMatchesGap;
    private long teamDaysGap;
    private boolean alwEmtDay;
    private boolean alwTeamPairRecur;
    private int opSchedCnt;
}
