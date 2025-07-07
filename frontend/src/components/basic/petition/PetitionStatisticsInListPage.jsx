import React, {useEffect, useRef, useState} from 'react';
import Typography from "@mui/material/Typography";
import Progress from "../../layouts/statistics/Progress";
import Utils from "../../../utils/Utils";
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';

const PetitionStatisticsInListPage = ({ countSupported, countNeeded, status, petitionId }) => {
    const [localCountSupported, setLocalCountSupported] = useState(countSupported);
    const stompClientRef = useRef(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8081/ws');
        const stompClient = Stomp.over(socket);
        stompClientRef.current = stompClient;

        stompClient.connect({}, () => {
            stompClient.subscribe(`/topic/petitions/${petitionId}/counter`, (message) => {
                const newCount = JSON.parse(message.body);
                console.log('WS message received:', newCount);
                setLocalCountSupported(newCount);
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
            <Typography variant='h5' mb={0.5} fontWeight="bold">
                {localCountSupported}
            </Typography>

            <Progress
                color={Utils.getStatusMUIColor(status)}
                count={localCountSupported}
                maxCount={countNeeded}
            />
        </>
    );
};

export default PetitionStatisticsInListPage;
