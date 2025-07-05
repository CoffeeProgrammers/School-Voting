import React, {useState} from 'react';
import {Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import theme from "../../assets/theme";
import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import Groups2Icon from "@mui/icons-material/Groups2";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import VotingAnswerBox from "../../components/basic/voting/VotingAnswerBox";
import VotingDate from "../../components/basic/voting/VotingDate";
import {blueGrey} from "@mui/material/colors";
import HowToVoteIcon from "@mui/icons-material/HowToVote";
import UserList from "../../components/basic/user/UserList";

const voting = {
    "id": 1,
    "name": "Poll on Environmental Policy",
    "description": "lorem ipsum dolor sit amet, consectetur adipiscing elit lorem ipsum dolor sit amet, consectetur adipiscing elitlorem ipsum dolor sit amet, consectetur adipiscing elit lorem ipsum dolor sit amet, consectetur adipiscing elit lorem ipsum dolor sit amet, consectetur adipiscing elitvlorem ipsum dolor sit amet, consectetur adipiscing elit lorem ipsum dolor sit amet, consectetur adipiscing elitvlorem ipsum dolor sit amet, consectetur adipiscing elitvlorem ipsum dolor sit amet, consectetur adipiscing elitvlorem ipsum dolor sit amet, consectetur adipiscing elitvlorem ipsum dolor sit amet, consectetur adipiscing elitvlorem ipsum dolor sit amet, consectetur adipiscing elitlorem ipsum dolor sit amet, consectetur adipiscing elitvlorem ipsum dolor sit amet, consectetur adipiscing elitv",
    "levelType": "NATIONAL",
    "startTime": "2025-06-01T08:00:00Z",
    "endTime": "2026-06-05T20:00:00Z",
    "creator": {
        "id": 10,
        "email": "jane.doe@example.com",
        "firstName": "Jane",
        "lastName": "Doe"
    },
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
}

const users = [
    {id: 1, firstName: "Alice", lastName: "Johnson", email: "alice.johnson@example.com"},
    {id: 2, firstName: "Bob", lastName: "Smith", email: "bob.smith@example.com"},
    {id: 3, firstName: "Charlie", lastName: "Brown", email: "charlie.brown@example.com"},
    {id: 4, firstName: "Diana", lastName: "Williams", email: "diana.williams@example.com"},
    {id: 5, firstName: "Ethan", lastName: "Davis", email: "ethan.davis@example.com"},
    {id: 6, firstName: "Fiona", lastName: "Clark", email: "fiona.clark@example.com"},
    {id: 7, firstName: "George", lastName: "Miller", email: "george.miller@example.com"},
    {id: 8, firstName: "Hannah", lastName: "Taylor", email: "hannah.taylor@example.com"},
    {id: 9, firstName: "Ian", lastName: "Anderson", email: "ian.anderson@example.com"},
    {id: 10, firstName: "Julia", lastName: "Thomas", email: "julia.thomas@example.com"}
];

const VotingPage = () => {
    const [tab, setTab] = useState("Description")

    const isActive = new Date(voting.startTime) < new Date() && new Date(voting.endTime) > new Date();

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
                        {voting.description}
                    </Typography>)
            case 'Participants':
                return <UserList users={users}/>
        }
    }


    const renderSuccessSupportButton = () => {
        return (
            <Button disabled startIcon={<HowToVoteIcon color="success"/>} variant="contained"
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
                Voted
            </Button>
        )
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
                    {voting.name}
                </Typography>


                <Stack direction='column' sx={{marginBottom: 4, marginTop: 1.2, gap: 0.5, paddingX: 1}}>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <AccountCircleIcon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {voting.creator.firstName + " " + voting.creator.lastName}
                        </Typography>
                        <Divider orientation="vertical" sx={{height: 15, color: 'black', marginX: 0.5}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {voting.creator.email}
                        </Typography>

                    </Box>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <Groups2Icon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {voting.levelType === 'class' ? 'Class' : 'School'}
                        </Typography>
                    </Box>
                </Stack>

                <Stack direction="row" width={'100%'}>
                    {renderTabButton('Description', 110)}
                    {renderTabButton('Participants', 105)}
                </Stack>

                <Divider/>

                <Box paddingX={1}>
                    {renderTab()}
                </Box>


            </Box>


            <Box>
                <Box sx={{border: '1px solid #ddd', borderRadius: '5px', padding: '15px', marginX: 1}}>

                    <Typography variant='body2' fontWeight={'bold'}
                                sx={{alignItems: 'center', display: 'flex', justifyContent: 'center',}}>
                        Options
                    </Typography>

                    <Divider sx={{marginY: 0.5}}/>

                    <Box sx={{alignItems: 'center', display: 'flex', justifyContent: 'center',}}>
                        <Stack direction={'column'} width={'100%'} maxWidth={270}>
                            {voting.statistics.answers.map((answer) => (
                                <Box key={answer.id}>
                                    <VotingAnswerBox
                                        answer={answer}
                                        maxAnswerCount={voting.statistics.countAllAnswered}
                                        selectedAnswer={1}
                                        setSelectedValue={() => {}}/>
                                </Box>
                            ))}
                        </Stack>
                    </Box>
                    <Divider sx={{marginY: 0.5}}/>
                    <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Typography mt={0.3}  variant='body1' fontWeight={'bold'}>
                            Voted {voting.statistics.countAllAnswered}/{voting.statistics.countAll}
                        </Typography>

                        <VotingDate startDate={voting.startTime} endDate={voting.endTime}/>

                        <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                            {isActive ? (
                                <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                    {voting.isAnswered ? (
                                        <Box alignItems="center" display="flex" justifyContent="center" mt={2.5} mb={1}>
                                            {renderSuccessSupportButton()}
                                        </Box>
                                    ) : (
                                        <Box alignItems="center" display="flex" justifyContent="center" mt={2.5} mb={1}>
                                            <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                                                Vote
                                            </Button>
                                        </Box>
                                    )}
                                </Box>
                            ) : (
                                voting.isAnswered && (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={2.5} mb={1}>
                                        {renderSuccessSupportButton()}
                                    </Box>
                                ))}
                        </Box>
                    </Box>
                </Box>
            </Box>
        </Box>
    );
};

export default VotingPage;