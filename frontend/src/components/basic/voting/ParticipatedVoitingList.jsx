import React, {useEffect, useState} from 'react';
import VotingService from "../../../services/base/ext/VotingService";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Loading from "../../layouts/Loading";
import VotingList from "./VotingList";
import PaginationBox from "../../layouts/list/PaginationBox";

const ParticipatedVotingList = () => {
    const [votingList, setVotingList] = useState([])

    const [page, setPage] = useState(1);
    const [pagesCount, setPagesCount] = useState(1)

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [page]);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await VotingService.getMyVoting({
                    page: page - 1,
                    size: 10,
                    isNotVote: false,
                })

                setVotingList(response.content)
                setPagesCount(response.totalPages)
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [page]);


    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box>
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

export default ParticipatedVotingList;