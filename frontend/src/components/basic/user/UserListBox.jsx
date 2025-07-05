import React from 'react';
import Box from "@mui/material/Box";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import Typography from "@mui/material/Typography";
import {blueGrey} from "@mui/material/colors";

const UserListBox = ({user}) => {
    return (
        <Box sx={{
            display:'flex',
            gap: 0.5,
            height: 30,
            paddingX: 2,
            paddingY: 3.75,
            border: '1px solid #ddd',
            borderRadius: '5px',
            alignItems:"center",
            justifyContent:"space-between"
        }}>
            <Box sx={{display: 'flex', gap: 0.5, alignItems: 'center'}}>
                <AccountCircleIcon sx={{fontSize: 30}} color={'primary'}/>
                <Typography>{user.firstName}</Typography>
                <Typography>{user.lastName}</Typography>
            </Box>
            <Typography sx={{color: blueGrey[500]}}>{user.email}</Typography>
        </Box>
    );
};

export default UserListBox;