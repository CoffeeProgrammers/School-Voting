import React, {useState} from 'react';
import {Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Search from "../../components/layouts/list/Search";
import Divider from "@mui/material/Divider";
import theme from "../../assets/theme";
import VotingListBox from "../../components/basic/voting/VotingListBox";

const voting = [
    {
        "id": 1,
        "name": "Poll on Environmental Policy",
        "levelType": "NATIONAL",
        "startTime": "2025-06-01T08:00:00Z",
        "endTime": "2025-06-05T20:00:00Z",
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
        "isAnswered": true
    },
    {
        "id": 2,
        "name": "Budget Allocation Vote",
        "levelType": "REGIONAL",
        "startTime": "2025-07-01T09:00:00Z",
        "endTime": "2025-07-03T17:00:00Z",
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
        "isAnswered": true
    },
    {
        "id": 4,
        "name": "Local Festival Theme",
        "levelType": "COMMUNITY",
        "startTime": "2025-06-10T12:00:00Z",
        "endTime": "2025-06-11T12:00:00Z",
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
        "isAnswered": true
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


const VotingListPage = () => {
    const [searchName, setSearchName] = useState(null)
    const [filter, setFilter] = useState(null)

    const filters = [
        {value: null, label: 'All'},
        {value: 'ACTIVE', label: 'Active'},
        {value: 'VOTED', label: 'Voted'},

        {value: 'FINISHED', label: 'Finished'},
    ];

    return (
        <Box>
            <Stack direction="row"
                   sx={{alignItems: 'center', display: "flex", justifyContent: "space-between", paddingX: '10px',}}>
                <Typography variant="h6" fontWeight={'bold'}>Voting</Typography>
                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.25}>
                    <Search
                        searchQuery={searchName}
                        setSearchQuery={setSearchName}
                        sx={{mr: 1.5}}
                    />

                    <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                        Create a voting
                    </Button>
                </Box>

            </Stack>

            <Divider sx={{mt: 0.75}}/>
            <Stack direction="row" width={'100%'}>
                {filters.map((option, index) => (
                    <Button key={index} onClick={() => setFilter(option.value)} fullWidth
                            sx={{height: 40, borderRadius: 0, width: index !== 0 ? '100%' : 100}}>
                        <Typography noWrap color={filter === option.value ? 'primary' : 'text.secondary'}
                                    sx={{
                                        borderBottom: "2.5px solid",
                                        borderBottomColor: filter === option.value ? theme.palette.primary.main : "transparent",
                                    }}>
                            {option.label}
                        </Typography>
                    </Button>
                ))}
            </Stack>
            <Divider sx={{mb: 0.75}}/>

            <Stack direction="column">
                {voting.map((voting) => (
                    <Box key={voting.id}>
                        <VotingListBox voting={voting}/>
                    </Box>
                ))}
            </Stack>
        </Box>
    );
};

export default VotingListPage;