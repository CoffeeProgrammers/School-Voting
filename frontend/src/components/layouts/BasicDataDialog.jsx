import React from 'react';
import DialogTitle from "@mui/material/DialogTitle";
import {DialogActions, Divider, IconButton} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import {styled} from "@mui/material/styles";
import Dialog from "@mui/material/Dialog";
import CloseIcon from "@mui/icons-material/Close";

const BootstrapDialog = styled(Dialog)(({theme}) => ({
    '& .MuiDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuiDialogActions-root': {
        padding: theme.spacing(1),
    },
}));

const BasicDataDialog = (
    {
        open,
        handleClose,
        size,
        title,
        children,
        actions
    }) => {

    return (
        <>
            <BootstrapDialog
                onClose={handleClose}
                open={open}
                maxWidth={size}
                fullWidth
            >
                <DialogTitle sx={{m: 0, p: 2}}>
                    {title}
                </DialogTitle>

                <IconButton
                    onClick={handleClose}
                    sx={(theme) => ({
                        position: 'absolute',
                        right: 8,
                        top: 8,
                        color: theme.palette.grey[500],
                    })}
                >
                    <CloseIcon/>
                </IconButton>

                <Divider/>

                <DialogContent>
                    {children}
                </DialogContent>

                <Divider sx={{mb: 2}}/>

                <DialogActions>
                    {actions}
                </DialogActions>
            </BootstrapDialog>
        </>
    );
};

export default BasicDataDialog;