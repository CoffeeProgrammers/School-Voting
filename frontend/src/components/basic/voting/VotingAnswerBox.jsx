import React from 'react';
import Radio from "@mui/material/Radio";
import {Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Progress from "../../layouts/statistics/Progress";
import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";

const VotingAnswerBox = ({answer, maxAnswerCount, selectedAnswer, setSelectedValue}) => {

    const handleChange = (event) => {
        setSelectedValue(event.target.value);
    };

    return (
        <Stack direction='row' width={'100%'}>
            <Radio
                checked={selectedAnswer === answer.id}
                onChange={handleChange}
                value={answer.id}
                name="radio-buttons"
            />
            <Stack direction='column' width={'100%'} mt={0.8}>
                <Box sx={{display: 'flex', justifyContent: 'space-between'}}>
                    <Typography noWrap variant={"body2"}>{answer.name}</Typography>
                    <Typography mr={1.5} variant={"body2"}>{((answer.count * 100) / maxAnswerCount).toFixed(1)}%</Typography>

                </Box>

                <Progress count={answer.count} maxCount={maxAnswerCount} color={"success"}/>
            </Stack>
        </Stack>
    );
};

export default VotingAnswerBox;