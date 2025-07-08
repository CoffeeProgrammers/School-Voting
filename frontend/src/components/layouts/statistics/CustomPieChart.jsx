import {Cell, Pie, PieChart} from 'recharts';
import Typography from "@mui/material/Typography";
import {blueGrey} from "@mui/material/colors";
import Utils from "../../../utils/Utils";


export function CustomPieChart({supportedCount, totalCount, status}) {
    const isSupported = !(supportedCount < totalCount);
    const data = [
        {name: 'Supported', value: Number(supportedCount)},
        {name: 'needForReview', value: isSupported ? 0 : totalCount - Number(supportedCount)}
    ];

    console.log(supportedCount)
    console.log(data)
    const COLORS = [
        Utils.getStatusColor(status),
        blueGrey[50]
    ];

    return (
        <div style={{position: 'relative', width: 200, height: 200}}>
            <PieChart width={200} height={200}>
                <Pie
                    data={data}
                    dataKey="value"
                    cx="50%"
                    cy="50%"
                    innerRadius={67}
                    outerRadius={90}
                    startAngle={90}
                    endAngle={-450}
                    fill="#8884d8"
                    stroke="none"
                >
                    {data.map((_, index) => (
                        <Cell
                            key={index}
                             fill={COLORS[index % COLORS.length]}
                            // style={{fill: COLORS[index % COLORS.length]}}
                        />))}
                </Pie>
            </PieChart>

            <div style={{
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%)',
                textAlign: 'center'
            }}>
                <Typography variant={'h4'} fontWeight={'bold'}>{supportedCount}</Typography>
                <Typography variant="caption" fontWeight="bold"
                            sx={{display: 'flex', flexDirection: 'column', gap: 0, lineHeight: 1}}>
                    <span>votes out of</span>
                    <span>the {totalCount}</span>
                    <span>required</span>
                </Typography>


            </div>
        </div>
    );
}
