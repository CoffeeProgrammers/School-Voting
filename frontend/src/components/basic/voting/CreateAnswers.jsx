import React, {useState} from 'react';
import Box from "@mui/material/Box";
import {IconButton, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import Divider from "@mui/material/Divider";
import CancelIcon from '@mui/icons-material/Cancel';

const CreateAnswers = ({answersList, addAnswer, deleteAnswer,}) => {
    const [newAnswer, setNewAnswer] = useState('')
    return (
        <Box display={"flex"} flexDirection={"column"} gap={1}>
            <Box display={"flex"} alignItems={"center"}>
                <TextField
                    multiline
                    label="Answer"
                    fullWidth
                    variant="outlined"
                    value={newAnswer}
                    onChange={(e) => setNewAnswer(e.target.value)}
                />

                <IconButton disabled={newAnswer === ''} fontSize={'medium'} onClick={() => {
                    addAnswer(newAnswer)
                    setNewAnswer('')
                }}>
                    <AddCircleIcon fontSize={'medium'} color="primary"/>
                </IconButton>
            </Box>
            <Box display={'flex'} flexDirection={'column'} gap={1} px={2} mr={4.5}>
                <Divider/>
                {answersList.map((answer, index) => (<>
                    <Box display={"flex"} alignItems={"center"} justifyContent={'space-between'} gap={1} key={index}>

                        <Typography px={1} variant={"body2"}>{answer}</Typography>
                        <IconButton onClick={() => deleteAnswer(index)} size="small">
                            <CancelIcon color="primary"/>
                        </IconButton>
                    </Box>
                    <Divider/>
                </>))}
            </Box>


        </Box>
    );
};

export default CreateAnswers;