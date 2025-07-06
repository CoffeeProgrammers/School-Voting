import React from 'react';
import Box from "@mui/material/Box";
import PetitionListBox from "./PetitionListBox";
import {Stack} from "@mui/material";

const PetitionList = ({petitions}) => {
    return (
        <Stack direction="column">
            {petitions.map((petition) => (
                <Box key={petition.id}>
                    <PetitionListBox petition={petition}/>
                </Box>
            ))}
        </Stack>
    );
};

export default PetitionList;