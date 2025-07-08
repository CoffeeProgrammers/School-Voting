import React, {useEffect, useState} from 'react';
import UserService from "../../../services/base/ext/UserService";
import Typography from "@mui/material/Typography";
import {Button} from "@mui/material";
import BasicDataDialog from "../../layouts/BasicDataDialog";
import UserList from "./UserList";

const AssignUsersToClass = ({students, onAssign, open, setOpen}) => {
    const [selectedIds, setSelectedIds] = useState([]);

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
                const studentListResponse = await UserService.getUsersWithoutClass({
                    email: searchEmail,
                    firstName: searchFirstName,
                    lastName: searchLastName,
                    page: page - 1,
                    size: 15
                })

                setUsers(studentListResponse.content);
                setPagesCount(studentListResponse.totalPages);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchStudents();
    }, [searchFirstName, searchLastName, searchEmail, page, students]);

    const toggleUserId = (userId) => {
        setSelectedIds(prev =>
            prev.includes(userId)
                ? prev.filter(id => id !== userId)
                : [...prev, userId]
        );
    };

    const handleAssign = async () => {
        onAssign(selectedIds);
        setOpen(false);
    }
    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <>
            <BasicDataDialog
                open={open}
                handleClose={() => setOpen(false)}
                title={"Choose the users to add"}
                size={'large'}
                actions={
                    <Button onClick={() => handleAssign()} variant="contained" color="primary"
                            sx={{height: 32, borderRadius: 10}}
                    >
                        Save
                    </Button>}
            >

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
    )
};

export default AssignUsersToClass;