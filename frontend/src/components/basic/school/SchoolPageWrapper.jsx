import React, {useEffect, useState} from 'react';
import Box from "@mui/material/Box";
import SchoolBox from "./SchoolBox";
import Loading from "../../layouts/Loading";
import Typography from "@mui/material/Typography";
import SchoolService from "../../../services/base/ext/SchoolService";

const SchoolPageWrapper = ({children}) => {

    const [school, setSchool] = useState()

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await SchoolService.getMySchool()
                setSchool(response)
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return <Loading/>;
    }

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '27.5% 70%',
            paddingX: 1,
            paddingTop: 2,
            paddingBottom: 4,
            gap: 3.5,
        }}>
            <SchoolBox school={school}/>

            {children}
        </Box>
    );
};

export default SchoolPageWrapper;