import React from 'react';
import {Box} from "@mui/material";
import UserList from "../../components/basic/user/UserList";
import {Link as RouterLink, useNavigate, useParams} from "react-router-dom";
import SchoolPageWrapper from "../../components/basic/school/SchoolPageWrapper";
import Link from "@mui/material/Link";
import Typography from "@mui/material/Typography";
import {grey} from "@mui/material/colors";
import Divider from "@mui/material/Divider";

const ClassPage = () => {
    const navigate = useNavigate();

    const users = [
        {id: 1, firstName: "Alice", lastName: "Johnson", email: "alice.johnson@example.com"},
        {id: 2, firstName: "Bob", lastName: "Smith", email: "bob.smith@example.com"},
        {id: 3, firstName: "Charlie", lastName: "Brown", email: "charlie.brown@example.com"},
        {id: 4, firstName: "Diana", lastName: "Williams", email: "diana.williams@example.com"},
        {id: 5, firstName: "Ethan", lastName: "Davis", email: "ethan.davis@example.com"},
        {id: 6, firstName: "Fiona", lastName: "Clark", email: "fiona.clark@example.com"},
        {id: 7, firstName: "George", lastName: "Miller", email: "george.miller@example.com"},
        {id: 8, firstName: "Hannah", lastName: "Taylor", email: "hannah.taylor@example.com"},
        {id: 9, firstName: "Ian", lastName: "Anderson", email: "ian.anderson@example.com"},
        {id: 10, firstName: "Julia", lastName: "Thomas", email: "julia.thomas@example.com"}
    ];

    const {id} = useParams();

    return (
        <SchoolPageWrapper>

            <Box sx={{border: '1px solid #ddd', borderRadius: '5px', marginY: '5px' }}>
                <Box ml={1.5} mt={0.75}>
                    <Link component={RouterLink} to={'/school'} >
                        <Typography variant={'caption'}>← go back </Typography>
                    </Link>
                </Box>
                <Box>
                    <Box sx={{paddingX: '15px', mb: 0.55}}>
                        <Typography variant={"h6"} fontWeight={'bold'}>My class: 11-A</Typography>
                    </Box>

                    <Divider/>
                    <UserList users={users}/>
                </Box>
            </Box>
        </SchoolPageWrapper>
    );
};

export default ClassPage;