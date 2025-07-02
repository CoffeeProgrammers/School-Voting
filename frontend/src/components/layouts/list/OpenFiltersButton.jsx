import React from 'react';
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import {IconButton} from "@mui/material";

const OpenFiltersButton = ({setOpenFilterMenu, isOpenFilterMenu}) => {
    return (
        <IconButton
            sx={{borderRadius: "25px"}}
            onClick={() => setOpenFilterMenu(!isOpenFilterMenu)}
        >
            <FilterAltIcon
                fontSize="medium"
            />
        </IconButton>
    );
};

export default OpenFiltersButton;