import React from 'react';
import {LinearProgress} from "@mui/material";
import Box from "@mui/material/Box";

const Progress = ({progressPercent, status}) => {

    const getColor = (status) => {
        switch (status) {
            case 'ACTIVE':
                return 'secondary';
            case 'UNSUCCESSFUL':
                return 'error';
            case 'WAITING_FOR_CONSIDERATION':
                return 'secondary';
            case 'APPROVED':
                return 'success';
            case 'REJECTED':
                return 'error';
            default:
                return null;
        }
    };
    return (
        <Box sx={{display: 'flex', alignItems: 'center'}}>
            <Box sx={{width: '100%', mr: 1}}>
                <LinearProgress color={getColor(status)} variant="determinate"
                                value={progressPercent}/>
            </Box>
        </Box>
    );
};

export default Progress;