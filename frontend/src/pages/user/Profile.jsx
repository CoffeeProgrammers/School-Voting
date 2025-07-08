import React from 'react';
import Box from "@mui/material/Box";
import UserAdditionBox from "../../components/basic/profile/UserAdditionBox";
import UserProfileBox from "../../components/basic/profile/UserProfileBox";

const Profile = () => {


    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '25.5% 70%',
            paddingX: 1,
            paddingTop: 2,
            paddingBottom: 4,
            gap: 3.5,
        }}>
            <UserProfileBox/>
            <UserAdditionBox/>
        </Box>
    );
};

export default Profile;