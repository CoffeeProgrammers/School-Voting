import React from 'react';
import Divider from "@mui/material/Divider";
import {Stack} from "@mui/material";
import Box from "@mui/material/Box";
import PetitionListBox from "../../components/basic/petition/PetitionListBox";
import Typography from "@mui/material/Typography";

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

const PetitionsReviewPage = () => {
    return (
        <Box>
            <Typography variant="h6" fontWeight={'bold'} paddingX={'10px'}>Petitions Review</Typography>

            <Divider sx={{mb: 0.75}}/>

            <Stack direction="column">
                {SCHOOL_PETITIONS.map((petition) => (
                    <Box key={petition.id}>
                        <PetitionListBox petition={petition}/>
                    </Box>
                ))}
            </Stack>

        </Box>
    );
};

export default PetitionsReviewPage;