import React from 'react';
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import HomeWorkIcon from "@mui/icons-material/HomeWork";
import {blueGrey} from "@mui/material/colors";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import EmailIcon from "@mui/icons-material/Email";

const SchoolBox = ({school}) => {
    return (
        <Box sx={{
            border: '1px solid',
            borderColor: blueGrey[100],
            borderRadius: '5px',
            backgroundColor: blueGrey[50],
            paddingX: '20px',
            paddingY: '30px',
            paddingBottom: "15px",
            widths: '100%',
        }}>

            <Box mb={1} sx={{display: 'flex', flexDirection: 'column', alignItems: 'center',}}>
                <HomeWorkIcon color='primary' sx={{fontSize: 140,}}/>
                <Typography variant='h4' fontWeight='bold'>School</Typography>
            </Box>

            <Box marginX={2}>
                <Typography sx={{fontSize: 15, display: 'flex', justifyContent: 'center'}}
                            fontWeight='bold'>Name:</Typography>
                <Box sx={{display: 'flex', gap: 0.375}}>
                    <HomeWorkIcon sx={{fontSize: 18, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 15}}>{school.name}</Typography>
                </Box>
                <Typography sx={{fontSize: 15, display: 'flex', justifyContent: 'center'}}
                            fontWeight='bold'> Director:</Typography>
                <Box sx={{display: 'flex', gap: 0.375}}>
                    <AccountCircleIcon sx={{fontSize: 20, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 15}}>
                        {school.director.firstName + " " + school.director.lastName}
                    </Typography>

                </Box>

                <Box sx={{display: 'flex', gap: 0.375}}>
                    <EmailIcon sx={{fontSize: 20, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 15,}}>
                        {school.director.email}
                    </Typography>
                </Box>
            </Box>
        </Box>
    );
};

export default SchoolBox;