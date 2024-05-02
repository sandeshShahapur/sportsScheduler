/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import '../styles/Form.css';

const Form = ({ onFormSubmit }) => {
  const [teams, setTeams] = useState('');
  const [matchesBtwTeams, setMatchesBtwTeams] = useState('');
  const [dayNumMatches, setDayNumMatches] = useState('');
  const [teamMatchesGap, setTeamMatchesGap] = useState('');
  const [teamDaysGap, setTeamDaysGap] = useState('');
  const [alwEmtDay, setAlwEmtDay] = useState(false);
  const [alwTeamPairRecur, setAlwTeamPairRecur] = useState(false);
  const [opSchedCnt, setOpSchedCnt] = useState('');
  const [error, setError] = useState('');
  const [isValid, setIsValid] = useState(false);

  const fillWithExampleValues = () => {
    setTeams("Sharks, Void, Titans, Nomads");
    setMatchesBtwTeams("2");
    setDayNumMatches("1, 2");
    setTeamMatchesGap("1");
    setTeamDaysGap("1");
    setAlwEmtDay(true);
    setAlwTeamPairRecur(true);
    setOpSchedCnt("2");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Perform form validation

    // Form validation passed, construct JSON object with form data
    const formData = {
      teams: teams.split(',').filter(Boolean).map(str => str.trim()),
      matchesBtwTeams: parseInt(matchesBtwTeams),
      dayNumMatches: dayNumMatches.split(',').map(Number),
      teamMatchesGap: parseInt(teamMatchesGap),
      teamDaysGap: parseInt(teamDaysGap),
      alwEmtDay,
      alwTeamPairRecur,
      opSchedCnt: parseInt(opSchedCnt),
    };

    // Call the callback function to generate schedules
    await onFormSubmit(formData);
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        {/* Input fields for form parameters */}
        <label htmlFor="teams">Teams:</label>
        <input
          type="text"
          id="teams"
          placeholder='Comma separated'
          value={teams}
          onChange={(e) => {
            setTeams(e.target.value);
          }}
          required
        />

        <label htmlFor="matchesBtwTeams">Matches Between Teams:</label>
        <input
          type="number"
          id="matchesBtwTeams"
          value={matchesBtwTeams}
          onChange={(e) => {
            setMatchesBtwTeams(e.target.value);
          }}
          min="1"
          required
        />

        <label htmlFor="dayNumMatches">Day Number Matches:</label>
        <input
          type="text"
          id="dayNumMatches"
          placeholder='Comma separated. Eg: 1, 2, 3 (it will loop)'
          value={dayNumMatches}
          onChange={(e) => {
            setDayNumMatches(e.target.value);
          }}
          required
        />

        <label htmlFor="teamMatchesGap">Team Matches Gap:</label>
        <input
          type="number"
          id="teamMatchesGap"
          placeholder='0 to allow no gap'
          value={teamMatchesGap}
          onChange={(e) => {
            setTeamMatchesGap(e.target.value);
          }}
          min="0"
          required
        />

        <label htmlFor="teamDaysGap">Team Days Gap:</label>
        <input
          type="number"
          id="teamDaysGap"
          placeholder='0 to allow no gap'
          value={teamDaysGap}
          onChange={(e) => {
            setTeamDaysGap(e.target.value);
          }}
          min="0"
          required
        />

        <label htmlFor="alwEmtDay">Allow Empty Day:</label>
        <input
          type="checkbox"
          id="alwEmtDay"
          checked={alwEmtDay}
          value={alwEmtDay}
          onChange={() => setAlwEmtDay(!alwEmtDay)}
        />

        <label htmlFor="alwTeamPairRecur">Allow Team Pair Recurrence:</label>
        <input
          type="checkbox"
          id="alwTeamPairRecur"
          checked={alwTeamPairRecur}
          value={alwTeamPairRecur}
          onChange={() => setAlwTeamPairRecur(!alwTeamPairRecur)}
        />

        <label htmlFor="opSchedCnt">Output Schedule Count:</label>
        <input
          type="number"
          id="opSchedCnt"
          placeholder='0 for all schedules'
          value={opSchedCnt}
          onChange={(e) => {
            setOpSchedCnt(e.target.value);
          }}
          min="0"
          required
        />
        <div className='button-container'>
          {/* Submit button */}
          <button type="submit">Submit</button>
          {/* Button to fill with example values */}
          <button type="button" onClick={fillWithExampleValues}>Fill with Example Values</button>
        </div>
      </form>

      {/* Display error message if there is any */}
      {error && <div>{error}</div>}
    </div>
  );
};

export default Form;