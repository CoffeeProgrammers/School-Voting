import React from 'react';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {useNavigate} from "react-router-dom";
import Utils from "../../../utils/Utils";
import {Chip} from "@mui/material";
import HowToVoteIcon from '@mui/icons-material/HowToVote';
import {blueGrey} from "@mui/material/colors";
import VotingDate from "./VotingDate";

const VotingListBox = ({voting}) => {
    const navigate = useNavigate();
    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '2fr 1fr',
            borderBottom: '1px solid #ddd',
            paddingX: '15px',
            paddingTop: '7px',
            paddingBottom: '15px',
            cursor: 'pointer',
            transition: "background-color 0.2s, border-color 0.2s, box-shadow 0.2s",
            "&:hover": {
                cursor: "pointer",
                bgcolor: "#f5f5f5",
                borderColor: "#c6c5c5",
                boxShadow: "0px 0px 5px rgba(0, 0, 0, 0.2)",
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
                   <VotingDate startDate={voting.startDate} endDate={voting.endDate}/>
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