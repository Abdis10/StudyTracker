import Navbar from "../components/Navbar.jsx";
import Card from "../components/Card.jsx";
import StudySessionCard from "../components/StudySessionCard.jsx";


function Dashboard() {
    return (
        <div className="container">
            <Navbar />
            <div className="welcome-section">
                <h1>Welcome Back, James!</h1>
                <p>Track your study progess and stay on top of your productivity.</p>
            </div>
            <div className="study-summary-section">
                <Card title="Study Summary">
                    <StudySessionCard />
                </Card>
            </div>
            <div className="Recent-s-sessions">

            </div>
        </div>
    )
}


export default Dashboard;