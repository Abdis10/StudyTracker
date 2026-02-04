import { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from "./features/pages/LoginPage.jsx";
import SignupPage from "./features/pages/SignupPage.jsx";

function App() {

  return (
    <BrowserRouter>
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
        </Routes>
    </BrowserRouter>
  )
}

export default App
