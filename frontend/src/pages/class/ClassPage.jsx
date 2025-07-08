import React from 'react';
import {Box} from "@mui/material";
import {useNavigate, useParams} from "react-router-dom";
import SchoolPageWrapper from "../../components/basic/school/SchoolPageWrapper";
import ClassBox from "../../components/basic/class/ClassBox";

const ClassPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();

    return (
        <SchoolPageWrapper>

            <Box sx={{border: '1px solid #ddd', borderRadius: '5px', marginY: '5px' }}>
                <ClassBox isMy={false} classId={id}/>
            </Box>
        </SchoolPageWrapper>
    );
};

export default ClassPage;