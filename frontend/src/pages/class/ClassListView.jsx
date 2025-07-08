import React, {useEffect, useState} from 'react';
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Search from "../../components/layouts/list/Search";
import {Button, Stack} from "@mui/material";
import Divider from "@mui/material/Divider";
import Loading from "../../components/layouts/Loading";
import PaginationBox from "../../components/layouts/list/PaginationBox";
import ClassList from "../../components/basic/class/ClassList";
import ClassService from "../../services/base/ext/ClassService";
import {useNavigate} from "react-router-dom";


const ClassListView = () => {
    const navigate = useNavigate()

    const [classList, setClassList] = useState([])

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
                const response = await ClassService.getAllClasses({
                    page: page - 1,
                    size: 12,
                    name: searchName,
                })

                setClassList(response.content)
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
        <>
            <Stack direction="row" sx={{
                alignItems: 'center',
                display: "flex",
                justifyContent: "space-between",
                paddingX: '15px',
            }}>

                <Typography variant={"h6"} fontWeight={'bold'}>Classes</Typography>

                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.5}>
                    <Search
                        label={"Name"}
                        searchQuery={searchName}
                        setSearchQuery={setSearchName}
                        sx={{mr: 1}}
                    />
                    <Button onClick={() => navigate(`/school/class/create`)} variant="contained" color="primary"
                            sx={{height: 32, borderRadius: 10}}>
                        Create a class
                    </Button>
                </Box>
            </Stack>

            <Divider sx={{mt: 0.5, mb: 0.75}}/>

            {loading ? <Loading/> : (<>

                <ClassList studentsClass={classList}/>

                <Divider/>

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
        </>
    )
};

export default ClassListView;