import { useState } from "react";
import "../css/logSessionCard.css";

function LogSessionCard({ onClose, onSave }) {
    const [date, setDate] = useState("");
    const [hours, setHours] = useState("");
    const [productivityScore, setProductivity] = useState(5);
    const [comment, setComment] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        const newSession = {
            date,
            hours,
            productivityScore,
            comment,
        };

        onSave(newSession);
    };

    return (
        <div className="logsession-overlay">
            <div className="logsession-card">
                <h3>Log Study Session</h3>

                <form onSubmit={handleSubmit}>
                    <label>Date</label>
                    <input
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />

                    <label>Hours</label>
                    <input
                        type="number"
                        step="0.5"
                        min="0"
                        value={hours}
                        onChange={(e) => setHours(e.target.value)}
                        required
                    />

                    <label>Productivity (1–10)</label>
                    <input
                        type="range"
                        min="1"
                        max="10"
                        value={productivityScore}
                        onChange={(e) => setProductivity(e.target.value)}
                    />
                    <span>{productivityScore}/10</span>

                    <label>Comment</label>
                    <textarea
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        placeholder="How did the session go?"
                    />

                    <div className="button-group">
                        <button type="button" className="cancel" onClick={onClose}>
                            Cancel
                        </button>
                        <button type="submit" className="save">
                            Save Session
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default LogSessionCard;