import React, {useEffect, useRef, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Divider from "@mui/material/Divider";
import Groups2Icon from '@mui/icons-material/Groups2';
import {Button, Stack} from "@mui/material";
import {blueGrey} from "@mui/material/colors";
import ThumbUpAltIcon from '@mui/icons-material/ThumbUpAlt';
import theme from "../../assets/theme";
import Loading from "../../components/layouts/Loading";
import PetitionService from "../../services/base/ext/PetitionService";
import DeleteButton from "../../components/layouts/DeleteButton";
import {useError} from "../../contexts/ErrorContext";
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import {CustomPieChart} from "../../components/layouts/statistics/CustomPieChart";
import Utils from "../../utils/Utils";
import Cookies from "js-cookie";

const PetitionPage = () => {
        const {id} = useParams();
        const {showError} = useError()
        const navigate = useNavigate()
        const [tab, setTab] = useState("Description")

        const [petition, setPetition] = useState()
        const [viewDate, setViewDate] = useState();
        const [loading, setLoading] = useState(true);
        const [error, setError] = useState(null);
        const [supportCount, setSupportCount] = useState(0);
        const [petitionStatus, setPetitionStatus] = useState(null);

        const stompClientRef = useRef(null);
        const role = Cookies.get('role')

        useEffect(() => {
            const fetchData = async () => {
                setLoading(true);
                try {
                    const response = await PetitionService.getPetition(id);
                    console.log(response)
                    setPetition(response);
                    setSupportCount(response.countSupported);
                    setPetitionStatus(response.status);
                    setViewDate(Utils.getDaysLeft(response.endTime) + ' days left');
                } catch (error) {
                    setError(error);
                } finally {
                    setLoading(false);
                }
            };

            fetchData();
        }, [id]);

        useEffect(() => {
            if (!petition) return;

            const socket = new SockJS('http://localhost:8081/ws');
            const stompClient = Stomp.over(socket);
            stompClientRef.current = stompClient;

            stompClient.connect({}, () => {
                stompClient.subscribe(`/topic/petitions/${petition.id}/counter`, (message) => {
                    const body = JSON.parse(message.body);
                    setSupportCount(body.count.toString());
                    setPetitionStatus(body.status);
                });
            });

            return () => {
                if (stompClientRef.current?.connected) {
                    stompClientRef.current.disconnect();
                }
            };
        }, [petition]);

        const handleDelete = async () => {
            try {
                setLoading(true)
                await PetitionService.deletePetition(id)
                navigate('/petitions', {replace: true});
            } catch (error) {
                showError(error);
            } finally {
                setLoading(false)
            }
        };

        const support = async () => {
            try {
                setLoading(true)
                await PetitionService.supportPetition(petition.id)
                setPetition({...petition, supportedByCurrentId: true})
            } catch (error) {
                showError(error);
            } finally {
                setLoading(false)
            }
        };

        const approve = async () => {
            try {
                setLoading(true)
                await PetitionService.approvePetition(petition.id)
            } catch (error) {
                showError(error);
            } finally {
                setLoading(false)
            }
        };

        const reject = async () => {
            try {
                setLoading(true)
                await PetitionService.rejectPetition(petition.id)
            } catch (error) {
                showError(error);
            } finally {
                setLoading(false)
            }
        };

        const renderSuccessSupportButton = () => (
            <Button
                disabled
                startIcon={<ThumbUpAltIcon color="success"/>}
                variant="contained"
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
                }}
            >
                Supported
            </Button>
        );

        const renderTabButton = (title, width) => (
            <Button onClick={() => setTab(title)} sx={{height: 33, borderRadius: 0, width}}>
                <Typography
                    variant="body1"
                    color={tab === title ? "primary" : "text.secondary"}
                    sx={{
                        borderBottom: "2.5px solid",
                        borderBottomColor: tab === title ? theme.palette.primary.main : "transparent",
                    }}
                >
                    {title}
                </Typography>
            </Button>
        );

        const renderTab = () => {
            switch (tab) {
                case 'Description':
                    return (
                        <Typography variant='body1' mt={1.25}>
                            {petition.description}
                        </Typography>)
                case 'Comments':
                    return <Box mt={2.5}>Comments</Box>
            }
        }

        if (loading) {
            return <Loading/>;
        }

        if (error) {
            return <Typography color={"error"}>Error: {error.message}</Typography>;
        }

        return (
            <Box
                sx={{
                    display: 'grid',
                    gridTemplateColumns: '3fr 1.3fr',
                    paddingX: 5,
                    paddingTop: 2,
                    paddingBottom: 4
                }}
            >
                <Box paddingRight={4} mt={4.5}>
                    {Cookies.get("userId") === petition.creator.id.toString() ?
                        (<DeleteButton
                            text={'Are you sure you want to delete this petition?'}
                            deleteFunction={handleDelete}
                            fontSize={20}
                        />) : ""}


                    <Typography variant='h4'>
                        {petition.name}
                    </Typography>

                    <Stack direction="column" sx={{mb: 4, mt: 1.2, gap: 0.5, px: 1}}>
                        <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5}}>
                            <AccountCircleIcon sx={{fontSize: 20, color: 'primary'}}/>
                            <Typography color="primary" sx={{fontSize: 13}}>
                                {petition.creator.firstName} {petition.creator.lastName}
                            </Typography>
                            <Divider orientation="vertical" sx={{height: 15, mx: 0.5}}/>
                            <Typography color="primary" sx={{fontSize: 13}}>
                                {petition.creator.email}
                            </Typography>

                        </Box>
                        <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5}}>
                            <Groups2Icon sx={{fontSize: 20, color: 'primary'}}/>
                            <Typography color="primary" sx={{fontSize: 13}}>
                                {petition.levelType.toLowerCase()}
                            </Typography>
                        </Box>
                    </Stack>

                    <Stack direction="row" width="100%">
                        {renderTabButton('Description', 105)}
                        {renderTabButton('Comments', 100)}
                    </Stack>

                    <Divider/>

                    <Box px={1}>{renderTab()}</Box>
                </Box>

                <Box>
                    <Box sx={{border: '1px solid #ddd', borderRadius: '5px', p: '15px', mx: 1}}>
                        <Box sx={{display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                            <CustomPieChart
                                supportedCount={supportCount}
                                totalCount={petition.countNeeded}
                                status={petitionStatus}
                            />
                        </Box>

                        <Box mt={0.25} sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                            <Box display="flex" alignItems="center">
                                {Utils.getStatus(
                                    petitionStatus,
                                    {mr: 0.5, fontSize: 20},
                                    {fontSize: 14.5}
                                )}
                            </Box>

                            {petitionStatus === 'ACTIVE' && role === 'STUDENT' ? (
                                <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                    <Typography mt={0.55}>{viewDate}</Typography>
                                    {petition.supportedByCurrentId ? (
                                        <Box mt={5} mb={1.5} display="flex" justifyContent="center" alignItems="center">
                                            {renderSuccessSupportButton()}
                                        </Box>
                                    ) : (
                                        <Box mt={5} mb={1.5} display="flex" justifyContent="center" alignItems="center">
                                            <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}
                                                    onClick={support}>
                                                Support petition
                                            </Button>
                                        </Box>
                                    )}
                                </Box>
                            ) : (petition.supportedByCurrentId && (
                                    <Box mt={5} mb={1.5} display="flex" justifyContent="center" alignItems="center">
                                        {renderSuccessSupportButton()}
                                    </Box>
                                )
                            )}
                            {role === 'DIRECTOR' && petitionStatus === 'WAITING_FOR_CONSIDERATION' ?
                            <Box mt={0.25} sx={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}>
                                <Button variant="contained" color="success" sx={{height: 32, borderRadius: 10, mx: 1}}
                                        onClick={approve}>
                                    Approve
                                </Button>
                                <Button variant="contained" color="error" sx={{height: 32, borderRadius: 10, mx: 1}}
                                        onClick={reject}>
                                    Reject
                                </Button>
                            </Box> : ""}
                        </Box>
                    </Box>
                </Box>
            </Box>
        );
    }
;

export default PetitionPage;
