import {useEffect, useState} from "react";
import "../css/logSessionCard.css";
import { Plus } from 'lucide-react';

function LogSessionCard({ onClose, onSave, initialData } ) {
    const [date, setDate] = useState("");
    const [hours, setHours] = useState("");
    const [productivityScore, setProductivity] = useState(5);
    const [comment, setComment] = useState("");
    const [subject, setSubject] = useState("");
    const [subjects, setSubjects] = useState([]);
    const [mode, setMode] = useState("select"); // "select" | "create"
    const [newSubject, setNewSubject] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        const newSession = {
            date,
            hours,
            productivityScore,
            comment,
            subjectId: subject
        };

        onSave(newSession);
    };

    useEffect(() => {
        if (initialData) {
            setDate(initialData.date);
            setHours(initialData.hours);
            setProductivity(initialData.productivityScore);
            setComment(initialData.comment);
        }
    }, [initialData]);

    const handleSubjectChange = (value) => {
        if (value === "add-new") {
            setMode("create");
        } else {
            setSubject(value);
        }
    };

    const handleAddSubject = () => {
        if (!newSubject.trim()) return;

        const newObj = {
            id: Date.now(), // midlertidig
            name: newSubject
        };

        setSubjects((prev) => [...prev, newObj]);

        setSubject(newObj.id);
        setNewSubject("");
        setMode("select");
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

                    <label>Subject</label>
                    {mode === "select" ? (
                        <select
                            value={subject}
                            onChange={(e) => handleSubjectChange(e.target.value)}
                        >
                            <option value="">Select subject</option>

                            {subjects.map((s) => (
                                <option key={s.id} value={s.id}>
                                    {s.name}
                                </option>
                            ))}

                            <option value="add-new">+ Add new subject</option>
                        </select>
                    ) : (
                        <div className="new-subject-inline">
                            <input
                                type="text"
                                placeholder="New subject..."
                                value={newSubject}
                                onChange={(e) => setNewSubject(e.target.value)}
                            />
                            <button type="button" onClick={handleAddSubject}>Save</button>
                            <button
                                type="button"
                                onClick={() => {
                                    setMode("select");
                                    setNewSubject("");
                                }}
                            >
                                Cancel
                            </button>                        </div>
                    )}

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