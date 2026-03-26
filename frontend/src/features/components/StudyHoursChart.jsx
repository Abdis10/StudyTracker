import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";

function StudyHoursChart( {sessions} ) {
    return (
        <div className="chart-shell">
            <ResponsiveContainer width="100%" height="100%">
                <LineChart data={sessions}>
                    <XAxis dataKey="date" tickFormatter={(date) => new Date(date).toLocaleDateString("en-US", {weekday: "short"})} />
                    <YAxis />
                    <Tooltip />
                    <Line
                        type="monotone"
                        dataKey="hours"
                        strokeWidth={3}
                        isAnimationActive={true}
                        animationDuration={800}
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
}

export default StudyHoursChart;
