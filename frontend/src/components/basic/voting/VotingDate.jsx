import React from 'react';
import {Chip} from "@mui/material";
import {blueGrey} from "@mui/material/colors";
import Utils from "../../../utils/Utils";
import Typography from "@mui/material/Typography";

const VotingDate = ({startDate, endDate}) => {
    const now = new Date();
    const start = new Date(startDate);
    const end = new Date(endDate);

    if (now < start) {
        return (
            <Chip
                label="Not started yet"
                size="small"
                sx={{backgroundColor: blueGrey[50]}}
            />
        );
    }

    if (now >= start && now <= end) {
        const daysLeft = Utils.getDaysLeft(endDate);
        return (
            <Typography>
                {`${daysLeft} day${daysLeft !== 1 ? 's' : ''} left`}
            </Typography>
        )
    }
    return (
        <Chip
            label="Finished"
            size="small"
            sx={{backgroundColor: blueGrey[100]}}
        />)
};

export default VotingDate;