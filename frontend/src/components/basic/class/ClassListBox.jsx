import React from 'react';
import {Card, Typography} from '@mui/material';
import Box from "@mui/material/Box";
import Groups2Icon from '@mui/icons-material/Groups2';
import {useNavigate} from "react-router-dom";

const ClassListBox = ({studentClass}) => {
    const navigate = useNavigate()

    return (
        <Box sx={{
            cursor: 'pointer',
            transition: "background-color 0.4s, border-color 0.4s, box-shadow 0.4s",
            "&:hover": {
                cursor: "pointer",
                bgcolor: "#f5f5f5",
                borderColor: "#c6c5c5",
                boxShadow: "0px 0px 5px rgba(0, 0, 0, 0.2)",
            },
            display:'flex',
            gap:1,
            height: 100,
            padding: [15, 4],
            border: '1px solid #ddd',
            borderRadius: '5px',
            alignItems:"center"
            }} onClick={() => {
                navigate(`${studentClass.id}`)
            }}>
            <Groups2Icon color={'primary'}></Groups2Icon>
            <Typography>{studentClass.className}</Typography>
        </Box>
    );
};

export default ClassListBox;