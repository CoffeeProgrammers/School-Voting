import React from 'react';
import {LinearProgress} from "@mui/material";
import Box from "@mui/material/Box";

const Progress = ({progressPercent, color}) => {

    return (
        <Box sx={{display: 'flex', alignItems: 'center'}}>
            <Box sx={{width: '100%', mr: 1}}>
                <LinearProgress color={color} variant="determinate"
                                value={progressPercent}/>
            </Box>
        </Box>
    );
};

export default Progress;