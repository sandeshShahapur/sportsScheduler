import React, { useState, useEffect } from "react";
import Form from "./Form";
import ScheduleTable from "./ScheduleTable";

const App = () => {
  const [isMounted, setIsMounted] = useState(false);
  const [showDesktopModeSuggestion, setShowDesktopModeSuggestion] = useState(false);

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [schedules, setSchedules] = useState(null);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth < 768) {
        setShowDesktopModeSuggestion(true);
      } else {
        setShowDesktopModeSuggestion(false);
      }
    };

    handleResize(); // Check viewport width on component mount
    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);


  const handleFetchSchedules = async (formData) => {
    setIsMounted(true);
    let fetchCompleted = false;
    const loadingTimeout = setTimeout(() => {
      if (!fetchCompleted) {
        setIsLoading(true);
      }
    }, 2 * 1000); // 2 seconds
    const failTimeout = setTimeout(() => {
      if (!fetchCompleted) {
        setError("Process failure likely due to heavy computation.<br> Suggestions: Try with fewer teams or matches between teams.");
        setIsLoading(false);
      }
    }, 70 * 1000); // 70 seconds
    setError("");

    try {
      setSchedules(await fetchSchedules(formData));
    } catch (error) {
      setError(error.message);
    } finally {
      clearTimeout(loadingTimeout);
      clearTimeout(failTimeout);
      fetchCompleted = true;
      setIsLoading(false);
    }
  };

  const fetchSchedules = async (formData) => {
    const response = await fetch(
      "https://sportsscheduler-vxsj.onrender.com/generateStandardSchedule", // "http://localhost:8080/generateStandardSchedule"
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      }
    );

    if (!response.ok) {
      if (response.status === 500) {
        throw new Error(
          "Failed to generate schedules! <br /> Likely invalid parameters, please try again with different parameters."
        );
      } else {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
    }

    return response.json();
  }


  return (
    <div>
      <header className="container-fluid mb-5 p-5 bg-dark text-white">
        <h1 className="display-4 fw-bold">Sports Tournament Scheduler</h1>
        <p className="ps-md-2">
          A tool to generate tournament schedules for a given set of teams and parameters.
        </p>
      </header>

      <main className="container mb-5">
        <div className={`row align-content-center justify-content-center ${isMounted ? " justify-content-xl-between" : ""}`}>
          <Form handleFetchSchedules={handleFetchSchedules} />
          {showDesktopModeSuggestion && (
            <div className="alert alert-info mt-3" role="alert">
              For a better experience, please switch to desktop mode.
            </div>
          )}

          {isMounted && (
            <div className="col-12 col-xl-6">
              {error ? (
                  <div className="alert alert-danger" role="alert">{error}</div>
              ) : isLoading ? (
                <div>
                  <div className="spinner-border float-start me-3 mb-5" role="status"></div> Loading... <br />
                  If it is your first submit, it may take up to a minute.
                </div>
              ) : schedules ? (
                  <ScheduleTable schedules={schedules} />
              ) : null}
            </div>
          )}
        </div>
      </main>

      {error && (
        <div className="mt-3 border-top border-3 pt-3 mb-2 text-center text-danger">
          If you are using any adblocker (e.g., Brave Shield of Brave browser), consider disabling it if you faced an error when submitting.
        </div>
      )}
    </div>
  );
};

export default App;

