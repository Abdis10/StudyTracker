import { Plus } from "lucide-react";
import "../css/studySessions.css";

function StudySessions() {
    return (
        <div className="sessions-page-container">
            <div className="study-sessions">
                <div className="sessions-header">
                    <h2>Study Sessions</h2>
                    <button className="log-btn">
                        <Plus size={18} />
                        Log Session
                    </button>
                </div>

                <div className="session-display">
                    <table>
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Hours</th>
                            <th>Productivity</th>
                            <th>Comments</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>May 8, 2024</td>
                            <td>2.5 h</td>
                            <td><span className="productivity high">8/10</span></td>
                            <td>Felt focused and productive</td>
                        </tr>
                        <tr>
                            <td>May 9, 2024</td>
                            <td>1.5 h</td>
                            <td><span className="productivity medium">6/10</span></td>
                            <td>Distracted first hour</td>
                        </tr>
                        <tr>
                            <td>May 10, 2024</td>
                            <td>3 h</td>
                            <td><span className="productivity high">9/10</span></td>
                            <td>Deep work session</td>
                        </tr>
                        <tr>
                            <td>May 11, 2024</td>
                            <td>2 h</td>
                            <td><span className="productivity low">4/10</span></td>
                            <td>Tired but consistent</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="session-diagram">
                {/* Chart later */}
            </div>
        </div>
    );
}

export default StudySessions;
