import java.util.*;

class SportsScheduler {
    static String[] iplTeams2024 = new String[] {
            "Chennai Super Kings",
            "Delhi Capitals",
            "Gujarat Titans",
            "Kolkata Knight Riders",
            "Lucknow Super Giants",
            "Mumbai Indians",
            "Punjab Kings",
            "Rajasthan Royals",
            "Royal Challengers Bengaluru",
            "Sunrisers Hyderabad"
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

    public SportsScheduler(String[] teams, long matchesBtwTeams, long[] dayNumMatches, long teamMatchesGap, long teamDaysGap) {
        if (teams.length < 2) {
            throw new IllegalArgumentException("teams must have at least 2 teams");
        } else if (matchesBtwTeams < 1) {
            throw new IllegalArgumentException("matchesBtwTeams must be greater than 0");
        } else if (dayNumMatches.length < 1) {
            throw new IllegalArgumentException("dayNumMatches must have at least 1 element");
        } else if (teamMatchesGap < 0) {
            throw new IllegalArgumentException("teamMatchesGap must be greater than or equal to 0");
        } else if (teamDaysGap < 0) {
            throw new IllegalArgumentException("teamDaysGap must be greater than or equal to 0");
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
    }

    void intialiseState() {
        Arrays.fill(lastPlayedDay, -10000);
        Arrays.fill(lastPlayedMatch, -10000);
        Arrays.fill(lastPlayedAgainstTeam, "");
    }

    public List<List<String[]>> standardSchedule(int curDay, int curMatch, int playedToday, boolean allowEmptyDay,boolean allowTeamPairRecur, int outputPermCount) {
        intialiseState();
        return backtrackSD(curDay, curMatch, playedToday, allowEmptyDay, allowTeamPairRecur, outputPermCount);
    }

    List<List<String[]>> backtrackSD(int curDay, int curMatch, int playedToday, boolean allowEmptyDay, boolean allowTeamPairRecur, int outputPermCount) {
        if (outputPermCount > 0 && resCount >= outputPermCount) {
            return res;
        }

        if (isScheduleCompleted((numTeams - 1) * matchesBtwTeams)) {
            resCount++;
            res.add(new ArrayList<>(schedule));
            //System.out.println(resCount + " schedules added");
            return res;
        }

        boolean deadlocked = true, deadlockedDueToTeamPairRecur = false;
        for (int team1 = 0; team1 < numTeams; team1++) {
            if (    teamMatchesPlayed[team1] == (numTeams - 1) * matchesBtwTeams ||
                    curDay - lastPlayedDay[team1] - 1 < teamDaysGap ||
                    curMatch - lastPlayedMatch[team1] - 1 < teamMatchesGap
            ) {
                continue;
            }

            for (int team2 = team1 + 1; team2 < numTeams; team2++) {
                if (    team1 == team2 ||
                        teamMatchesPlayed[team2] == (numTeams - 1) * matchesBtwTeams ||
                        playedEachOtherCount[team1][team2] == matchesBtwTeams ||
                        curDay - lastPlayedDay[team2] - 1 < teamDaysGap ||
                        curMatch - lastPlayedMatch[team2] - 1 < teamMatchesGap
                ) {
                        continue;
                }
                if (!allowTeamPairRecur) {
                    if (!lastPlayedAgainstTeam[team1].equals("") && lastPlayedAgainstTeam[team1].equals(teams[team2])) {
                        deadlockedDueToTeamPairRecur = true;
                        continue;
                    }
                }

                deadlocked = false;
                int temp1lastPlayedDay = lastPlayedDay[team1];
                // team1 and team2 can play
                // save the state
                int tempt2lastPlayedDay = lastPlayedDay[team2];
                int temp1lastPlayedMatch = lastPlayedMatch[team1];
                int tempt2lastPlayedMatch = lastPlayedMatch[team2];
                String temp1lastPlayedAgainstTeam = lastPlayedAgainstTeam[team1], tempt2lastPlayedAgainstTeam = lastPlayedAgainstTeam[team2];

                // update the state
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
                schedule.add(new String[] { teams[team1], teams[team2] });
                playedToday++;

                // backtrack
                if (playedToday == dayNumMatches[curDay % dayNumMatches.length]) {
                    backtrackSD(curDay + 1, curMatch + 1, 0, allowEmptyDay, allowTeamPairRecur, outputPermCount);
                } else {
                    backtrackSD(curDay, curMatch + 1, playedToday + 1, allowEmptyDay, allowTeamPairRecur,
                            outputPermCount);
                }

                // return to the original state
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

        if (allowEmptyDay && deadlocked) {
            if (deadlockedDueToTeamPairRecur)
                return res;

            schedule.add(new String[] { "NA", "NA" });
            if (playedToday + 1 == dayNumMatches[curDay % dayNumMatches.length]) {
                backtrackSD(curDay + 1, curMatch + 1, 0, allowEmptyDay, allowTeamPairRecur, outputPermCount);
            } else {
                backtrackSD(curDay, curMatch + 1, playedToday + 1, allowEmptyDay, allowTeamPairRecur, outputPermCount);
            }
            schedule.remove(schedule.size() - 1);
        }

        return res;
    }

    long calculateNumDays(long numMatches, long[] dayNumMatches) {
        long res = 0;
        long team1 = 0;
        while (numMatches > 0) {
            numMatches -= dayNumMatches[(int) (team1++) % dayNumMatches.length];
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

class Test {
    public static void main(String[] args) {
        SportsScheduler scheduler = new SportsScheduler(SportsScheduler.iplTeams2024, 2, new long[] { 1 }, 1, 1);
        List<List<String[]>> schedules = scheduler.standardSchedule(0, 0, 0, false, false, 1);
        System.out.println("Number of schedules: " + schedules.size());

        for (int i = 0; i < schedules.size(); i++) {
            System.out.println("Schedule " + (i + 1) + ":");
            List<String[]> schedule = schedules.get(i);

            int count = 1;
            int dayNum = 1, matchNum = 1;
            for (String[] match : schedule) {
                System.out.println("  " + count + ": Day " + dayNum + ", Match " + matchNum + ": " + match[0] + " vs " + match[1]);

                if (matchNum == scheduler.dayNumMatches[(dayNum - 1) % scheduler.dayNumMatches.length]) {
                    dayNum++;
                    matchNum = 1;
                } else {
                    matchNum++;
                }
                count++;
            }
            System.out.println();
        }
    }
}