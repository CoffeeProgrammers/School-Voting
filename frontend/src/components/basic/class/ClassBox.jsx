import React, {useEffect, useState} from 'react';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import UserList from "../user/UserList";
import ClassService from "../../../services/base/ext/ClassService";
import UserService from "../../../services/base/ext/UserService";
import Link from "@mui/material/Link";
import {Link as RouterLink} from "react-router";
import DeleteButton from "../../layouts/DeleteButton";
import {useError} from "../../../contexts/ErrorContext";
import {useNavigate} from "react-router-dom";
import EditButton from "../../layouts/EditButton";
import AssignUsersToClass from "../user/AssignStudentsToClass";
import Cookies from "js-cookie";
// import AssignUsersToCLass from "../user/AssignStudentsToClass";

const ClassBox = ({isMy, classId}) => {
    const {showError} = useError()
    const navigate = useNavigate();

    const [openAssignDialog, setOpenAssignDialog] = useState(false)

    const [studentClass, setStudentClass] = useState(null);
    const [students, setStudents] = useState([]);

    const [searchFirstName, setSearchFirstName] = useState('');
    const [searchLastName, setSearchLastName] = useState('');
    const [searchEmail, setSearchEmail] = useState('');

    const [page, setPage] = useState(1);
    const [pagesCount, setPagesCount] = useState(1);

    const [loadingClass, setLoadingClass] = useState(true);
    const [loadingUsers, setLoadingUsers] = useState(false);
    const [error, setError] = useState(null);
    
    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [page]);

    useEffect(() => {
        setPage(1);
    }, [searchFirstName, searchLastName, searchEmail]);

    useEffect(() => {
        const fetchClass = async () => {
            setLoadingClass(true);
            try {
                const studentClassResponse = isMy ? (
                    await ClassService.getMyClass()
                ) : (
                    await ClassService.getClassById(classId)
                )
                setStudentClass(studentClassResponse);
            } catch (err) {
                setError(err);
            } finally {
                setLoadingClass(false);
            }
        };

        fetchClass();
    }, []);

    useEffect(() => {
        if (!studentClass) return;

        const fetchStudents = async () => {
            setLoadingUsers(true);
            try {
                const studentListResponse = isMy ? (
                    await UserService.getUsersOfMyClass({
                        email: searchEmail,
                        firstName: searchFirstName,
                        lastName: searchLastName,
                        page: page - 1,
                        size: 15
                    })
                ) : (
                    await UserService.getUsersByClass(classId, {
                        email: searchEmail,
                        firstName: searchFirstName,
                        lastName: searchLastName,
                        page: page - 1,
                        size: 15
                    })
                )

                setStudents(studentListResponse.content);
                setPagesCount(studentListResponse.totalPages);
            } catch (err) {
                setError(err);
            } finally {
                setLoadingUsers(false);
            }
        };

        fetchStudents();
    }, [studentClass, searchFirstName, searchLastName, searchEmail, page]);

    const handleDelete = async () => {
        try {
            setLoadingClass(true);
            setLoadingUsers(true);
            await ClassService.deleteClass(studentClass.id);
            navigate('/school', {replace: true});
        } catch (error) {
            showError(error);
        } finally {
            setLoadingClass(false);
            setLoadingUsers(false);
        }
    };
    const handleAssign = async (userIds) => {
        try {
            setLoadingUsers(true);
            const response = await ClassService.assignUsers(studentClass.id, userIds);
            setStudents([...students, ...response]);
        } catch (error) {
            showError(error);
        } finally {
            setLoadingUsers(false);
        }
    }

    const handleUnassign = async (userId) => {
        try {
            setLoadingUsers(true);
            const response = await ClassService.unassignUsers(studentClass.id, [userId]);
            setStudents(students.filter(student => student.id !== userId));
        } catch (error) {
            showError(error);
        } finally {
            setLoadingUsers(false);
        }
    }
    const role = Cookies.get("role");
    const isStudent = role === 'STUDENT';
    const isTeacher = role === 'TEACHER';
    const isDirector = role === 'DIRECTOR';


    if (error) {
        return <Typography color={"error"}>Error: {error.message}</Typography>;
    }

    return (
        <>
            <Box sx={{paddingX: '15px', mb: 0.55}}>
                {!isMy && (
                    <Box ml={1.5} mt={0.75} display={'flex'} alignItems={'center'} justifyContent={'flex-start'}>
                        <Link component={RouterLink} to={'/school'}>
                            <Typography variant={'caption'}>← go back </Typography>
                        </Link>
                        {Cookies.get("role") === "TEACHER" || Cookies.get("role") === "DIRECTOR" ?
                            (<Box ml={1}>
                                <Box display="flex" alignItems="center" gap={1}>
                                    <DeleteButton
                                        text={'Are you sure you want to delete this class?'}
                                        deleteFunction={handleDelete}
                                        fontSize={19}
                                    />

                                    <EditButton path={'update'} fontSize={19} state={studentClass}/>
                                </Box>
                            </Box>) : ""
                        }
                    </Box>
                )}
                <Box>
                    <Typography variant={"h6"} fontWeight={'bold'}>
                        {loadingClass ? 'Loading...' : `${isMy ? 'My class' : 'Class'}: ${studentClass?.name || ''}`}
                    </Typography>
                </Box>

            </Box>

            <Divider/>

            <UserList
                users={students}
                actions={isDirector || isTeacher}
                addToListFunction={() => setOpenAssignDialog(true)}
                deleteFromListFunction={handleUnassign}
                searchFirstName={searchFirstName}
                searchLastName={searchLastName}
                searchEmail={searchEmail}
                setSearchEmail={setSearchEmail}
                setSearchFirstName={setSearchFirstName}
                setSearchLastName={setSearchLastName}
                loading={loadingUsers}
                page={page}
                setPage={setPage}
                pagesCount={pagesCount}
            />
            <AssignUsersToClass
                students={students}
                onAssign={handleAssign}
                open={openAssignDialog}
                setOpen={setOpenAssignDialog}
            />
        </>
    );
};

export default ClassBox;
