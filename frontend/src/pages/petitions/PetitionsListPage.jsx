import React, {useEffect, useState} from 'react';
import Search from "../../components/layouts/list/Search";
import {Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import theme from "../../assets/theme";
import Box from "@mui/material/Box";
import PetitionList from "../../components/basic/petition/PetitionList";
import {useError} from "../../contexts/ErrorContext";
import PetitionService from "../../services/base/ext/PetitionService";
import Loading from "../../components/layouts/Loading";
import Cookies from "js-cookie";
import PaginationBox from "../../components/layouts/list/PaginationBox";

const PetitionsListPage = () => {
    const {showError} = useError()

    const role = Cookies.get('role')

    const [petitions, setPetitions] = useState([])

    const [searchName, setSearchName] = useState(null)
    const [statusFilter, setStatusFilter] = useState(null)

    const [page, setPage] = useState(1);
    const [pagesCount, setPagesCount] = useState(1)

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [page]);

    useEffect(() => {
        setPage(1);
    }, [searchName, statusFilter]);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = role === 'STUDENT' ? (
                    await PetitionService.getMyPetitions({
                        page: page - 1,
                        size: 10,
                        name: searchName,
                        status: statusFilter
                    })
                ) : (
                    await PetitionService.getPetitionsForDirector({
                        page: page - 1,
                        size: 10,
                        name: searchName,
                        status: statusFilter
                    })
                )

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


    const statusFilterList = [
        {value: null, label: 'All'},
        {value: 'ACTIVE', label: 'Active'},
        {value: 'WAITING_FOR_CONSIDERATION', label: 'Waiting for consideration'},
        {value: 'UNSUCCESSFUL', label: 'Unsuccessful'},
        {value: 'APPROVED', label: 'Approved'},
        {value: 'REJECTED', label: 'Rejected'},
    ];


    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box>
            <Stack direction="row"
                   sx={{alignItems: 'center', display: "flex", justifyContent: "space-between", paddingX: '10px',}}>
                <Typography variant="h6" fontWeight={'bold'}>Petitions</Typography>
                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.25}>
                    <Search
                        searchQuery={searchName}
                        setSearchQuery={setSearchName}
                        sx={{mr: 1.5}}
                    />

                    <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                        Create a petition
                    </Button>
                </Box>

            </Stack>
            <Divider sx={{mt: 0.75}}/>

            <Stack direction="row" width={'100%'}>
                {statusFilterList.map((option, index) => (
                    <Button key={index} onClick={() => setStatusFilter(option.value)} fullWidth
                            sx={{height: 40, borderRadius: 0, width: index !== 0 ? '100%' : 100}}>
                        <Typography noWrap color={statusFilter === option.value ? 'primary' : 'text.secondary'}
                                    sx={{
                                        borderBottom: "2.5px solid",
                                        borderBottomColor: statusFilter === option.value ? theme.palette.primary.main : "transparent",
                                    }}>
                            {option.label}
                        </Typography>
                    </Button>
                ))}
            </Stack>
            <Divider sx={{mb: 0.75}}/>

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

export default PetitionsListPage;