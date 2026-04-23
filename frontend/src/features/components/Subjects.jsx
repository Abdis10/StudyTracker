import SubjectSummaryBar from "./SubjectSummaryBar.jsx";
import useAuth from "../auth/useAuth.js";
import {useEffect, useState} from "react";
import {getSessions, getSubjects} from "../../api/sessionApi.js";
import {logger} from "../utils/Logger.js";
import {toast} from "react-hot-toast";
import SubjectList from "./SubjectList.jsx";

function Subjects() {
    const { isAuth } = useAuth();
    const [sessions, setSessions] = useState([]);
    const [subjects, setSubjects] = useState([]);

    useEffect(() => {
        if (isAuth) {
            const sessionData = async () => {
                try {
                    const token = localStorage.getItem("token");
                    const result = await getSessions(token);

                    if (result.success) {
                        setSessions(Array.isArray(result.data) ? result.data : []);
                    }
                } catch (e) {
                    logger.error("Fetching sessions failed:", e);
                }
            };
            sessionData();
        }
    }, [isAuth]);

    useEffect(() => {
        if (isAuth) {
            const subjectData = async () => {
                try {
                    const token = localStorage.getItem("token");
                    const subjectResult = await getSubjects(token);

                    if (subjectResult.success) {
                        setSubjects(Array.isArray(subjectResult.data) ? subjectResult.data : []);
                    } else {
                        toast.error(subjectResult.data.message);
                    }
                } catch (e) {
                    logger.error("Register network error:", e);
                    toast.error("Something went wrong with the connection to the server!");
                }
            };
            subjectData();
        }
    }, [isAuth]);


    return (
        <>
            <SubjectSummaryBar sessionData={sessions} subjectData={subjects} />
            <SubjectList subjects={subjects} sessions={sessions} />
        </>
    );
}

export default Subjects;