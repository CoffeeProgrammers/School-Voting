import React from 'react';
import Box from "@mui/material/Box";
import {Button} from "@mui/material";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";

const FormWrapper = ({children, label, onCreate}) => {
    return (
        <Box sx={{
            display: 'flex',
            justifyContent: 'center',
        }}>
            <Box sx={{
                border: '1px solid #ddd',
                borderRadius: '5px',
                paddingY: '10px',
                width: '100%',
                maxWidth: '650px',
                mb: 5
            }}>
                <Box paddingX={"15px"} display={"flex"} justifyContent={"space-between"} alignItems={"center"}>
                    <Typography variant={"h6"}>{label}</Typography>

                    <Button onClick={onCreate} variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                        Save
                    </Button>
                </Box>

                <Divider sx={{mt: 0.5, mb: 1}}/>
                <Box paddingX={"15px"} display={"flex"} flexDirection={"column"} gap={1}>
                    {children}
                </Box>

            </Box>
        </Box>
    );
};

export default FormWrapper;