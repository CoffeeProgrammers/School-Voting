import React, {useState} from 'react';
import Box from "@mui/material/Box";
import PersonIcon from "@mui/icons-material/Person";
import Typography from "@mui/material/Typography";
import EmailIcon from "@mui/icons-material/Email";
import {Button, Stack} from "@mui/material";
import theme from "../../assets/theme";
import Divider from "@mui/material/Divider";
import AttributionRoundedIcon from '@mui/icons-material/AttributionRounded';
import PetitionListBox from "../../components/basic/petition/PetitionListBox";
import VotingListBox from "../../components/basic/voting/VotingListBox";

const SCHOOL_PETITIONS = [
    {
        id: 1,
        name: "Increase funding for school libraries",
        endTime: "2025-09-30T23:59:59",
        levelType: "class",
        status: "ACTIVE",
        countSupport: 35,
        countNeeded: 100,
        supportedByCurrentUser: false,
    },
    {
        id: 2,
        name: "Introduce mandatory media literacy lessons",
        endTime: "2025-10-15T23:59:59",
        levelType: "school",
        status: "WAITING_FOR_CONSIDERATION",
        countSupport: 52,
        countNeeded: 500,
        supportedByCurrentUser: true,
    },
    {
        id: 3,
        name: "Install water filters in all city schools",
        endTime: "2025-09-10T23:59:59",
        levelType: "class",
        status: "APPROVED",
        countSupport: 134,
        countNeeded: 100,
        supportedByCurrentUser: false,
    },
    {
        id: 4,
        name: "Improve the quality of school meals",
        endTime: "2025-11-01T23:59:59",
        levelType: "school",
        status: "ACTIVE",
        countSupport: 88,
        countNeeded: 200,
        supportedByCurrentUser: true,
    },
    {
        id: 5,
        name: "Add more extracurricular clubs and activities",
        endTime: "2025-09-25T23:59:59",
        levelType: "class",
        status: "WAITING_FOR_CONSIDERATION",
        countSupport: 47,
        countNeeded: 150,
        supportedByCurrentUser: false,
    },
    {
        id: 6,
        name: "Replace broken desks and chairs in classrooms",
        endTime: "2025-10-05T23:59:59",
        levelType: "school",
        status: "REJECTED",
        countSupport: 1065,
        countNeeded: 300,
        supportedByCurrentUser: false,
    },
    {
        id: 7,
        name: "Replace broken desks and chairs in classrooms",
        endTime: "2025-09-20T23:59:59",
        levelType: "class",
        status: "APPROVED",
        countSupport: 23,
        countNeeded: 100,
        supportedByCurrentUser: false,
    },
    {
        id: 8,
        name: "Ensure mental health support in every school",
        endTime: "2025-10-30T23:59:59",
        levelType: "school",
        status: "WAITING_FOR_CONSIDERATION",
        countSupport: 271,
        countNeeded: 250,
        supportedByCurrentUser: true,
    },
    {
        id: 9,
        name: "Add more digital equipment for online learning",
        endTime: "2025-09-18T23:59:59",
        levelType: "class",
        status: "UNSUCCESSFUL",
        countSupport: 41,
        countNeeded: 120,
        supportedByCurrentUser: false,
    },
    {
        id: 10,
        name: "Introduce basic financial literacy in high schools",
        endTime: "2025-11-10T23:59:59",
        levelType: "school",
        status: "ACTIVE",
        countSupport: 93,
        countNeeded: 300,
        supportedByCurrentUser: true,
    },
];
const voting = [
    {
        "id": 1,
        "name": "Poll on Environmental Policy",
        "levelType": "NATIONAL",
        "startTime": "2024-09-30T23:59:59",
        "endTime": "2026-09-30T23:59:59",
        "statistics": {
            "answers": [
                {"id": 1, "name": "Yes", "count": 900},
                {"id": 2, "name": "No", "count": 500},
                {"id": 3, "name": "Abstain", "count": 100},
                {"id": 4, "name": "Need more info", "count": 50}
            ],
            "countAll": 1800,
            "countAllAnswered": 1550
        },
        "isAnswered": false
    },
    {
        "id": 2,
        "name": "Budget Allocation Vote",
        "levelType": "REGIONAL",
        "startTime": "2025-07-01T09:00:00Z",
        "endTime": "2026-07-03T17:00:00Z",
        "statistics": {
            "answers": [
                {"id": 5, "name": "Yes", "count": 430},
                {"id": 6, "name": "No", "count": 290},
                {"id": 7, "name": "Abstain", "count": 60},
                {"id": 8, "name": "Partially support", "count": 70}
            ],
            "countAll": 950,
            "countAllAnswered": 850
        },
        "isAnswered": false
    },
    {
        "id": 3,
        "name": "New Public Transport Lines",
        "levelType": "CITY",
        "startTime": "2025-06-15T10:00:00Z",
        "endTime": "2025-06-20T18:00:00Z",
        "statistics": {
            "answers": [
                {"id": 9, "name": "Yes", "count": 800},
                {"id": 10, "name": "No", "count": 250},
                {"id": 11, "name": "Abstain", "count": 100}
            ],
            "countAll": 1250,
            "countAllAnswered": 1150
        },
        "isAnswered": false
    },
    {
        "id": 4,
        "name": "Local Festival Theme",
        "levelType": "COMMUNITY",
        "startTime": "2025-06-10T12:00:00Z",
        "endTime": "2026-06-11T12:00:00Z",
        "statistics": {
            "answers": [
                {"id": 12, "name": "Yes", "count": 60},
                {"id": 13, "name": "No", "count": 90},
                {"id": 14, "name": "Abstain", "count": 10},
                {"id": 15, "name": "Prefer other theme", "count": 30}
            ],
            "countAll": 250,
            "countAllAnswered": 190
        },
        "isAnswered": true
    },
    {
        "id": 5,
        "name": "Change School Schedule",
        "levelType": "REGIONAL",
        "startTime": "2025-05-20T08:00:00Z",
        "endTime": "2025-05-25T20:00:00Z",
        "statistics": {
            "answers": [
                {"id": 16, "name": "Yes", "count": 600},
                {"id": 17, "name": "No", "count": 400},
                {"id": 18, "name": "Abstain", "count": 120}
            ],
            "countAll": 1200,
            "countAllAnswered": 1120
        },
        "isAnswered": false
    },
    {
        "id": 6,
        "name": "City Clean-up Initiative",
        "levelType": "CITY",
        "startTime": "2025-07-01T10:00:00Z",
        "endTime": "2025-07-10T22:00:00Z",
        "statistics": {
            "answers": [
                {"id": 19, "name": "Yes", "count": 400},
                {"id": 20, "name": "No", "count": 250},
                {"id": 21, "name": "Abstain", "count": 80},
                {"id": 22, "name": "Volunteer only", "count": 70},
                {"id": 23, "name": "Donate only", "count": 40}
            ],
            "countAll": 950,
            "countAllAnswered": 840
        },
        "isAnswered": false
    },
    {
        "id": 7,
        "name": "Renaming Public Spaces",
        "levelType": "NATIONAL",
        "startTime": "2025-05-01T09:00:00Z",
        "endTime": "2025-05-07T21:00:00Z",
        "statistics": {
            "answers": [
                {"id": 24, "name": "Yes", "count": 1000},
                {"id": 25, "name": "No", "count": 800},
                {"id": 26, "name": "Abstain", "count": 200}
            ],
            "countAll": 2200,
            "countAllAnswered": 2000
        },
        "isAnswered": true
    },
    {
        "id": 8,
        "name": "Smoking Ban in Public Parks",
        "levelType": "CITY",
        "startTime": "2025-07-01T07:00:00Z",
        "endTime": "2025-07-08T18:00:00Z",
        "statistics": {
            "answers": [
                {"id": 27, "name": "Yes", "count": 520},
                {"id": 28, "name": "No", "count": 310},
                {"id": 29, "name": "Abstain", "count": 100}
            ],
            "countAll": 1100,
            "countAllAnswered": 930
        },
        "isAnswered": false
    },
    {
        "id": 9,
        "name": "Electric Vehicle Subsidies",
        "levelType": "NATIONAL",
        "startTime": "2025-06-01T00:00:00Z",
        "endTime": "2025-06-10T00:00:00Z",
        "statistics": {
            "answers": [
                {"id": 30, "name": "Yes", "count": 1300},
                {"id": 31, "name": "No", "count": 700},
                {"id": 32, "name": "Abstain", "count": 200},
                {"id": 33, "name": "Only for low-income", "count": 150}
            ],
            "countAll": 2500,
            "countAllAnswered": 2350
        },
        "isAnswered": true
    },
    {
        "id": 10,
        "name": "Mandatory Recycling",
        "levelType": "CITY",
        "startTime": "2026-07-01T06:00:00Z",
        "endTime": "2028-07-04T20:00:00Z",
        "statistics": {
            "answers": [
                {"id": 34, "name": "Yes", "count": 870},
                {"id": 35, "name": "No", "count": 410},
                {"id": 36, "name": "Abstain", "count": 100},
                {"id": 37, "name": "Only for businesses", "count": 70}
            ],
            "countAll": 1600,
            "countAllAnswered": 1450
        },
        "isAnswered": false
    }
]


const user = {
    id: 1,
    firstName: "Alice",
    lastName: "Johnson",
    email: "alice.johnson@example.com",
    role: "student",
}

const Profile = () => {
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
                return (
                    <Stack direction="column">
                        {SCHOOL_PETITIONS.map((petition) => (
                            <Box key={petition.id}>
                                <PetitionListBox petition={petition}/>
                            </Box>
                        ))}
                    </Stack>)
            case 'Created Voting':
                return <Stack direction="column">
                    {voting.map((voting) => (
                        <Box key={voting.id}>
                            <VotingListBox voting={voting}/>
                        </Box>
                    ))}
                </Stack>
            case 'Participated Voting':
                return <Stack direction="column">
                    {voting.map((voting) => (
                        <Box key={voting.id}>
                            <VotingListBox voting={voting}/>
                        </Box>
                    ))}
                </Stack>
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


            <Box sx={{border: '1px solid #ddd', borderRadius: '5px', padding: '17px'}}>
                <Stack direction="row" width={'100%'} ml={1.25}>
                    {renderTabButton('Created Petitions', 170)}
                    {renderTabButton('Created Voting', 170)}
                    {renderTabButton('Participated Voting', 170)}

                </Stack>

                <Divider sx={{mb: 0.75}}/>

                {renderTab()}
            </Box>

        </Box>
    );
};

export default Profile;