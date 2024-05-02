import React, { useState } from "react";
import "../styles/ScheduleTable.css";

const ScheduleTable = ({ schedules }) => {
  const [selectedScheduleIndex, setSelectedScheduleIndex] = useState(0);

  if (typeof schedules === "undefined") {
    return null;
  }

  if (schedules.length === 0) {
    console.log(schedules);
    return (
      <div className="schedule-table-container">
        <div className="no-schedules-message">
          No schedules available for the given parameters. Please alter the
          parameters. Suggestion: Enabling empty days or team pair recurs might
          be sufficient for generating at least one schedule or you could also
          increase the number of teams.
        </div>
      </div>
    );
  }

  const handleScheduleChange = (index) => {
    setSelectedScheduleIndex(index);
  };

  let selectedSchedule = schedules[selectedScheduleIndex];

  return (
    <div className="schedule-table-container">
      <div className="schedule-dropdown">
        {/* Dropdown to select the schedule */}
        <select
          value={selectedScheduleIndex}
          onChange={(e) => handleScheduleChange(e.target.value)}
        >
          {schedules.map((schedule, index) => (
            <option key={index} value={index}>
              Schedule {index + 1}
            </option>
          ))}
        </select>
      </div>
      <br />
      {/* Display the selected schedule in a table */}
      <table className="schedule-table">
        <thead>
          <tr>
            <th>Day</th>
            <th>Match</th>
            <th>Team 1</th>
            <th>Team 2</th>
          </tr>
        </thead>
        <tbody>
          {selectedSchedule.map((match, index) => (
            <tr key={index}>
              <td>{match[0]}</td>
              <td>{match[1]}</td>
              <td>{match[2]}</td>
              <td>{match[3]}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ScheduleTable;