import React, {useEffect, useState} from 'react';
import Divider from "@mui/material/Divider";
import {Stack} from "@mui/material";
import Box from "@mui/material/Box";
import PetitionListBox from "../../components/basic/petition/PetitionListBox";
import Typography from "@mui/material/Typography";
import {useError} from "../../contexts/ErrorContext";
import PetitionService from "../../services/base/ext/PetitionService";
import Loading from "../../components/layouts/Loading";
import Search from "../../components/layouts/list/Search";

const PetitionsReviewPage = () => {
    const {showError} = useError()

    const [petitions, setPetitions] = useState([])

    const [searchName, setSearchName] = useState(null)
    const [statusFilter, setStatusFilter] = useState(null)

    const [page, setPage] = useState(1);
    const [pagesCount, setPagesCount] = useState(1)

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setPage(1);
    }, [searchName, statusFilter]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await PetitionService.getPetitionsForDirector({
                    page: page - 1,
                    size: 15,
                    name: searchName,
                    status: 'WAITING_FOR_CONSIDERATION'
                });

                console.log("petitions:")
                console.log(response)

                setPetitions(response.content)
                setPagesCount(response.totalPages)
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [searchName, statusFilter, page]);

    if (loading) {
        return <Loading/>;
    }

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box>
            <Stack direction="row"
                   sx={{alignItems: 'center', display: "flex", justifyContent: "space-between", paddingX: '10px',}}>
                <Typography variant="h6" fontWeight={'bold'}>Petitions</Typography>

                <Search
                    searchQuery={searchName}
                    setSearchQuery={setSearchName}
                    sx={{mr: 1.5}}
                />

            </Stack>
            <Divider sx={{mt: 0.75, mb: 0.75}}/>

            <Stack direction="column">
                {petitions.map((petition) => (
                    <Box key={petition.id}>
                        <PetitionListBox petition={petition}/>
                    </Box>
                ))}
            </Stack>

        </Box>
    );
};

export default PetitionsReviewPage;