import React, {useState} from 'react';
import {useParams} from "react-router-dom";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Divider from "@mui/material/Divider";
import Groups2Icon from '@mui/icons-material/Groups2';
import {Button, Stack} from "@mui/material";
import {CustomPieChart} from "../../components/layouts/statistics/CustomPieChart";
import Utils from "../../utils/Utils";
import {blueGrey} from "@mui/material/colors";
import ThumbUpAltIcon from '@mui/icons-material/ThumbUpAlt';
import theme from "../../assets/theme";

const petition = {
    id: 1,
    name: "Quality Wi-Fi for All: Let's Ensure Fast and Stable Internet in Our School!",
    description: "We urge the administration of School №X (or [School Name]) to improve the quality of Wi-Fi coverage and internet speed throughout the entire school. Stable and fast internet is crucial for modern learning, access to online resources, and electronic journals and diaries. Current Wi-Fi issues hinder an effective educational process. Support this petition to help us create a more comfortable and tech-friendly environment for everyone!",
    endTime: "2025-09-30T23:59:59",
    levelType: "class",
    status: "ACTIVE",
    countSupport: 247,
    countNeeded: 350,
    supportedByCurrentUser: true,
    creator: {
        id: 10,
        email: "jane.doe@example.com",
        firstName: "Jane",
        lastName: "Doe"
    }
};

const PetitionPage = () => {
    const {petitionId} = useParams();

    const [tab, setTab] = useState("Description")

    const viewDate = Utils.getDaysLeft(petition.endTime) + ' days left';

    const renderSuccessSupportButton = () => {
        return (
            <Button disabled startIcon={<ThumbUpAltIcon color='success'/>} variant="contained"
                    color="success"
                    sx={{
                        height: 32,
                        borderRadius: 10,
                        fontWeight: 500,
                        "&.Mui-disabled": {
                            backgroundColor: blueGrey[50],
                            color: theme => theme.palette.success.main,
                            opacity: 1,
                        }
                    }}>
                Supported
            </Button>
        )
    }

    const renderTabButton = (title, width) => {
        return (
            <Button onClick={() => setTab(title)} sx={{height: 33, borderRadius: 0, width: width}}>
                <Typography variant='body1' color={tab === title ? 'primary' : 'text.secondary'}
                            sx={{
                                borderBottom: "2.5px solid",
                                borderBottomColor: tab === title ? theme.palette.primary.main : "transparent",
                            }}>
                    {title}
                </Typography>
            </Button>
        )
    }

    const renderTab = () => {
        switch (tab) {
            case 'Description':
                return (
                    <Typography variant='body1' mt={1.25}>
                        {petition.description}
                    </Typography>)
            case 'Comments':
                return <Box mt={2.5}>Comments</Box>
        }
    }
    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '3fr 1.3fr',
            paddingX: 5,
            paddingTop: 2,
            paddingBottom: 4
        }}>
            <Box paddingRight={4} mt={4.5}>
                <Typography variant='h4'>
                    {petition.name}
                </Typography>


                <Stack direction='column' sx={{marginBottom: 4, marginTop: 1.2, gap: 0.5, paddingX: 1}}>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <AccountCircleIcon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.creator.firstName + " " + petition.creator.lastName}
                        </Typography>
                        <Divider orientation="vertical" sx={{height: 15, color: 'black', marginX: 0.5}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.creator.email}
                        </Typography>

                    </Box>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <Groups2Icon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.levelType === 'class' ? 'Class' : 'School'}
                        </Typography>
                    </Box>
                </Stack>

                <Stack direction="row" width={'100%'}>
                    {renderTabButton('Description', 105)}
                    {renderTabButton('Comments', 100)}
                </Stack>

                <Divider/>

                <Box paddingX={1}>
                    {renderTab()}
                </Box>


            </Box>

            <Box>
                <Box sx={{border: '1px solid #ddd', borderRadius: '5px', padding: '15px', marginX: 1}}>
                    <Box sx={{alignItems: 'center', display: 'flex', justifyContent: 'center',}}>
                        <CustomPieChart
                            supportedCount={petition.countSupport}
                            totalCount={petition.countNeeded}
                            status={petition.status}
                        />

                    </Box>
                    <Box mt={0.25} sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Box display="flex" alignItems="center">
                            {Utils.getStatus(
                                petition.status,
                                {mr: 0.5, fontSize: 20},
                                {fontSize: 14.5}
                            )}
                        </Box>
                        {petition.status === 'ACTIVE' ? (
                            <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                <Typography mt={0.55}>
                                    {viewDate}
                                </Typography>
                                {petition.supportedByCurrentUser ? (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                        {renderSuccessSupportButton()}
                                    </Box>
                                ) : (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                        <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                                            Support petition
                                        </Button>
                                    </Box>
                                )}

                            </Box>
                        ) : (
                            petition.supportedByCurrentUser && (
                                <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                    {renderSuccessSupportButton()}
                                </Box>
                            ))}

                    </Box>

                </Box>
            </Box>
        </Box>
    );
};

export default PetitionPage;