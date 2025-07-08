import React, {useEffect, useState} from 'react';
import Box from "@mui/material/Box";
import UserService from "../../../services/base/ext/UserService";
import Typography from "@mui/material/Typography";
import Loading from "../../layouts/Loading";
import PersonIcon from "@mui/icons-material/Person";
import EmailIcon from "@mui/icons-material/Email";
import AttributionRoundedIcon from "@mui/icons-material/AttributionRounded";
import Divider from "@mui/material/Divider";
import {Button} from "@mui/material";
import GoogleAuthService from "../../../services/base/ext/GoogleAuthService";
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import EventBusyIcon from '@mui/icons-material/EventBusy';
import EditButton from "../../layouts/EditButton";
import {useSearchParams} from "react-router";
import {useError} from "../../../contexts/ErrorContext";

const UserProfileBox = () => {
    const [user, setUser] = useState()

    const [searchParams] = useSearchParams();
    const { showError } = useError();

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isConnected, setIsConnected] = useState(false);
    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await UserService.getMyUser()
                const response2 = await GoogleAuthService.isConnected()

                setUser(response)
                setIsConnected(response2)
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);
    useEffect(() => {
        const urlError = searchParams.get('error');
        if (urlError === 'already_linked') {
            showError({message: "This Google account is already linked to another user."});
        }
    }, [searchParams, showError]);

    const handleConnect = async () => {
        try {
            const response = await GoogleAuthService.connect();
            window.location.href = response;
            setIsConnected(true);
        } catch (error) {
            console.error("Failed to connect Google Calendar:", error);
        }
    };

    const handleDisconnect = async () => {
        try {
            await GoogleAuthService.revoke();
            setIsConnected(false);
        } catch (error) {
            console.error("Failed to disconnect Google Calendar:", error);
        }
    };


    if (loading) {
        return <Loading/>;
    }

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box>
            <Box sx={{
                border: '1px solid #ddd',
                borderRadius: '5px',
                padding: '17px',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center'
            }}>
                <Box sx={{
                    height: 180,
                    width: 180,
                    padding: 0,
                    marginBottom: "10px",
                    borderRadius: "100%",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    border: 3,
                    borderColor: 'primary.main'
                }}>
                    <PersonIcon color="primary" sx={{fontSize: 180, padding: 0, margin: 0}}/>
                </Box>
                <Box display="flex" justifyContent={'left'} my={0.3}>
                    <EditButton path={'update'} state={user}/>
                </Box>
                <Typography variant='h6' fontWeight='bold'>{user.firstName + " " + user.lastName}</Typography>

                <Box sx={{display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 0.375, mx: 0.5}}>
                    <EmailIcon sx={{fontSize: 20, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 14.75,}}>
                        {user.email}
                    </Typography>
                </Box>

                <Box sx={{display: 'flex', alignItems: 'center', gap: 0.375, mx: 0.5, mr: 1.5}}>
                    <AttributionRoundedIcon sx={{fontSize: 22, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 14.75,}}>
                        {user.role}
                    </Typography>
                </Box>

                <Divider sx={{width:'100%', my: 1.5}}/>

                <Box sx={{display: 'flex', alignItems: 'center', gap: 0.375}}>
                    {isConnected ?
                        <Button variant="contained" color="primary" sx={{ height: 32, borderRadius: 10 }} onClick={handleDisconnect}>

                            <EventBusyIcon sx={{mr: 0.5}}/> Disconnect Google Calendar
                        </Button> :
                        <Button variant="contained" color="primary" sx={{ height: 32, borderRadius: 10 }} onClick={handleConnect}>
                            <EventAvailableIcon sx={{mr: 0.5}}/>
                            <Typography variant={'body2'} noWrap>Connect Google Calendar </Typography>
                        </Button>}
                </Box>

            </Box>
        </Box>
    );
};

export default UserProfileBox;