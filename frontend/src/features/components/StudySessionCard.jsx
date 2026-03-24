import "../css/studySessionCard.css"

function StudySessionCard( {studySummaryData} ) {
    const util = (data) => {
        let num = data;
        let hours = Math.floor(num);
        let minutes = (Math.ceil(num) - num)* 60;
        return hours + "h" + " " + minutes + "m";
    }

    return (
        <div className="study-session-card">
            <div className="card-body">
                <p className="label">Today's Study Time</p>
                {studySummaryData.todayStudyTime ? (<h1 className="time"> {util(studySummaryData.todayStudyTime)} </h1>) : 0 + "h"}
                <hr />

                <div className="summary">
                    <p>
                        This Week: {studySummaryData.weekStudyTime ?  (<span className="highlight"> {util(studySummaryData.weekStudyTime)} </span>) : 0 + "h"}
                    </p>
                    <p>
                        This Month: {studySummaryData.monthStudyTime ? (<span className="highlight"> {util(studySummaryData.monthStudyTime)} </span>) : 0 + "h"}
                    </p>
                </div>
            </div>
        </div>
    );
}

export default StudySessionCard;
