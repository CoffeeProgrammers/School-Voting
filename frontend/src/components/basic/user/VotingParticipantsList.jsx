import React, {useEffect, useState} from 'react';
import UserService from "../../../services/base/ext/UserService";
import Typography from "@mui/material/Typography";
import {useParams} from "react-router-dom";
import UserList from "./UserList";

const VotingParticipantsList = () => {
    const {id} = useParams();

    const [participants, setParticipants] = useState([]);

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
                const studentListResponse = await UserService.getUsersByVoting(id, {
                    email: searchEmail,
                    firstName: searchFirstName,
                    lastName: searchLastName,
                    page: page - 1,
                    size: 15
                })

                setParticipants(studentListResponse.content);
                setPagesCount(studentListResponse.totalPages);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchStudents();
    }, [searchFirstName, searchLastName, searchEmail, page]);

    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }
    return (
        <UserList
            users={participants}
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
    );
};

export default VotingParticipantsList;