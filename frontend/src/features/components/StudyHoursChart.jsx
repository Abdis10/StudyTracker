import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid } from "recharts";

function StudyHoursChart( {sessions} ) {
    return (
        <LineChart width={400} height={300} data={sessions}>
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
    );
}

export default StudyHoursChart;