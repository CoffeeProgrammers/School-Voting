import React, {useEffect, useState} from 'react';
import Box from "@mui/material/Box";
import UserService from "../../../services/base/ext/UserService";
import Typography from "@mui/material/Typography";
import Loading from "../../layouts/Loading";
import PersonIcon from "@mui/icons-material/Person";
import EmailIcon from "@mui/icons-material/Email";
import AttributionRoundedIcon from "@mui/icons-material/AttributionRounded";

const UserProfileBox = () => {
    const [user, setUser] = useState()

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await UserService.getMyUser()

                setUser(response)
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

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

            </Box>
        </Box>
    );
};

export default UserProfileBox;