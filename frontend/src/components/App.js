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
      const response = await fetch('http://localhost:8080/generateStandardSchedule', {
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

        <div className="delay-note">
          Please note: The server may experience delays due to inactivity.
          If you encounter issues, please refresh the page or try again later.
        </div>
      </main>
    </div>
  );
};

export default App;
