import React, {useState} from 'react';
import Cookies from "js-cookie";
import {Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import theme from "../../../assets/theme";
import CreatedPetitionsList from "../petition/CreatedPetitionsList";
import CreatedVotingList from "../voting/CreatedVotingList";
import ParticipatedVotingList from "../voting/ParticipatedVoitingList";
import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";

const UserAdditionBox = () => {
    const role = Cookies.get('role');
    const isStudent = role === 'STUDENT';
    const isDirector = role === 'DIRECTOR';
    const isTeacher = role === 'TEACHER';

    const [tab, setTab] = useState(!isStudent ? "Created Voting" : "Created Petitions")

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
        <div>
            {!isDirector && (
                <Box sx={{border: '1px solid #ddd', borderRadius: '5px', padding: '17px', pb: 0}}>
                    <Stack direction="row" width={'100%'} ml={1.25}>
                        {isStudent && renderTabButton('Created Petitions', 170)}
                        {renderTabButton('Created Voting', 170)}
                        {renderTabButton('Participated Voting', 170)}

                    </Stack>

                    <Divider sx={{mb: 0.75}}/>

                    {renderTab()}
                </Box>
            )}
        </div>
    );
};

export default UserAdditionBox;