import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid } from "recharts";

function ProductivityChart( {sessions} ) {
    return (
        <LineChart width={400} height={300} data={sessions}>
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
    )
}

export default ProductivityChart;