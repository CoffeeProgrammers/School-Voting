import React from 'react';
import Box from "@mui/material/Box";
import ClassList from "../classes/ClassList";
import UserList from "../../components/basic/user/UserList";
import SchoolPageWrapper from "../../components/basic/school/SchoolPageWrapper";

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

const SchoolPage = () => {
    const isStudent = false;
    return (
        <SchoolPageWrapper>
            <Box sx={{border: '1px solid #ddd', borderRadius: '5px', marginY: '5px', paddingTop: '15px'}}>
                {isStudent ? (
                    <UserList users={users}/>
                ) : (
                    <ClassList/>
                )}

            </Box>
        </SchoolPageWrapper>
    );
};

export default SchoolPage;