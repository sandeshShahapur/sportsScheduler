# A backtracking recursive algorithm to schedule a sports tournament.

**Current parameters tweakable:**
- number of teams
- number of matches each team plays against each other
- minimum days gap (>= 0) between consecutive matches of a team
- minimum matches gap (>= 0) between consecutive matches of a team
- number of matches on each day (broken atm)
- whether schedule can allow days without any matches (we might want this to obtain at least one schedule, cause deadlock)
- whether schedule can allow consecutive matches involving the same two teams (we might want this to obtain at least one schedule, cause deadlock)
- number of permutations to output

Produces [permutations](https://en.wikipedia.org/wiki/Permutation) of schedules relative to each match while accounting for [combinations](https://en.wikipedia.org/wiki/Combination) of team pair ups within an individual match.

> [!Important]
> Uncertainty over accuracy of generating all possible permutations but each schedule passes parameter requirements and no duplicate permutation was generated **to the extent i tested.**



## Example
IPL with 7 teams playing:
- two matches against each other 
- one match played on the weekdays and two matches played on the weekends
- no team can play on two consecutive days or matches
- no two consecutive matches can have idetitcal team matchups
- a match can be empty 
- returns single schedule.
![image](https://github.com/sandeshShahapur/sportsScheduler/assets/110241292/1397c16a-0aa6-4d2f-8213-e0017b1f79a0)

