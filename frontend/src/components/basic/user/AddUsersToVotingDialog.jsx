import React, {useEffect, useState} from 'react';
import BasicDataDialog from "../../layouts/BasicDataDialog";
import UserService from "../../../services/base/ext/UserService";
import Typography from "@mui/material/Typography";
import {Button, Stack} from "@mui/material";
import theme from "../../../assets/theme";
import UserList from "./UserList";
import Divider from "@mui/material/Divider";

const AddUsersToVotingDialog = ({userIds, toggleUserId, levelType, selectedIds}) => {
    const [open, setOpen] = useState(false)

    const [role, setRole] = useState(levelType === 'GROUP_OF_PARENTS_AND_STUDENTS' ? 'STUDENT' : 'TEACHER')

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
        <>
            <Button onClick={() => setOpen(true)} variant={'outlined'} fullWidth>
                {userIds.length} selected users
            </Button>

            <BasicDataDialog
                open={open}
                handleClose={() => setOpen(false)}
                title={"Choose the users to add"}
                size={'large'}
            >
                {levelType === 'GROUP_OF_PARENTS_AND_STUDENTS' && (<>
                    <Stack direction="row" width={'100%'} ml={1.25}>
                        {renderTabButton('Student', 100)}
                        {renderTabButton('Parent', 100)}
                    </Stack>
                    <Divider/>
                </>)}

                <UserList
                    users={users}

                    addButtons={true}
                    toggleUserId={toggleUserId}
                    selectedIds={selectedIds}

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
            </BasicDataDialog>
        </>
    );
};

export default AddUsersToVotingDialog;