import React from 'react';
import {Stack} from "@mui/material";
import Box from "@mui/material/Box";
import VotingListBox from "./VotingListBox";
import Divider from "@mui/material/Divider";

const VotingList = ({votingList}) => {
    return (
        <Stack direction="column">
            {votingList.map((voting) => (
                <Box key={voting.id}>
                    <VotingListBox voting={voting}/>
                </Box>
            ))}
            <Divider/>
        </Stack>
    );
};

export default VotingList;