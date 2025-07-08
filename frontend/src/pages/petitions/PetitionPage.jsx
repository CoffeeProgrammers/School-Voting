import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Divider from "@mui/material/Divider";
import Groups2Icon from '@mui/icons-material/Groups2';
import {Button, Stack} from "@mui/material";
import Utils from "../../utils/Utils";
import {blueGrey} from "@mui/material/colors";
import ThumbUpAltIcon from '@mui/icons-material/ThumbUpAlt';
import theme from "../../assets/theme";
import Loading from "../../components/layouts/Loading";
import PetitionService from "../../services/base/ext/PetitionService";
import StatisticInPage from "../../components/basic/petition/StatisticInPage";
import DeleteButton from "../../components/layouts/DeleteButton";
import {useError} from "../../contexts/ErrorContext";

const PetitionPage = () => {
    const {id} = useParams();
    const {showError} = useError()
    const navigate = useNavigate()
    const [tab, setTab] = useState("Description")

    const [petition, setPetition] = useState()
    const [viewDate, setViewDate] = useState()

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await PetitionService.getPetition(id)

                setPetition(response)
                setViewDate(Utils.getDaysLeft(response.endTime) + ' days left')
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
            await PetitionService.deletePetition(id)
            navigate('/petitions', {replace: true});
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false)
        }
    };

    const renderSuccessSupportButton = () => {
        return (
            <Button disabled startIcon={<ThumbUpAltIcon color='success'/>} variant="contained"
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
                Supported
            </Button>
        )
    }

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
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '3fr 1.3fr',
            paddingX: 5,
            paddingTop: 2,
            paddingBottom: 4
        }}>
            <Box paddingRight={4} mt={4.5}>
                <DeleteButton
                    text={'Are you sure you want to delete this petition?'}
                    deleteFunction={handleDelete}
                    fontSize={20}
                />
                <Typography variant='h4'>
                    {petition.name}
                </Typography>


                <Stack direction='column' sx={{marginBottom: 4, marginTop: 1.2, gap: 0.5, paddingX: 1}}>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <AccountCircleIcon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.creator.firstName + " " + petition.creator.lastName}
                        </Typography>
                        <Divider orientation="vertical" sx={{height: 15, color: 'black', marginX: 0.5}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.creator.email}
                        </Typography>

                    </Box>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <Groups2Icon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.levelType === 'class' ? 'Class' : 'School'}
                        </Typography>
                    </Box>
                </Stack>

                <Stack direction="row" width={'100%'}>
                    {renderTabButton('Description', 105)}
                    {renderTabButton('Comments', 100)}
                </Stack>

                <Divider/>

                <Box paddingX={1}>
                    {renderTab()}
                </Box>


            </Box>

            <Box>
                <Box sx={{border: '1px solid #ddd', borderRadius: '5px', padding: '15px', marginX: 1}}>
                    <Box sx={{alignItems: 'center', display: 'flex', justifyContent: 'center',}}>
                        <StatisticInPage
                            countSupported={petition.countSupported}
                            countNeeded={petition.countNeeded}
                            status={petition.status}
                        />

                    </Box>
                    <Box mt={0.25} sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Box display="flex" alignItems="center">
                            {Utils.getStatus(
                                petition.status,
                                {mr: 0.5, fontSize: 20},
                                {fontSize: 14.5}
                            )}
                        </Box>
                        {petition.status === 'ACTIVE' ? (
                            <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                <Typography mt={0.55}>
                                    {viewDate}
                                </Typography>
                                {petition.supportedByCurrentId ? (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                        {renderSuccessSupportButton()}
                                    </Box>
                                ) : (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                        <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                                            Support petition
                                        </Button>
                                    </Box>
                                )}

                            </Box>
                        ) : (
                            petition.supportedByCurrentId && (
                                <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                    {renderSuccessSupportButton()}
                                </Box>
                            ))}

                    </Box>

                </Box>
            </Box>
        </Box>
    );
};

export default PetitionPage;