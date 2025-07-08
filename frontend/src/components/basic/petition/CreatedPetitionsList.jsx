import React, {useEffect, useState} from 'react';
import PetitionService from "../../../services/base/ext/PetitionService";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Loading from "../../layouts/Loading";
import PetitionList from "./PetitionList";
import PaginationBox from "../../layouts/list/PaginationBox";

const CreatedPetitionsList = () => {
    const [petitions, setPetitions] = useState([])

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
                const response = await PetitionService.getMyOwnPetitions({
                    page: page - 1,
                    size: 10,
                })

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
    }, [page]);


    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box>
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

export default CreatedPetitionsList;