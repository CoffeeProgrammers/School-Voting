import React, {useState} from 'react';
import Search from "../../components/layouts/list/Search";
import {Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import theme from "../../assets/theme";
import Box from "@mui/material/Box";

const PetitionsListPage = () => {
    const [searchName, setSearchName] = useState(null)
    const [status, setStatus] = useState('Active')

    const statusOptions = [
        {value: '', label: <em>None</em>},
        {value: 'ACTIVE', label: 'Active'},
        {value: 'WAITING_FOR_CONSIDERATION', label: 'Waiting for consideration'},
        {value: 'UNSUCCESSFUL', label: 'Unsuccessful'},
        {value: 'APPROVED', label: 'Approved'},
        {value: 'REJECTED', label: 'Rejected'},

    ];
    return (
        <>

            <Stack direction="row" sx={{alignItems: 'center', display: "flex", justifyContent: "space-between "}}>
                <Typography variant="h6" fontWeight={'bold'}>Petitions</Typography>
                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.5}>
                    <Search
                        label={"Title"}
                        searchQuery={searchName}
                        setSearchQuery={setSearchName}
                        sx={{mr: 1.5}}
                    />

                    <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>Create a
                        petition
                    </Button>
                </Box>

            </Stack>
            <Divider sx={{mt: 0.5}}/>
            <Stack direction="row" width={'100%'}>
                {statusOptions.map((option, index) => (
                    index !== 0 && (
                        <Button key={index} onClick={() => setStatus(option.value)} fullWidth
                                sx={{height: 40, borderRadius: 0}}>
                            <Typography color={status === option.value ? 'primary' : 'text.secondary'}
                                        sx={{
                                            borderBottom: "2.5px solid",
                                            borderBottomColor: status === option.value ? theme.palette.primary.main : "transparent",
                                        }}>
                                {option.label}
                            </Typography>
                        </Button>
                    )))}
            </Stack>
            <Divider sx={{mb: 0.75}}/>

        </>
    );
};

export default PetitionsListPage;