import Navbar from "./features/components/Navbar.jsx";
import Footer from "./features/components/Footer.jsx";
import {Outlet} from "react-router-dom";
import LogoutCard from "./features/components/LogoutCard.jsx";
import {useState} from "react";
import "../src/features/css/applayout.css";

function AppLayout() {
    const [logoutIsClicked, setLogoutIsClicked] = useState(false);
    return (
        <>
            <div className={`app-layout ${logoutIsClicked ? "app-layout-dimmed" : ""}`}>
                <Navbar onClickLogout={setLogoutIsClicked} />
                <main className="app-main">
                    <Outlet />
                </main>
                <Footer />

                {logoutIsClicked && (
                    <div className="logout-overlay">
                        <div className="logout-box">
                            <LogoutCard onClickLogout={setLogoutIsClicked} />
                        </div>
                    </div>
                )}
            </div>
        </>
    )
}

export default AppLayout;
