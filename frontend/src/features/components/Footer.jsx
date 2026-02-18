import { BookOpen, BarChart3, Clock } from "lucide-react";
import "../css/footer.css";

function Footer() {
    return (
        <footer className="app-footer">
            <div className="footer-wrapper">

                {/* Left Section */}
                <div className="footer-brand">
                    <h3>StudyTracker</h3>
                    <p>Stay consistent. Track progress. Improve daily.</p>
                </div>

                {/* Middle Section */}
                <div className="footer-features">
                    <div className="feature">
                        <Clock size={18} />
                        <span>Track Sessions</span>
                    </div>
                    <div className="feature">
                        <BarChart3 size={18} />
                        <span>Analyze Progress</span>
                    </div>
                    <div className="feature">
                        <BookOpen size={18} />
                        <span>Manage Subjects</span>
                    </div>
                </div>

                {/* Right Section */}
                <div className="footer-meta">
                    <p>© {new Date().getFullYear()} StudyTracker</p>
                    <p>Built with React & Java</p>
                </div>

            </div>
        </footer>
    );
}

export default Footer;
