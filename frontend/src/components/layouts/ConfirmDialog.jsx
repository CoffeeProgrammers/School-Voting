import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import PropTypes from "prop-types";
import * as React from "react";
import Typography from "@mui/material/Typography";

ConfirmDialog.propTypes = {
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
    text: PropTypes.string.isRequired,
    someFunction: PropTypes.func.isRequired
};

function ConfirmDialog({onClose, open, text, someFunction}) {
    const handleConfirm = () => {
        someFunction();
        onClose();
    };

    const handleClose = () => {
        onClose();
    };

    return (
        <Dialog onClose={handleClose} open={open} maxWidth="xs" fullWidth>
            <DialogTitle style={{backgroundColor: '#fff', color: '#000'}}/>
            <DialogContent style={{backgroundColor: '#fff', textAlign: 'center'}}>
                <Box
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                    height="100%"
                    color="#000"
                >
                    <Typography variant="body1" component="h3" gutterBottom>
                        {text}
                    </Typography>
                    <Box display="flex" justifyContent="center" alignItems="center">
                        <Button
                            onClick={handleClose}
                            variant="contained"
                            color="primary"
                            style={{
                                marginRight: '10px'
                            }}
                        >
                            No
                        </Button>
                        <Button
                            onClick={handleConfirm}
                            variant="contained"
                            color="error"
                            style={{
                                marginLeft: '10px'
                            }}
                        >
                            Yes
                        </Button>
                    </Box>
                </Box>
            </DialogContent>
        </Dialog>
    );
}

export default ConfirmDialog;