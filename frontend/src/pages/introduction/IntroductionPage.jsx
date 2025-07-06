import React from 'react';
import Box from '@mui/material/Box';
import {Button, Container, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import PsychologyAltIcon from '@mui/icons-material/PsychologyAlt';

const IntroductionPage = () => {
    return (
        <Container  sx={{height:500, border:"1px solid black", padding:0}}>
            <Box sx={{display:'flex', alignItems:'center', justifyContent:"space-between", padding:"10px 0"}}>
                <Stack sx={{display:'flex', flexDirection:'row' , alignItems:'center'}}>
                    <PsychologyAltIcon color='primary' sx={{mt: '3px', mr: '6px', fontSize: '35px'}}/>
                    <Typography variant="h6" fontWeight='bold' noWrap>
                        School Governance
                    </Typography>
                </Stack>
                <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                    Login
                </Button>
            </Box>
            <Box>
                <Box></Box>
            </Box>
        </Container>
    );
};

export default IntroductionPage;