import React from 'react';
import Box from "@mui/material/Box";
import PetitionListBox from "./PetitionListBox";
import {Stack} from "@mui/material";
import Divider from "@mui/material/Divider";

const PetitionList = ({petitions}) => {
    return (
        <Stack direction="column">
            {petitions.map((petition) => (
                <Box key={petition.id}>
                    <PetitionListBox petition={petition}/>
                </Box>
            ))}
            <Divider/>
        </Stack>
    );
};

export default PetitionList;