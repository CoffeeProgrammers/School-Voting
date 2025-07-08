import React, {useEffect, useRef, useState} from 'react';
import {CustomPieChart} from '../../layouts/statistics/CustomPieChart';
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';

const StatisticInPage = ({ countSupported, countNeeded, status, petitionId }) => {
    const [supportCount, setSupportCount] = useState(countSupported);
    const stompClientRef = useRef(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8081/ws');
        const stompClient = Stomp.over(socket);
        stompClientRef.current = stompClient;

        stompClient.connect({}, () => {
            stompClient.subscribe(`/topic/petitions/${petitionId}/counter`, (message) => {
                const newCount = JSON.parse(message.body);
                console.log(message);
                setSupportCount(newCount);
            });
        });

        return () => {
            if (stompClientRef.current?.connected) {
                stompClientRef.current.disconnect();
            }
        };
    }, [petitionId]);

    return (
        <div className="flex flex-col items-center gap-4">
            <CustomPieChart
                supportedCount={supportCount}
                totalCount={countNeeded}
                status={status}
            />
            <div className="text-sm text-gray-500">
                Supported by: {supportCount} from {countNeeded}
            </div>
        </div>
    );
};

export default StatisticInPage;
