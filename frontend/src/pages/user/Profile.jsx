import React, {useState} from 'react';
import Box from "@mui/material/Box";
import PersonIcon from "@mui/icons-material/Person";
import Typography from "@mui/material/Typography";
import EmailIcon from "@mui/icons-material/Email";
import {Button, Stack} from "@mui/material";
import theme from "../../assets/theme";
import Divider from "@mui/material/Divider";
import AttributionRoundedIcon from '@mui/icons-material/AttributionRounded';
import CreatedPetitionsList from "../../components/basic/petition/CreatedPetitionsList";
import CreatedVotingList from "../../components/basic/voting/CreatedVotingList";
import ParticipatedVotingList from "../../components/basic/voting/ParticipatedVoitingList";
import Cookies from "js-cookie";

const user = {
    id: 1,
    firstName: "Alice",
    lastName: "Johnson",
    email: "alice.johnson@example.com",
    role: "student",
}

const Profile = () => {
    const role = Cookies.get('role');
    const isStudent = role === 'STUDENT';
    const isDirector = role === 'DIRECTOR';

    const [tab, setTab] = useState("Created Petitions")

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
            case 'Created Petitions':
                return <CreatedPetitionsList/>
            case 'Created Voting':
                return <CreatedVotingList/>
            case 'Participated Voting':
                return <ParticipatedVotingList/>
        }
    }

    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '25.5% 70%',
            paddingX: 1,
            paddingTop: 2,
            paddingBottom: 4,
            gap: 3.5,
        }}>
            <Box>
                <Box sx={{
                    border: '1px solid #ddd',
                    borderRadius: '5px',
                    padding: '17px',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'
                }}>
                    <Box sx={{
                        height: 180,
                        width: 180,
                        padding: 0,
                        marginBottom: "10px",
                        borderRadius: "100%",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        border: 3,
                        borderColor: 'primary.main'
                    }}>
                        <PersonIcon color="primary" sx={{fontSize: 180, padding: 0, margin: 0}}/>
                    </Box>
                    <Typography variant='h6' fontWeight='bold'>{user.firstName + " " + user.lastName}</Typography>

                    <Box sx={{display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 0.375, mx: 0.5}}>
                        <EmailIcon sx={{fontSize: 20, marginLeft: 1, mt: 0.3}} color='primary'/>
                        <Typography sx={{fontSize: 14.75,}}>
                            {user.email}
                        </Typography>
                    </Box>

                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.375, mx: 0.5, mr: 1.5}}>
                        <AttributionRoundedIcon sx={{fontSize: 22, marginLeft: 1, mt: 0.3}} color='primary'/>
                        <Typography sx={{fontSize: 14.75,}}>
                            {user.role}
                        </Typography>
                    </Box>

                </Box>
            </Box>

            {!isDirector && (
                <Box sx={{border: '1px solid #ddd', borderRadius: '5px', padding: '17px'}}>
                    <Stack direction="row" width={'100%'} ml={1.25}>
                        {isStudent && renderTabButton('Created Petitions', 170)}
                        {renderTabButton('Created Voting', 170)}
                        {renderTabButton('Participated Voting', 170)}

                    </Stack>

                    <Divider sx={{mb: 0.75}}/>

                    {renderTab()}
                </Box>
            )}


        </Box>
    );
};

export default Profile;