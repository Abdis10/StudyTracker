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
            <div className={`${logoutIsClicked ? "background-dimmed" : "container"}`}>
                <Navbar onClickLogout={setLogoutIsClicked} />
                <main>
                    <Outlet />
                </main>
                <Footer />
                {/* ✅ Modal OUTSIDE grid */}
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