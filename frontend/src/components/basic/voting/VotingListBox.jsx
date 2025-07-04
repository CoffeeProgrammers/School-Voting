import React from 'react';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {useNavigate} from "react-router-dom";
import Utils from "../../../utils/Utils";
import {Chip} from "@mui/material";
import HowToVoteIcon from '@mui/icons-material/HowToVote';
import {blueGrey} from "@mui/material/colors";

const VotingListBox = ({voting}) => {
    const navigate = useNavigate();

    const renderDate = (startDate, endDate) => {
        const now = new Date();
        const start = new Date(startDate);
        const end = new Date(endDate);

        if (now < start) {
            return (
                <Chip
                    label="Not started yet"
                    size="small"
                    sx={{mr: 0.5, backgroundColor: blueGrey[50]}}
                />
            );
        }

        if (now >= start && now <= end) {
            const daysLeft = Utils.getDaysLeft(endDate);
            return (
                <Typography mr={1}>
                    {`${daysLeft} day${daysLeft !== 1 ? 's' : ''} left`}
                </Typography>
            )
        }
        return (
            <Chip
                label="Finished"
                size="small"
                sx={{mr: 0.5, backgroundColor: blueGrey[100]}}
            />)
    }


    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '2fr 1fr',
            borderBottom: '1px solid #ddd',
            paddingX: '15px',
            paddingTop: '7px',
            paddingBottom: '15px',
            cursor: 'pointer',
            '&:hover': {
                backgroundColor: '#f5f5f5',
            },
            '&:last-child': {
                borderBottom: 'none',
            },
            '&:first-child': {
                borderTop: '1px solid #ddd',
            },
        }}
             onClick={() => {
                 navigate(`${voting.id}`)
             }}
        >
            <Box mt={0.5}>
                <Typography color='text.secondary' variant='body2'>
                    {"#" + voting.levelType.toLowerCase()}
                </Typography>

                <Typography variant='h5'>
                    {voting.name}
                </Typography>
            </Box>
            <Box mt={8} display="flex" alignItems="center" justifyContent="space-between">
                <Box/>
                <Box display="flex" alignItems="center" gap={0.5}>
                    <Typography>
                        {renderDate(voting.startTime, voting.endTime)}
                    </Typography>
                    <Chip
                        icon={<HowToVoteIcon color="success"/>}
                        label="Voted"
                        size="small"
                        sx={{color: 'success.main', backgroundColor: blueGrey[50]}}
                    />
                </Box>
            </Box>
        </Box>
    );
};

export default VotingListBox;