import React from 'react';
import {Grid} from "@mui/material";
import ClassListBox from "./ClassListBox";
import {useNavigate} from "react-router-dom";

const ClassList = ({studentsClass}) => {
    const navigate = useNavigate();

    return (
        <Grid container rowSpacing={0.5} columnSpacing={0.5} paddingX={"10px"} pb={1} px={1.75}>
            {studentsClass.map(studentsClass => (
                <Grid size={{xs: 12, sm: 6, md: 4, lg: 6}} key={studentsClass.id}
                      onClick={() => navigate(`class/${studentsClass.id}`)}>
                    <ClassListBox studentClass={studentsClass}></ClassListBox>
                </Grid>
            ))}
        </Grid>
    );
};

export default ClassList;