import React from 'react';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {Chip} from "@mui/material";
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import Progress from "../../layouts/Progress";
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import PeopleRoundedIcon from '@mui/icons-material/PeopleRounded';
import CancelOutlinedIcon from '@mui/icons-material/CancelOutlined';
import DateUtils from "../../../utils/DateUtils";

const PetitionListBox = ({petition}) => {

    const percentage = Math.min(
        (petition.countSupport * 100) / petition.countNeeded,
        100
    );

    const date = DateUtils.getDaysLeft(petition.endTime);
    const viewDate = date < 0 ? 'Expired' : date + " days left";

    const getStatus = (status) => {
        switch (status) {
            case 'ACTIVE':
                return (
                    <>
                        <PeopleRoundedIcon color="secondary" sx={{mr: 0.25, fontSize: 18}}/>
                        <Typography sx={{fontSize: 13}}>
                            Active
                        </Typography>
                    </>
                );
            case 'UNSUCCESSFUL':
                return (
                    <>
                        <CancelOutlinedIcon color="error" sx={{mr: 0.25, fontSize: 18}}/>
                        <Typography sx={{fontSize: 13}}>
                            Unsuccessful
                        </Typography>
                    </>
                );
            case 'WAITING_FOR_CONSIDERATION':
                return (
                    <>
                        <AccessTimeIcon color="secondary" sx={{mr: 0.25, fontSize: 18}}/>
                        <Typography sx={{fontSize: 13}}>
                            Waiting for consideration
                        </Typography>
                    </>
                );
            case 'APPROVED':
                return (
                    <>
                        <CheckCircleOutlineIcon color="success" sx={{mr: 0.25, fontSize: 18}}/>
                        <Typography sx={{fontSize: 13}}>
                            Approved
                        </Typography>
                    </>
                );
            case 'REJECTED':
                return (
                    <>
                        <CancelOutlinedIcon color="error" sx={{mr: 0.25, fontSize: 18}}/>
                        <Typography sx={{fontSize: 13}}>
                            Rejected
                        </Typography>
                    </>
                );
            default:
                return null;
        }
    };

    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '2fr 1fr',
            borderBottom: '1px solid #ddd',
            paddingTop: '7px',
            paddingBottom: '15px',
            cursor: 'pointer'
        }}>
            <Box mt={0.5} >
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
                    {getStatus(petition.status)}
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
                            icon={<CheckCircleOutlineIcon color="success"/>}
                            label="Supported"
                            size="small"
                            sx={{backgroundColor: '#a8dca7', color: 'success.main', ml: 1, mr: 2}}
                        />
                    ) : <Box mt={1.5}/>}
                </Box>

            </Box>
        </Box>
    );
};

export default PetitionListBox;