import React, { useState } from 'react';
import Form from './Form';
import ScheduleTable from './ScheduleTable';
import '../styles/App.css'; // Import CSS file for styling

const App = () => {
  const [schedules, setSchedules] = useState();
  const [displayEmptySchedulesError, setDisplayEmptySchedulesError] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleGenerateSchedule = async (formData) => {
    setDisplayEmptySchedulesError(true);
    setIsLoading(true);
    setError('');

    try {
      const response = await fetch('https://sportsscheduler-vxsj.onrender.com/generateStandardSchedule', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        if (response.status === 500)
          throw new Error('Error generating schedules! Please try again with different parameters.');
        else
          throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const data = await response.json();
      setSchedules(data);
    } catch (error) {
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="app-container">
      <header>
        <h1>Sports Schedule Generator</h1>
      </header>

      <main>
        <div className="form-container">
          {/* Form component to capture user input */}
          <Form onFormSubmit={handleGenerateSchedule} />
        </div>

        {
        displayEmptySchedulesError &&
        <div className="schedule-container">
          {/* Display loading indicator while fetching data */}
          {isLoading && <div>Loading...</div>}
          {/* Display error message if there is any */}
          {error && <div className='error-message'>{error}</div>}

          {/* Display ScheduleTable component with schedules */}
          {!error && <ScheduleTable schedules={schedules} />}
        </div>
        }
      </main>

      <div className='note-container'>
        <div className="delay-note">
            Please note: The server may experience delays due to inactivity.
            Wait upto a minute for the schedule to be generated.
        </div>
        <div className="adblocker-note">
          If you are using any adblocker (eg: Brave Shield of Brave browser),
          consider disabling it for proper functionality.
        </div>
      </div>
    </div>
  );
};

export default App;
