# A WebApp to schedule sports tournaments

[Check it out!](https://sandeshshahapur.github.io/sportsScheduler/)

##  Designed and implemented a backtracking recursive algorithm 

### Current parameters tweakable:
- number of teams
- number of matches each team plays against each other
- number of matches played on each day
- minimum days gap (>= 0) between consecutive matches of a team
- minimum matches gap (>= 0) between consecutive matches of a team
- starting date of tournament
- whether schedule can allow days without any matches (we might want this to obtain at least one schedule, cause deadlock)
- whether schedule can allow consecutive matches involving the same two teams (we might want this to obtain at least one schedule, cause deadlock)
- number of permutations to output

Produces [permutations](https://en.wikipedia.org/wiki/Permutation) of schedules relative to each match while accounting for [combinations](https://en.wikipedia.org/wiki/Combination) of team pair ups within an individual match.

***

## Example
![Screenshot of Website](https://github.com/sandeshShahapur/sportsScheduler/assets/110241292/68cdc6bb-da96-4036-a85a-2d426d714cbb)
