import React from 'react';
import { Card, Typography } from '@mui/material';

const Class = ({name}) => {
    return (
            <Card sx={{width: 200,
                height:100,
                borderRadius: "20px",
                padding:5
                }}>
                <Typography>{name}</Typography>
            </Card>
    );
};

export default Class;