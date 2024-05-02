package com.sandeshshahapur.sportsScheduler.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SportsSchedulerService {
    static String[] iplTeams2024 = new String[] {
            "Chennai Super Kings",
            "Delhi Capitals",
            "Gujarat Titans",
            "Kolkata Knight Riders",
            "Lucknow Super Giants",
            "Mumbai Indians",
            "Punjab Kings",
            "Rajasthan Royals"
    };

    static int[] oneDayNumMatches = new int[] { 1 };
    static int[] iplDayNumMatches = new int[] { 1, 1, 1, 1, 1, 2, 2 };

    String[] teams;
    int numTeams;
    long matchesBtwTeams;
    long[] dayNumMatches;
    long teamMatchesGap;
    long teamDaysGap;

    long numMatches;
    long numDays;

    int[][] playedEachOtherCount;
    int[] lastPlayedDay, lastPlayedMatch;
    String[] lastPlayedAgainstTeam;
    int[] teamMatchesPlayed;

    List<String[]> schedule;
    List<List<String[]>> res;
    int resCount = 0;


    /**
     * Initialises the state of the scheduler.
     * This method fills the arrays lastPlayedDay, lastPlayedMatch, and lastPlayedAgainstTeam with initial values.
     * Numeric arrays shouldn't be initialised with minimum values as that could result in underflow.
     */
    void intialiseState() {
        Arrays.fill(lastPlayedDay, -10000);
        Arrays.fill(lastPlayedMatch, -10000);
        Arrays.fill(lastPlayedAgainstTeam, "");
    }

    /**
     * Constructs a SportsScheduler object with the given parameters.
     *
     * @param teams           an array of team names participating in the tournament
     * @param matchesBtwTeams the number of matches played between each pair of teams
     * @param dayNumMatches   an array representing the number of matches to be played on each day
     * @param teamMatchesGap  the minimum gap between matches played by a team
     * @param teamDaysGap     the minimum gap between days on which a team plays
     * @throws IllegalArgumentException if the input parameters are invalid
     */
    public List<List<String[]>> standardSchedule(
            String[] teams,
            long matchesBtwTeams,
            long[] dayNumMatches,
            long teamMatchesGap,
            long teamDaysGap,
            boolean alwEmtDay,
            boolean alwTeamPairRecur,
            int opSchedCnt
    ) {
        if (teams.length < 2) {
            throw new IllegalArgumentException("Tournament must have at least 2 teams");
        } else if (matchesBtwTeams < 1) {
            throw new IllegalArgumentException("Matches played between teams must be greater than 0");
        } else if (dayNumMatches.length < 1) {
            throw new IllegalArgumentException("dayNumMatches must have at least 1 element");
        } else if (teamMatchesGap < 0) {
            throw new IllegalArgumentException("Gap between matches a team plays must be greater than or equal to 0");
        } else if (teamDaysGap < 0) {
            throw new IllegalArgumentException("Gap between days a team plays must be greater than or equal to 0");
        } else if (opSchedCnt < 0) {
            throw new IllegalArgumentException("Number of schedules to output should at least be one or all (0)");
        }

        this.teams = teams;
        this.numTeams = teams.length;
        this.matchesBtwTeams = matchesBtwTeams;
        this.dayNumMatches = dayNumMatches;
        this.teamMatchesGap = teamMatchesGap;
        this.teamDaysGap = teamDaysGap;

        this.numMatches = (numTeams * (numTeams - 1) * matchesBtwTeams) / 2;
        this.numDays = calculateNumDays(numMatches, dayNumMatches);

        this.playedEachOtherCount = new int[numTeams][numTeams];
        this.lastPlayedDay = new int[numTeams];
        this.lastPlayedMatch = new int[numTeams];
        this.lastPlayedAgainstTeam = new String[numTeams];
        this.teamMatchesPlayed = new int[numTeams];

        this.schedule = new ArrayList<>();
        this.res = new ArrayList<>();

        intialiseState();
        return backtrackSD(0, 0, 0, alwEmtDay, alwTeamPairRecur, opSchedCnt);
    }

    /**
     * Generates a standard schedule for a sports event using backtracking.
     *
     * @param curDay              The current day of the schedule.
     * @param curMatch            The current match of the schedule.
     * @param playedToday         The number of matches played on the current day.
     * @param alwEmtDay           Flag indicating whether an empty day is allowed in the schedule.
     * @param alwTeamPairRecur  Flag indicating whether a team can play against the same opponent consecutively.
     * @param opSchedCnt          The number of schedules/permutations to output.
     * @return                    A list of lists representing the generated schedule.
     */
    List<List<String[]>> backtrackSD(
            int curDay,
            int curMatch,
            int playedToday,
            boolean alwEmtDay,
            boolean alwTeamPairRecur,
            int opSchedCnt
    ) {
        // if required number of schedules are generated, return (0 is considered as all possible schedules)
        if (opSchedCnt > 0 && resCount >= opSchedCnt) {
            return res;
        }

        // if all teams have played their matches, the schedule is completed, add it to the result
        if (isScheduleCompleted((numTeams - 1) * matchesBtwTeams)) {
            resCount++;
            res.add(new ArrayList<>(schedule));
            return res;
        }

        // deadlocked flag indicates whether no team can play on the current match and day due to constraints
        boolean deadlocked = true, deadlockedByTeamPairRecur = false;
        for (int team1 = 0; team1 < numTeams; team1++) { // permutations of matches in a schedule
            long curTeam1DaysGap = curDay - lastPlayedDay[team1] - 1;
            long curTeam1MatchesGap = curMatch - lastPlayedMatch[team1] - 1;

            if (    teamMatchesPlayed[team1] == (numTeams - 1) * matchesBtwTeams || // all matches played by a team
                    curTeam1DaysGap < teamDaysGap ||
                    curTeam1MatchesGap < teamMatchesGap
            ) {
                continue;
            }

            for (int team2 = team1 + 1; team2 < numTeams; team2++) { // combinations of team pairs within an individual match
                long curTeam2DaysGap = curDay - lastPlayedDay[team2] - 1;
                long curTeam2MatchesGap = curMatch - lastPlayedMatch[team2] - 1;

                if (
                        team1 == team2 ||
                                teamMatchesPlayed[team2] == (numTeams - 1) * matchesBtwTeams ||
                                playedEachOtherCount[team1][team2] == matchesBtwTeams ||
                                curTeam2DaysGap < teamDaysGap ||
                                curTeam2MatchesGap < teamMatchesGap
                ) {
                    continue;
                }

                // if team pair recurrence is not allowed, check if the teams have played against each other in the last match
                if (!alwTeamPairRecur) {
                    // if team1 is empty, it hasn't played any match yet and if team2 is empty, the latter check will be false anyway
                    if (!lastPlayedAgainstTeam[team1].equals("") && lastPlayedAgainstTeam[team1].equals(teams[team2])) {
                        deadlockedByTeamPairRecur = true;
                        continue;
                    }
                }

                // team1 and team2 can play each other; save the state of the schedule to recover state later
                deadlocked = false;
                int temp1lastPlayedDay = lastPlayedDay[team1];
                int tempt2lastPlayedDay = lastPlayedDay[team2];
                int temp1lastPlayedMatch = lastPlayedMatch[team1];
                int tempt2lastPlayedMatch = lastPlayedMatch[team2];
                String temp1lastPlayedAgainstTeam = lastPlayedAgainstTeam[team1], tempt2lastPlayedAgainstTeam = lastPlayedAgainstTeam[team2];

                // update the state of the schedule; prepare for the next match through backtracking
                playedEachOtherCount[team1][team2]++;
                playedEachOtherCount[team2][team1]++;
                lastPlayedDay[team1] = curDay;
                lastPlayedDay[team2] = curDay;
                lastPlayedMatch[team1] = curMatch;
                lastPlayedMatch[team2] = curMatch;
                lastPlayedAgainstTeam[team1] = teams[team2];
                lastPlayedAgainstTeam[team2] = teams[team1];
                teamMatchesPlayed[team1]++;
                teamMatchesPlayed[team2]++;
                schedule.add(new String[] {String.valueOf(curDay+1), String.valueOf(curMatch+1), teams[team1], teams[team2] });
                playedToday++;

                // backtrack
                int nextDay, newPlayedToday;
                if (playedToday == dayNumMatches[curDay % dayNumMatches.length]) {
                    nextDay = curDay + 1;
                    newPlayedToday = 0;
                } else {
                    nextDay = curDay;
                    newPlayedToday = playedToday;
                }
                backtrackSD(nextDay, curMatch+1, newPlayedToday, alwEmtDay, alwTeamPairRecur, opSchedCnt);

                // return to the original state of the schedule to try other paths
                playedToday--;
                schedule.remove(schedule.size() - 1);
                teamMatchesPlayed[team1]--;
                teamMatchesPlayed[team2]--;
                lastPlayedAgainstTeam[team1] = temp1lastPlayedAgainstTeam;
                lastPlayedAgainstTeam[team2] = tempt2lastPlayedAgainstTeam;
                lastPlayedMatch[team1] = temp1lastPlayedMatch;
                lastPlayedMatch[team2] = tempt2lastPlayedMatch;
                lastPlayedDay[team1] = temp1lastPlayedDay;
                lastPlayedDay[team2] = tempt2lastPlayedDay;
                playedEachOtherCount[team1][team2]--;
                playedEachOtherCount[team2][team1]--;
            }
        }

        /*
         * if only team pair recur available -> deadlockedByTeamPairRecur = true
         * if deadlock and empty day is not allowed -> return (no backtracking)
         * if deadlock and empty day is allowed -> if deadLockedByTeamPairRecur, return (possibly misses out on some schedules)
         *                                         else backtrack
         */
        // if empty day is not allowed and schedule is deadlocked, then we don't backtrack, else we do
        if (alwEmtDay && deadlocked) {
            // if team pair recurrence is allowed, deadlockedByTeamPairRecur will be false, and we will backtrack, else we won't
            // if empty day is allowed and team pair has recurred, we don't backtrack. else might lead to deadlock
            // !we miss out on some schedules if we don't backtrack, but we avoid deadlocks (do we want schedules with many empty days?)
            if (deadlockedByTeamPairRecur)
                return res;

            playedToday++;
            schedule.add(new String[] {String.valueOf(curDay+1), String.valueOf(curMatch+1), "NA", "NA" });
            // DRY this
            int nextDay, newPlayedToday;
            if (playedToday == dayNumMatches[curDay % dayNumMatches.length]) {
                nextDay = curDay + 1;
                newPlayedToday = 0;
            } else {
                nextDay = curDay;
                newPlayedToday = playedToday;
            }
            backtrackSD(nextDay, curMatch+1, newPlayedToday, alwEmtDay, alwTeamPairRecur, opSchedCnt);
            schedule.remove(schedule.size() - 1);
        }

        return res;
    }

    long calculateNumDays(long numMatches, long[] dayNumMatches) {
        long res = 0;
        long curDayMatchNum = 0;
        while (numMatches > 0) {
            numMatches -= dayNumMatches[(int) (curDayMatchNum++) % dayNumMatches.length];
            res++;
        }
        return res;
    }

    boolean isScheduleCompleted(long teamNumMatchesToPlay) {
        for (int teamPlayed : teamMatchesPlayed) {
            if (teamPlayed != teamNumMatchesToPlay)
                return false;
        }
        return true;
    }
}
