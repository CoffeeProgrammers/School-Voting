import * as React from 'react';
import ConfirmDialog from "./ConfirmDialog";
import {IconButton} from "@mui/material";
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';


export default function DeleteButton({text, deleteFunction, fontSize = 19}) {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <>
            <IconButton
                onClick={() => handleClickOpen()}
                color="error"
                aria-label="delete"
                sx={{minWidth: 'unset', width: 'auto', p: 0, fontSize: fontSize}}
            >
                <DeleteForeverIcon sx={{fontSize: fontSize}}/>
            </IconButton>
            <ConfirmDialog
                onClose={handleClose}
                open={open}
                text={text}
                someFunction={deleteFunction}

            />
        </>
    );
}


