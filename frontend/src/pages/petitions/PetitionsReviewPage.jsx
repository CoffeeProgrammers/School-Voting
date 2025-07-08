import React, {useEffect, useState} from 'react';
import Divider from "@mui/material/Divider";
import {Stack} from "@mui/material";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {useError} from "../../contexts/ErrorContext";
import PetitionService from "../../services/base/ext/PetitionService";
import Loading from "../../components/layouts/Loading";
import Search from "../../components/layouts/list/Search";
import PetitionList from "../../components/basic/petition/PetitionList";
import PaginationBox from "../../components/layouts/list/PaginationBox";

const PetitionsReviewPage = () => {
    const {showError} = useError()

    const [petitions, setPetitions] = useState([])

    const [searchName, setSearchName] = useState(null)

    const [page, setPage] = useState(1);
    const [pagesCount, setPagesCount] = useState(1)

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [page]);

    useEffect(() => {
        setPage(1);
    }, [searchName]);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await PetitionService.getPetitionsForDirector({
                    page: page - 1,
                    size: 15,
                    name: searchName,
                    status: 'WAITING_FOR_CONSIDERATION'
                });


                setPetitions(response.content)
                setPagesCount(response.totalPages)
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [searchName, page]);

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box>
            <Stack direction="row"
                   sx={{alignItems: 'center', display: "flex", justifyContent: "space-between", paddingX: '10px',}}>
                <Typography variant="h6" fontWeight={'bold'}>Petitions Review</Typography>

                <Search
                    searchQuery={searchName}
                    setSearchQuery={setSearchName}
                    sx={{mr: 1.5}}
                />

            </Stack>
            <Divider sx={{mt: 0.75, mb: 0.75}}/>

            {loading ? <Loading/> : (<>
                <PetitionList petitions={petitions}/>

                {pagesCount > 1 && (
                    <Box sx={{marginTop: "auto"}}>
                        <PaginationBox
                            page={page}
                            pagesCount={pagesCount}
                            setPage={setPage}
                        />
                    </Box>
                )}
            </>)}

        </Box>
    );
};

export default PetitionsReviewPage;