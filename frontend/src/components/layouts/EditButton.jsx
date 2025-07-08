import React from 'react';
import {IconButton} from "@mui/material";
import {useNavigate} from "react-router-dom";
import EditIcon from '@mui/icons-material/Edit';

const EditButton = ({path, state, fontSize = 20}) => {
    const navigate = useNavigate();
    return (
        <IconButton
            onClick={() => navigate(path, {state: {state}})}
            color="primary"
            aria-label="delete"
            sx={{minWidth: 'unset', width: 'auto', p: 0, fontSize: fontSize}}
        >
            <EditIcon sx={{fontSize: fontSize}}/>
        </IconButton>
    );
};

export default EditButton;