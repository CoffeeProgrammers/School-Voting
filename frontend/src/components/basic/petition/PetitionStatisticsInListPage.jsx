import React, {useState} from 'react';
import Typography from "@mui/material/Typography";
import Progress from "../../layouts/statistics/Progress";
import Utils from "../../../utils/Utils";

const PetitionStatisticsInListPage = ({countSupported, countNeeded, status}) => {
    const [localCountSupported, setLocalCountSupported] = useState(countSupported)

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