import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

const Form = ({ handleFetchSchedules } ) => {
  const [form, setForm] = useState({
    teams: "",
    matchesBtwTeams: "",
    dayNumMatches: "",
    teamMatchesGap: "",
    teamDaysGap: "",
    curDate: new Date().toISOString().split("T")[0],
    alwEmtDay: false,
    alwTeamPairRecur: false,
    opSchedCnt: "",
  });

  const fillWithExampleValues = () => {
    setForm({
      teams: "Sharks, Thunder, Titans, Raptors",
      matchesBtwTeams: "2",
      dayNumMatches: "1, 1, 1, 1, 1, 2, 2",
      teamMatchesGap: "1",
      teamDaysGap: "1",
      curDate: new Date().toISOString().split("T")[0],
      alwEmtDay: true,
      alwTeamPairRecur: true,
      opSchedCnt: "2",
    });
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prevState) => ({
      ...prevState,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const htmlForm = e.target;

    let isSemanticallyValid = true;
    const dayNumMatches = form.dayNumMatches.split(",").map(Number);
    const teams = shuffleTeams(form.teams.split(",").map((team) => team.trim()).filter(Boolean));

    if (dayNumMatches.some(isNaN) || dayNumMatches.some((num) => num < 1)){
      isSemanticallyValid = false;
      htmlForm.dayNumMatches.setCustomValidity("Invalid number");
    } else {
      htmlForm.dayNumMatches.setCustomValidity("");
    }
    if (teams.length < 3) {
      isSemanticallyValid = false;
      htmlForm.teams.setCustomValidity("Please provide at least 3 teams");
    } else {
      htmlForm.teams.setCustomValidity("");
    }

    htmlForm.classList.add("was-validated");
    if (!htmlForm.checkValidity() || !isSemanticallyValid)
      return;

    const formData = {
      "teams": teams,
      "matchesBtwTeams": Number(form.matchesBtwTeams),
      "dayNumMatches": dayNumMatches,
      "teamMatchesGap": Number(form.teamMatchesGap),
      "teamDaysGap": Number(form.teamDaysGap),
      "curDate": form.curDate,
      "alwEmtDay": form.alwEmtDay,
      "alwTeamPairRecur": form.alwTeamPairRecur,
      "opSchedCnt": Number(form.opSchedCnt),
    };
    handleFetchSchedules(formData);
  };

  return (
    <div className="col-10 col-md-8 col-lg-6 col-xl-5 border rounded-2 p-4 shadow mb-5">
      <form onSubmit={handleSubmit} className="needs-validation" noValidate>
        <div className="">
          <label htmlFor="teams" className="form-label mb-1">
            Teams
          </label>
          <input
            type="text"
            className="form-control"
            id="teams"
            name="teams"
            placeholder="comma seperated"
            value={form.teams}
            onChange={handleInputChange}
            required
          />
          <div className="invalid-feedback">Please provide minimum 3 teams.</div>
        </div>

        <div className="mb-3">
          <label htmlFor="matchesBtwTeams" className="form-label mb-1">
            {" "}
            Matches Between Teams
          </label>
          <input
            type="number"
            className="form-control"
            id="matchesBtwTeams"
            name="matchesBtwTeams"
            placeholder="minimum 1"
            value={form.matchesBtwTeams}
            onChange={handleInputChange}
            min={1}
            required
          />
          {!form.matchesBtwTeams ? (
            <div className="invalid-feedback">
              Please provide the number of matches between teams.
            </div>
          ) : (
            <div className="invalid-feedback">
              Please provide a valid number.
            </div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="dayNumMatches" className="form-label mb-1">
            Matches Count on Each Day
          </label>
          <input
            type="text"
            className="form-control"
            id="dayNumMatches"
            name="dayNumMatches"
            placeholder="comma seperated (it will loop)"
            value={form.dayNumMatches}
            onChange={handleInputChange}
            required
          />
          {!form.dayNumMatches ? (
            <div className="invalid-feedback">
              Please provide the matches count on each day.
            </div>
          ) : (
            <div className="invalid-feedback">
              Please provide valid counts.
            </div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="teamMatchesGap" className="form-label mb-1">
            Matches Gap Count for a Team
          </label>
          <input
            type="number"
            className="form-control"
            id="teamMatchesGap"
            name="teamMatchesGap"
            placeholder="0 for allowing no gap"
            value={form.teamMatchesGap}
            onChange={handleInputChange}
            min={0}
            required
          />
          {!form.teamMatchesGap ? (
            <div className="invalid-feedback">
              Please provide the team matches gap.
            </div>
          ) : (
            <div className="invalid-feedback">
              Please provide a valid number.
            </div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="teamDaysGap" className="form-label mb-1">
            Days Gap Count for a Team
          </label>
          <input
            type="number"
            className="form-control"
            id="teamDaysGap"
            name="teamDaysGap"
            placeholder="0 for allowing no gap"
            value={form.teamDaysGap}
            onChange={handleInputChange}
            min={0}
            required
          />
          {!form.teamDaysGap ? (
            <div className="invalid-feedback">
              Please provide the team days gap.
            </div>
          ) : (
            <div className="invalid-feedback">
              Please provide a valid number.
            </div>
          )}
        </div>

        <div className="mb-4">
          <label htmlFor="curDate" className="form-label mb-1">
            Starting Date
          </label>
          <input
            type="date"
            className="form-control"
            id="curDate"
            name="curDate"
            value={form.curDate}
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">Invalid starting date.</div>
        </div>

        <div className="mb-1 form-check">
          <label className="form-check-label" htmlFor="alwEmtDay">
            Allow Days with no Matches Played
          </label>
          <input
            type="checkbox"
            className="form-check-input"
            id="alwEmtDay"
            name="alwEmtDay"
            checked={form.alwEmtDay}
            onChange={handleInputChange}
          />
        </div>

        <div className="mb-4 form-check">
          <label className="form-check-label" htmlFor="alwTeamPairRecur">
            Allow Consecutive Matches with same Team Matchup
          </label>
          <input
            type="checkbox"
            className="form-check-input"
            id="alwTeamPairRecur"
            name="alwTeamPairRecur"
            checked={form.alwTeamPairRecur}
            onChange={handleInputChange}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="opSchedCnt" className="form-label mb-1">
            Output Schedule Count
          </label>
          <input
            type="number"
            className="form-control"
            id="opSchedCnt"
            name="opSchedCnt"
            placeholder="0 to output all schedules"
            value={form.opSchedCnt}
            onChange={handleInputChange}
            min={0}
            required
          />
          {!form.opSchedCnt ? (
            <div className="invalid-feedback">
              Please provide the optional schedule count.
            </div>
          ) : (
            <div className="invalid-feedback">
              Please provide a valid number.
            </div>
          )}
        </div>

        <div className="row justify-content-between column-gap-1">
          <button type="submit" className="col-auto col-sm-5 btn btn-primary mr-2">
            Submit
          </button>
          <button
            type="button"
            className="col-auto btn btn-secondary"
            onClick={fillWithExampleValues}
          >
            Fill Example Parameters
          </button>
        </div>
      </form>
    </div>
  );
};

const shuffleTeams = (teams) => {
  for (let i = teams.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [teams[i], teams[j]] = [teams[j], teams[i]];
  }
  return teams;
};

export default Form;
