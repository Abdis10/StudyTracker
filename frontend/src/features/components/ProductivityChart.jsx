import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from "recharts";

function ProductivityChart( {sessions} ) {
    return (
        <div className="chart-shell">
            <ResponsiveContainer width="100%" height="100%">
                <LineChart data={sessions}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" tickFormatter={(date) => new Date(date).toLocaleDateString("en-US", {weekday: "short"})} />
                    <YAxis domain={[0, 10]} />
                    <Tooltip />
                    <Line
                        type="monotone"
                        dataKey="productivityScore"
                        strokeWidth={3}
                        isAnimationActive={true}
                        animationDuration={800}
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    )
}

export default ProductivityChart;
