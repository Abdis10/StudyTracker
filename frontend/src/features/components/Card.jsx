import "../css/card.css";

function Card({ children, title }) {
    return (
        <div className="card">
            {title && (
                <div className="card-header">
                    <h3>{title}</h3>
                </div>
            )}
            <div className="card-content">
                {children}
            </div>
        </div>
    );
}

export default Card;