import React from 'react';
import {LinearProgress} from "@mui/material";
import Box from "@mui/material/Box";

const Progress = ({count, maxCount, color}) => {

    const percentage = maxCount !== 0 ? (Math.min(
        (count * 100) / maxCount,
        100
    )) : (0);
    return (
        <Box sx={{display: 'flex', alignItems: 'center'}}>
            <Box sx={{width: '100%', mr: 1}}>
                <LinearProgress color={color} variant="determinate"
                                value={percentage}/>
            </Box>
        </Box>
    );
};

export default Progress;