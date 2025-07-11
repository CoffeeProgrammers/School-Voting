import React, {useEffect, useState} from 'react';
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
import VotingService from "../../services/base/ext/VotingService";
import {useNavigate, useParams} from "react-router-dom";
import Loading from "../../components/layouts/Loading";
import VotingParticipantsList from "../../components/basic/user/VotingParticipantsList";
import DeleteButton from "../../components/layouts/DeleteButton";
import {useError} from "../../contexts/ErrorContext";
import EditButton from "../../components/layouts/EditButton";
import Cookies from "js-cookie";


const VotingPage = () => {
    const {id} = useParams();
    const [tab, setTab] = useState("Description")
    const {showError} = useError()
    const navigate = useNavigate()

    const [selectedAnswer, setSelectedAnswer] = useState(-1)

    const [voting, setVoting] = useState()
    const [isActive, setIsActive] = useState()

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await VotingService.getVoting(id)

                console.log(response)
                setVoting(response)
                setSelectedAnswer(response.myAnswerId ? response.myAnswerId : -1)
                setIsActive(new Date(response.startTime) < new Date() && new Date(response.endTime) > new Date())
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);


    const handleDelete = async () => {
        try {
            setLoading(true)
            await VotingService.deleteVoting(id)
            navigate('/voting', {replace: true});
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false)
        }
    };

    const vote = async () => {
        try {
            setLoading(true)
            const updatedVoting = await VotingService.vote(voting.id, selectedAnswer)
            console.log(updatedVoting)
            setVoting(updatedVoting)
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false)
        }
    };

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
                return <VotingParticipantsList/>
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

    if (loading) {
        return <Loading/>;
    }

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
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
                {Cookies.get("userId") === voting.creator.id.toString() && new Date(voting.startTime) > new Date() ?
                    (<Box display="flex" alignItems="center" gap={1}>
                        <DeleteButton
                            text={'Are you sure you want to delete this voting?'}
                            deleteFunction={handleDelete}
                            fontSize={20}
                        />

                        <EditButton path={'update'} state={voting}/>
                    </Box>) : ""}
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
                            {voting.levelType.toLowerCase()}
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
                                        maxAnswerCount={voting.statistics.countAnswered}
                                        myAnswer={voting.myAnswerId}
                                        selectedAnswer={selectedAnswer}
                                        setSelectedValue={setSelectedAnswer}/>
                                </Box>
                            ))}
                        </Stack>
                    </Box>
                    <Divider sx={{marginY: 0.5}}/>
                    <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Typography mt={0.3} variant='body1' fontWeight={'bold'}>
                            Voted {voting.statistics.countAnswered}/{voting.statistics.countAll}
                        </Typography>

                        <VotingDate startDate={voting.startTime} endDate={voting.endTime}/>

                        {voting.myAnswerId !== null ?
                            <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                {isActive ? (
                                    <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                        {Number(voting.myAnswerId) !== -1 ? (
                                            <Box alignItems="center" display="flex" justifyContent="center" mt={2.5}
                                                 mb={1}>
                                                {renderSuccessSupportButton()}
                                            </Box>
                                        ) : (
                                            <Box alignItems="center" display="flex" justifyContent="center" mt={2.5}
                                                 mb={1}>
                                                <Button disabled={selectedAnswer === -1} onClick={() => vote()}
                                                        variant="contained" color="primary"
                                                        sx={{height: 32, borderRadius: 10}}>
                                                    Vote
                                                </Button>
                                            </Box>
                                        )}
                                    </Box>
                                ) : Number(voting.myAnswerId) !== -1 ? (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={2.5}
                                         mb={1}>
                                        {renderSuccessSupportButton()}
                                    </Box>) : ""}
                            </Box> : ""}
                        {/*<Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>*/}
                        {/*    {isActive ? (*/}
                        {/*        <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>*/}
                        {/*            {voting.myAnswerId ? (*/}
                        {/*                <Box alignItems="center" display="flex" justifyContent="center" mt={2.5} mb={1}>*/}
                        {/*                    {renderSuccessSupportButton()}*/}
                        {/*                </Box>*/}
                        {/*            ) : (*/}
                        {/*                <Box alignItems="center" display="flex" justifyContent="center" mt={2.5} mb={1}>*/}
                        {/*                    <Button disabled={selectedAnswer === -1} onClick={() => vote()}*/}
                        {/*                            variant="contained" color="primary"*/}
                        {/*                            sx={{height: 32, borderRadius: 10}}>*/}
                        {/*                        Vote*/}
                        {/*                    </Button>*/}
                        {/*                </Box>*/}
                        {/*            )}*/}
                        {/*        </Box>*/}
                        {/*    ) : (*/}
                        {/*        voting.myAnswerId && (*/}
                        {/*            <Box alignItems="center" display="flex" justifyContent="center" mt={2.5} mb={1}>*/}
                        {/*                {renderSuccessSupportButton()}*/}
                        {/*            </Box>*/}
                        {/*        ))}*/}
                        {/*</Box>*/}

                    </Box>
                </Box>
            </Box>
        </Box>
    )
        ;
};

export default VotingPage;