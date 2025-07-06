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

const SCHOOL_PETITIONS = [
    {
        id: 1,
        name: "Increase funding for school libraries",
        endTime: "2025-09-30T23:59:59",
        levelType: "class",
        status: "ACTIVE",
        countSupport: 35,
        countNeeded: 100,
        supportedByCurrentUser: false,
    },
    {
        id: 2,
        name: "Introduce mandatory media literacy lessons",
        endTime: "2025-10-15T23:59:59",
        levelType: "school",
        status: "WAITING_FOR_CONSIDERATION",
        countSupport: 52,
        countNeeded: 500,
        supportedByCurrentUser: true,
    },
    {
        id: 3,
        name: "Install water filters in all city schools",
        endTime: "2025-09-10T23:59:59",
        levelType: "class",
        status: "APPROVED",
        countSupport: 134,
        countNeeded: 100,
        supportedByCurrentUser: false,
    },
    {
        id: 4,
        name: "Improve the quality of school meals",
        endTime: "2025-11-01T23:59:59",
        levelType: "school",
        status: "ACTIVE",
        countSupport: 88,
        countNeeded: 200,
        supportedByCurrentUser: true,
    },
    {
        id: 5,
        name: "Add more extracurricular clubs and activities",
        endTime: "2025-09-25T23:59:59",
        levelType: "class",
        status: "WAITING_FOR_CONSIDERATION",
        countSupport: 47,
        countNeeded: 150,
        supportedByCurrentUser: false,
    },
    {
        id: 6,
        name: "Replace broken desks and chairs in classrooms",
        endTime: "2025-10-05T23:59:59",
        levelType: "school",
        status: "REJECTED",
        countSupport: 1065,
        countNeeded: 300,
        supportedByCurrentUser: false,
    },
    {
        id: 7,
        name: "Replace broken desks and chairs in classrooms",
        endTime: "2025-09-20T23:59:59",
        levelType: "class",
        status: "APPROVED",
        countSupport: 23,
        countNeeded: 100,
        supportedByCurrentUser: false,
    },
    {
        id: 8,
        name: "Ensure mental health support in every school",
        endTime: "2025-10-30T23:59:59",
        levelType: "school",
        status: "WAITING_FOR_CONSIDERATION",
        countSupport: 271,
        countNeeded: 250,
        supportedByCurrentUser: true,
    },
    {
        id: 9,
        name: "Add more digital equipment for online learning",
        endTime: "2025-09-18T23:59:59",
        levelType: "class",
        status: "UNSUCCESSFUL",
        countSupport: 41,
        countNeeded: 120,
        supportedByCurrentUser: false,
    },
    {
        id: 10,
        name: "Introduce basic financial literacy in high schools",
        endTime: "2025-11-10T23:59:59",
        levelType: "school",
        status: "ACTIVE",
        countSupport: 93,
        countNeeded: 300,
        supportedByCurrentUser: true,
    },
];


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
        setPage(1);
    }, [searchName, statusFilter]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = role === 'STUDENT' ? (
                    await PetitionService.getMyPetitions({
                        page: page - 1,
                        size: 15,
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


    const statusFilterList = [
        {value: null, label: 'All'},
        {value: 'ACTIVE', label: 'Active'},
        {value: 'WAITING_FOR_CONSIDERATION', label: 'Waiting for consideration'},
        {value: 'UNSUCCESSFUL', label: 'Unsuccessful'},
        {value: 'APPROVED', label: 'Approved'},
        {value: 'REJECTED', label: 'Rejected'},
    ];

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
        </Box>
    );
};

export default PetitionsListPage;