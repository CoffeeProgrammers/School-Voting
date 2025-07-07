import React from 'react';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {Chip} from "@mui/material";
import Utils from "../../../utils/Utils";
import {useNavigate} from "react-router-dom";
import ThumbUpAltIcon from "@mui/icons-material/ThumbUpAlt";
import {blueGrey} from "@mui/material/colors";
import PetitionStatisticsInListPage from "./PetitionStatisticsInListPage";

const PetitionListBox = ({petition}) => {
    const navigate = useNavigate();

    const date = Utils.getDaysLeft(petition.endTime);
    const viewDate = date < 0 ? 'Expired' : date + " days left";

    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '2fr 1fr',
            borderBottom: '1px solid #ddd',
            paddingX: '15px',
            paddingTop: '7px',
            paddingBottom: '15px',
            cursor: 'pointer',
            transition: "background-color 0.2s, border-color 0.2s, box-shadow 0.2s",
            "&:hover": {
                cursor: "pointer",
                bgcolor: "#f5f5f5",
                borderColor: "#c6c5c5",
                boxShadow: "0px 0px 5px rgba(0, 0, 0, 0.2)",
            },
            '&:last-child': {
                borderBottom: 'none',

            },
            '&:first-child': {
                borderTop: '1px solid #ddd',
            },
        }}
             onClick={() => {
                 navigate(`/petitions/${petition.id}`)
             }}
        >
            <Box mt={0.5}>
                <Typography color='text.secondary' variant='body2'>
                    {"#" + petition.levelType}
                </Typography>

                <Typography variant='h5'>
                    {petition.name}
                </Typography>
            </Box>
            <Box>
                <Box mt={2}>
                    <PetitionStatisticsInListPage
                        countSupported={petition.countSupported}
                        countNeeded={petition.countNeeded}
                        status={petition.status}
                        petitionId={petition.id}
                    />

                </Box>
                <Box mt={0.75} display="flex" alignItems="center">
                    {Utils.getStatus(petition.status, {mr: 0.25, fontSize: 18}, {fontSize: 13})}
                </Box>
                <Box mt={1.5} display="flex" justifyContent="space-between" alignItems="center">
                    {petition.status === 'ACTIVE' ? (
                        <Typography variant='body2'>
                            {viewDate}
                        </Typography>
                    ) : (
                        <Box/>
                    )}
                    {petition.supportedByCurrentId ? (
                        <Chip
                            icon={<ThumbUpAltIcon color="success"/>}
                            label="Supported"
                            size="small"
                            sx={{color: 'success.main', backgroundColor: blueGrey[50], ml: 1, mr: 2}}
                        />
                    ) : <Box mt={1.5}/>}
                </Box>

            </Box>
        </Box>
    );
};

export default PetitionListBox;