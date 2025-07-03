import React from 'react';
import {LinearProgress} from "@mui/material";
import Box from "@mui/material/Box";
import Utils from "../../utils/Utils";

const Progress = ({progressPercent, status}) => {

    return (
        <Box sx={{display: 'flex', alignItems: 'center'}}>
            <Box sx={{width: '100%', mr: 1}}>
                <LinearProgress color={Utils.getStatusMUIColor(status)} variant="determinate"
                                value={progressPercent}/>
            </Box>
        </Box>
    );
};

export default Progress;