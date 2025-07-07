import React, {useState} from 'react';
import {CustomPieChart} from "../../layouts/statistics/CustomPieChart";

const StatisticInPage = ({countSupported, countNeeded, status}) => {
    const [localCountSupported, setLocalCountSupported] = useState(countSupported)


    return (
        <CustomPieChart
            supportedCount={localCountSupported}
            totalCount={countNeeded}
            status={status}
        />
    );
};

export default StatisticInPage;