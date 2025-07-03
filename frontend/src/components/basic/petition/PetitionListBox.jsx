import React from 'react';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {Chip} from "@mui/material";
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import Progress from "../../layouts/Progress";
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import PeopleRoundedIcon from '@mui/icons-material/PeopleRounded';
import CancelOutlinedIcon from '@mui/icons-material/CancelOutlined';
import Utils from "../../../utils/Utils";
import {useNavigate} from "react-router-dom";
import ThumbUpAltIcon from "@mui/icons-material/ThumbUpAlt";

const PetitionListBox = ({petition}) => {
    const navigate = useNavigate();

    const percentage = Math.min(
        (petition.countSupport * 100) / petition.countNeeded,
        100
    );

    const date = Utils.getDaysLeft(petition.endTime);
    const viewDate = date < 0 ? 'Expired' : date + " days left";

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
                 navigate(`${petition.id}`)
             }}
        >
            <Box mt={0.5}>
                <Typography color='text.secondary' variant='body2'>
                    {"#" + petition.levelType}
                </Typography>

                <Typography variant='h5'>
                    {petition.name}
                </Typography>
            </Box>
            <Box>
                <Box mt={2}>
                    <Typography variant='h5' mb={0.5} fontWeight="bold">
                        {petition.countSupport}
                    </Typography>

                    <Progress status={petition.status} progressPercent={percentage}/>
                </Box>
                <Box mt={0.75} display="flex" alignItems="center">
                    {Utils.getStatus(petition.status, {mr: 0.25, fontSize: 18}, {fontSize: 13})}
                </Box>
                <Box mt={1.5} display="flex" justifyContent="space-between" alignItems="center">
                    {petition.status === 'ACTIVE' ? (
                        <Typography variant='body2'>
                            {viewDate}
                        </Typography>
                    ) : (
                        <Box/>
                    )}
                    {petition.supportedByCurrentUser ? (
                        <Chip
                            icon={<ThumbUpAltIcon color="success"/>}
                            label="Supported"
                            size="small"
                            sx={{color: 'success.main', ml: 1, mr: 2}}
                        />
                    ) : <Box mt={1.5}/>}
                </Box>

            </Box>
        </Box>
    );
};

export default PetitionListBox;