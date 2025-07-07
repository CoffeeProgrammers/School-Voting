import React, {useEffect, useState} from 'react';
import {Box, Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import UserList from "../../components/basic/user/UserList";
import theme from "../../assets/theme";
import BuildCircleOutlinedIcon from "@mui/icons-material/BuildCircleOutlined";
import Cookies from "js-cookie";
import UserService from "../../services/base/ext/UserService";

const users = [
    {id: 1, firstName: "Alice", lastName: "Johnson", email: "alice.johnson@example.com"},
    {id: 2, firstName: "Bob", lastName: "Smith", email: "bob.smith@example.com"},
    {id: 3, firstName: "Charlie", lastName: "Brown", email: "charlie.brown@example.com"},
    {id: 4, firstName: "Diana", lastName: "Williams", email: "diana.williams@example.com"},
    {id: 5, firstName: "Ethan", lastName: "Davis", email: "ethan.davis@example.com"},
    {id: 6, firstName: "Fiona", lastName: "Clark", email: "fiona.clark@example.com"},
    {id: 7, firstName: "George", lastName: "Miller", email: "george.miller@example.com"},
    {id: 8, firstName: "Hannah", lastName: "Taylor", email: "hannah.taylor@example.com"},
    {id: 9, firstName: "Ian", lastName: "Anderson", email: "ian.anderson@example.com"},
    {id: 10, firstName: "Julia", lastName: "Thomas", email: "julia.thomas@example.com"}
];

const ControlPanel = () => {
    const isDirector = Cookies.get('role') === 'DIRECTOR';

    const [role, setRole] = useState("Student")

    const [users, setUsers] = useState([]);

    const [searchFirstName, setSearchFirstName] = useState('');
    const [searchLastName, setSearchLastName] = useState('');
    const [searchEmail, setSearchEmail] = useState('');

    const [page, setPage] = useState(1);
    const [pagesCount, setPagesCount] = useState(1);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [page]);

    useEffect(() => {
        setPage(1);
    }, [searchFirstName, searchLastName, searchEmail]);

    useEffect(() => {
        const fetchStudents = async () => {
            setLoading(true);
            try {
                const studentListResponse = await UserService.getUsersByRole(role.toUpperCase(), {
                    email: searchEmail,
                    firstName: searchFirstName,
                    lastName: searchLastName,
                    page: page - 1,
                    size: 15
                })

                console.log(studentListResponse.content)
                setUsers(studentListResponse.content);
                setPagesCount(studentListResponse.totalPages);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchStudents();
    }, [role, searchFirstName, searchLastName, searchEmail, page]);

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    const renderTabButton = (tabRole, width) => {
        return (
            <Button onClick={() => setRole(tabRole)} sx={{height: 33, borderRadius: 0, width: width}}>
                <Typography variant='body1' color={role === tabRole ? 'primary' : 'text.secondary'}
                            sx={{
                                borderBottom: "2.5px solid",
                                borderBottomColor: role === tabRole ? theme.palette.primary.main : "transparent",
                            }}>
                    {tabRole}s
                </Typography>
            </Button>
        )
    }

    return (
        <Box>
            <Box sx={{border: '1px solid #ddd', borderRadius: '5px', marginY: '10px'}}>
                <Box sx={{marginTop: '15px'}}>
                    <Box sx={{paddingX: '15px', mb: 0.55, display: 'flex', alignItems: 'center'}}>
                        <BuildCircleOutlinedIcon color="primary" sx={{fontSize: 29, marginRight: 0.5}}/>
                        <Typography variant={"h5"} fontWeight={'bold'}>Control panel</Typography>
                    </Box>

                    {isDirector && (
                        <Stack direction="row" width={'100%'} ml={1.25}>
                            {renderTabButton('Student', 100)}
                            {renderTabButton('Teacher', 100)}
                        </Stack>
                    )}
                    <Divider/>

                    <UserList
                        users={users}
                        actions={true}
                        loading={loading}
                        page={page}
                        setPage={setPage}
                        pagesCount={pagesCount}
                        searchFirstName={searchFirstName}
                        searchLastName={searchLastName}
                        searchEmail={searchEmail}
                        setSearchEmail={setSearchEmail}
                        setSearchFirstName={setSearchFirstName}
                        setSearchLastName={setSearchLastName}
                    />
                </Box>
            </Box>
        </Box>
    );
};

export default ControlPanel;