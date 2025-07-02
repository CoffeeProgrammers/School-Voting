import React, {useState} from 'react';
import Search from "../../components/layouts/list/Search";
import {Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import theme from "../../assets/theme";
import Box from "@mui/material/Box";
import PetitionListBox from "../../components/basic/petition/PetitionListBox";

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
        name: "Provide free menstrual hygiene products in schools",
        endTime: "2025-10-05T23:59:59",
        levelType: "school",
        status: "REJECTED",
        countSupport: 65,
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


const PetitionsListPage = () => {
    const [searchName, setSearchName] = useState(null)
    const [status, setStatus] = useState(null)

    const statusFilter = [
        {value: null, label: 'All'},
        {value: 'ACTIVE', label: 'Active'},
        {value: 'WAITING_FOR_CONSIDERATION', label: 'Waiting for consideration'},
        {value: 'UNSUCCESSFUL', label: 'Unsuccessful'},
        {value: 'APPROVED', label: 'Approved'},
        {value: 'REJECTED', label: 'Rejected'},
    ];

    return (
        <>

            <Stack direction="row" sx={{alignItems: 'center', display: "flex", justifyContent: "space-between "}}>
                <Typography variant="h6" fontWeight={'bold'}>Petitions</Typography>
                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.5}>
                    <Search
                        searchQuery={searchName}
                        setSearchQuery={setSearchName}
                        sx={{mr: 1.5}}
                    />

                    <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>Create a
                        petition
                    </Button>
                </Box>

            </Stack>
            <Divider sx={{mt: 0.75}}/>
            <Stack direction="row" width={'100%'}>
                {statusFilter.map((option, index) => (
                    <Button key={index}
                            onClick={() => setStatus(option.value)}
                            fullWidth
                            sx={{height: 40, borderRadius: 0, width: index !== 0 ? '100%' : 100}}>
                        <Typography color={status === option.value ? 'primary' : 'text.secondary'}
                                    sx={{
                                        borderBottom: "2.5px solid",
                                        borderBottomColor: status === option.value ? theme.palette.primary.main : "transparent",
                                    }}>
                            {option.label}
                        </Typography>
                    </Button>
                ))}
            </Stack>
            <Divider sx={{mb: 0.75}}/>

            <Stack direction="column">
                {SCHOOL_PETITIONS.map((petition) => (
                    <Box key={petition.id}>
                        <PetitionListBox petition={petition}/>
                    </Box>
                ))}
            </Stack>
        </>
    );
};

export default PetitionsListPage;