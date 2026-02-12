import { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import LoginPage from "./features/pages/LoginPage.jsx";
import SignupPage from "./features/pages/SignupPage.jsx";
import Dashboard from "./features/pages/Dashboard.jsx";
import ProtectedRoute from "./features/auth/ProtectedRoute.jsx";
import {validateSession} from "./api/authApi.js";

function App() {
    return (
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/dashboard"
                   element={
                    <ProtectedRoute>
                        <Dashboard />
                    </ProtectedRoute>
                   }
            />
        </Routes>
  )
}

export default App
