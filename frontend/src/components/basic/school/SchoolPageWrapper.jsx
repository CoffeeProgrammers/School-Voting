import React from 'react';
import Box from "@mui/material/Box";
import SchoolBox from "./SchoolBox";

const SchoolPageWrapper = ({children}) => {

    const school = {
        "id": 1,
        "name": "Lyceum N6 named after Ivan Revchyk",
        "director": {
            "id": 1,
            "firstName": "John",
            "lastName": "Doe",
            "email": "john.doe@gmail.com"
        }
    }

    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '27.5% 70%',
            paddingX: 1,
            paddingTop: 2,
            paddingBottom: 4,
            gap: 3.5,
        }}>
            <SchoolBox school={school}/>

            {children}
        </Box>
    );
};

export default SchoolPageWrapper;