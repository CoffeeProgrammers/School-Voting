import React, {useEffect, useState} from 'react';
import {Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Search from "../../components/layouts/list/Search";
import Divider from "@mui/material/Divider";
import theme from "../../assets/theme";
import VotingList from "../../components/basic/voting/VotingList";
import VotingService from "../../services/base/ext/VotingService";
import Cookies from "js-cookie";
import Loading from "../../components/layouts/Loading";
import PaginationBox from "../../components/layouts/list/PaginationBox";
import {useNavigate} from "react-router-dom";


const VotingListPage = () => {
    const role = Cookies.get('role')
    const isDirector = role === 'DIRECTOR'
    const navigate = useNavigate()

    const [votingList, setVotingList] = useState([])

    const [searchName, setSearchName] = useState(null)
    const [activeFilter, setActiveFilter] = useState(null)
    const [isNotVoted, setIsNotVoted] = useState(null)

    const [page, setPage] = useState(1);
    const [pagesCount, setPagesCount] = useState(1)

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setPage(1);
    }, [searchName, activeFilter, isNotVoted]);

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [page]);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = role === 'DIRECTOR' ? (
                    await VotingService.getVotingForDirector({
                        page: page - 1,
                        size: 10,
                        name: searchName,
                        now: activeFilter
                    })
                ) : (
                    await VotingService.getMyVoting({
                        page: page - 1,
                        size: 10,
                        name: searchName,
                        now: activeFilter,
                        isNotVote: isNotVoted,
                    })
                )

                setVotingList(response.content)
                setPagesCount(response.totalPages)
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [searchName, activeFilter, isNotVoted, page]);

    const activeFilters = [
        {value: true, label: 'Active'},
        {value: false, label: 'Finished'},
    ];

    const isNotVotedFilters = [
        {value: true, label: 'Not voted'},
        {value: false, label: 'Voted'},
    ];

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box>
            <Stack direction="row"
                   sx={{alignItems: 'center', display: "flex", justifyContent: "space-between", paddingX: '10px',}}>
                <Typography variant="h6" fontWeight={'bold'}>Voting</Typography>
                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.25}>
                    <Search
                        label={"Name"}
                        searchQuery={searchName}
                        setSearchQuery={setSearchName}
                        sx={{mr: 1.5}}
                    />

                    <Button onClick={() => navigate('create')} variant="contained" color="primary"
                            sx={{height: 32, borderRadius: 10}}>
                        Create a voting
                    </Button>
                </Box>

            </Stack>

            <Divider sx={{mt: 0.75}}/>
            <Stack direction="row" width={'100%'}>
                <Button
                    onClick={() => {
                        setActiveFilter(null)
                        setIsNotVoted(null)
                    }}
                    fullWidth sx={{height: 40, borderRadius: 0, width: 100}}
                >
                    <Typography
                        noWrap
                        color={(activeFilter === null && isNotVoted === null) ? 'primary' : 'text.secondary'}
                        sx={{
                            borderBottom: "2.5px solid",
                            borderBottomColor: (activeFilter === null && isNotVoted) === null ? theme.palette.primary.main : "transparent",
                        }}
                    >
                        All
                    </Typography>
                </Button>

                {activeFilters.map((option, index) => (
                    <Button key={index} onClick={() => setActiveFilter(option.value)} fullWidth
                            sx={{height: 40, borderRadius: 0, width: '100%'}}>
                        <Typography noWrap color={activeFilter === option.value ? 'primary' : 'text.secondary'}
                                    sx={{
                                        borderBottom: "2.5px solid",
                                        borderBottomColor: activeFilter === option.value ? theme.palette.primary.main : "transparent",
                                    }}>
                            {option.label}
                        </Typography>
                    </Button>
                ))}

                {!isDirector && (
                    isNotVotedFilters.map((option, index) => (
                        <Button key={index} onClick={() => setIsNotVoted(option.value)} fullWidth
                                sx={{height: 40, borderRadius: 0, width: '100%'}}>
                            <Typography noWrap color={isNotVoted === option.value ? 'primary' : 'text.secondary'}
                                        sx={{
                                            borderBottom: "2.5px solid",
                                            borderBottomColor: isNotVoted === option.value ? theme.palette.primary.main : "transparent",
                                        }}>
                                {option.label}
                            </Typography>
                        </Button>
                    )))}
            </Stack>
            <Divider sx={{mb: 0.75}}/>

            {loading ? <Loading/> : (<>
                    <VotingList votingList={votingList}/>

                    {pagesCount > 1 && (
                        <Box sx={{marginTop: "auto"}}>
                            <PaginationBox
                                page={page}
                                pagesCount={pagesCount}
                                setPage={setPage}
                            />
                        </Box>
                    )}
                </>
            )}
        </Box>
    );
};

export default VotingListPage;