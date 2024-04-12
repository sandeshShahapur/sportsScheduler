## A backtracking recursive algorithm to schedule a sports tournament.

**Current parameters tweakable:**
- number of teams
- number of matches each team plays against each other
- minimum days gap (>= 0) between consecutive matches of a team
- minimum matches gap (>= 0) between consecutive matches of a team
- number of matches on each day (broken atm)
- whether schedule can allow days without any matches (we might want this to obtain at least one schedule, cause deadlock)
- whether schedule can allow consecutive matches involving the same two teams (we might want this to obtain at least one schedule, cause deadlock)
- number of permutations to output

> [!Important]
> Uncertainty over accuracy of generating all possible permutations but each schedule passes parameter requirements and no duplicate permutation was generated **to the extent i tested.**

## Example
IPL with 8 teams playing two matches against each other with one match played daily. No team can play on two consecutive days or matches. No two consecutive matches can have idetitcal team matchups and a day cannot be empty. Returns single schedule.
![image](https://github.com/sandeshShahapur/sportsScheduler/assets/110241292/2080f25b-ade6-4f2a-a56d-5b8efac49ba8)
