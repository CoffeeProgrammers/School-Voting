import React from 'react';
import {TextField} from "@mui/material";

const Search = ({label, searchQuery, setSearchQuery, sx}) => {
    return (
        <TextField
            label= {label ? label : "Search..."}
            size="small"
            variant="outlined"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            InputProps={{
                sx: {
                    height: 32,
                    padding: 0,
                    '& input': {
                        padding: '4px 8px',
                        height: 'auto',
                    },
                },
            }}
            InputLabelProps={{
                sx: {
                    top: '-4px',
                },
            }}
            sx={{
                ...sx,
            }}
        />
    );
};

export default Search;