import { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from "./features/pages/LoginPage.jsx";
import SignupPage from "./features/pages/SignupPage.jsx";
import Dashboard from "./features/pages/Dashboard.jsx";

function App() {

  return (
    <BrowserRouter>
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/dashboard" element={<Dashboard />} />
        </Routes>
    </BrowserRouter>
  )
}

export default App
