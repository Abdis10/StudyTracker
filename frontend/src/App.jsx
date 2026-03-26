import {Routes, Route, Navigate} from 'react-router-dom';
import LoginPage from "./features/pages/LoginPage.jsx";
import SignupPage from "./features/pages/SignupPage.jsx";
import Dashboard from "./features/pages/Dashboard.jsx";
import ProtectedRoute from "./features/auth/ProtectedRoute.jsx";
import StudySessionsPage from "./features/pages/StudySessionsPage.jsx";
import AppLayout from "./AppLayout.jsx";
import Subjects from "./features/components/Subjects.jsx";
import Reports from "./features/components/Reports.jsx";

function App() {
    return (
        <Routes>
            {/* Public */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />

            {/* Protected area */}
            <Route element={<ProtectedRoute /> } >
                <Route element={<AppLayout /> }>
                    <Route path="/" element={<Navigate to="/dashboard" replace />} />
                    <Route path="/dashboard" element={<Dashboard />}/>
                    <Route path="/sessions" element={<StudySessionsPage />} />
                    <Route path="/subjects" element={<Subjects />} />
                    <Route path="/reports" element={<Reports />} />
                </Route>
            </Route>
        </Routes>
  )
}

export default App
