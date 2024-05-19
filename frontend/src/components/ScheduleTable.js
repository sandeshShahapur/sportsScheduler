import React, { useState } from "react";

const ScheduleTable = ({ schedules }) => {
  const [selectedScheduleIndex, setSelectedScheduleIndex] = useState(0);

  if (typeof schedules === "undefined") {
    throw new Error("Failed to load schedules");
  }

  if (schedules && schedules.length === 0) {
    return (
      <div className="alert alert-warning" role="alert">
        Could not generate any schedules for the given parameters. Please alter the parameters. <br/>
        <strong>Suggestion:</strong> Try enabling empty days and team pair recur, or you could also increase the number of teams.
      </div>

    );
  }

  // Shuffle the schedules array to display a random schedule by default
  for (let i = schedules.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [schedules[i], schedules[j]] = [schedules[j], schedules[i]];
  }

  let selectedSchedule = schedules[selectedScheduleIndex];

  return (
    <div className="p-4 border bor rounded-2 shadow">
      <div className="">
        <select
          className="form-select"
          value={selectedScheduleIndex}
          onChange={(e) => setSelectedScheduleIndex(e.target.value)}
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
      <div className="table-fit overflow-y-auto" style={{ maxHeight: "620px" }}>
        <table className="table table-hover pe-5 table-fit">
          <thead>
            <tr>
              <th>Match</th>
              <th>Day</th>
              <th>Date</th>
              <th>Weekday</th>
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
                <td>{match[4]}</td>
                <td>{match[5]}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ScheduleTable;
