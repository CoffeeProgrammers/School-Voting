import React, {useEffect, useRef, useState} from 'react';
import Typography from "@mui/material/Typography";
import Progress from "../../layouts/statistics/Progress";
import Utils from "../../../utils/Utils";
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import Box from "@mui/material/Box";

const PetitionStatisticsInListPage = ({ countSupported, countNeeded, status, petitionId }) => {
    const [localCountSupported, setLocalCountSupported] = useState(countSupported);
    const stompClientRef = useRef(null);
    const [localStatus, setLocalStatus] = useState(status);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8081/ws');
        const stompClient = Stomp.over(socket);
        stompClientRef.current = stompClient;

        stompClient.connect({}, () => {
            stompClient.subscribe(`/topic/petitions/${petitionId}/counter`, (message) => {
                const body = JSON.parse(message.body);
                console.log('WS message received:', body);
                setLocalCountSupported(body.count.toString());
                setLocalStatus(body.status);
            });
        });

        return () => {
            if (stompClientRef.current?.connected) {
                stompClientRef.current.disconnect();
            }
        };
    }, [petitionId]);

    return (
        <>
            <Box mt={2}>
            <Typography variant='h5' mb={0.5} fontWeight="bold">
                {localCountSupported}
            </Typography>

            <Progress
                color={Utils.getStatusMUIColor(localStatus)}
                count={localCountSupported}
                maxCount={countNeeded}
            />
            </Box>
            <Box mt={0.75} display="flex" alignItems="center">
                {Utils.getStatus(localStatus, {mr: 0.25, fontSize: 18}, {fontSize: 13})}
            </Box>
        </>
    );
};

export default PetitionStatisticsInListPage;
