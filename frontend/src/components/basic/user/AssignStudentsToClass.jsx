// import React, {useEffect, useState} from 'react';
// import UserService from "../../../services/base/ext/UserService";
// import Typography from "@mui/material/Typography";
// import {Button, IconButton} from "@mui/material";
// import BasicDataDialog from "../../layouts/BasicDataDialog";
// import UserList from "./UserList";
// import AddCircleIcon from "@mui/icons-material/AddCircle";
//
// const AssignUsersToCLass = ({onAssign}) => {
//     const [open, setOpen] = useState(false)
//     const [selectedIds, setSelectedIds] = useState([]);
//
//     const [users, setUsers] = useState([]);
//
//     const [searchFirstName, setSearchFirstName] = useState('');
//     const [searchLastName, setSearchLastName] = useState('');
//     const [searchEmail, setSearchEmail] = useState('');
//
//     const [page, setPage] = useState(1);
//     const [pagesCount, setPagesCount] = useState(1);
//
//     const [loading, setLoading] = useState(false);
//     const [error, setError] = useState(null);
//
//     useEffect(() => {
//         window.scrollTo({top: 0, behavior: 'smooth'});
//     }, [page]);
//
//     useEffect(() => {
//         setPage(1);
//     }, [searchFirstName, searchLastName, searchEmail]);
//
//     useEffect(() => {
//         const fetchStudents = async () => {
//             setLoading(true);
//             try {
//                 const studentListResponse = await UserService.getUsersWithoutClass({
//                     email: searchEmail,
//                     firstName: searchFirstName,
//                     lastName: searchLastName,
//                     page: page - 1,
//                     size: 15
//                 })
//
//                 console.log(studentListResponse.content)
//                 setUsers(studentListResponse.content);
//                 setPagesCount(studentListResponse.totalPages);
//             } catch (err) {
//                 setError(err);
//             } finally {
//                 setLoading(false);
//             }
//         };
//
//         fetchStudents();
//     }, [searchFirstName, searchLastName, searchEmail, page]);
//
//     const toggleUserId = (userId) => {
//         setSelectedIds(prev =>
//             prev.includes(userId)
//                 ? prev.filter(id => id !== userId)
//                 : [...prev, userId]
//         );
//     };
//
//     if (error) {
//         return <Typography color={"error"}>Error: {error.message}</Typography>;
//     }
//
//     return (
//         <>
//             <IconButton onClick={() => toggleUserId(user.id)} size="small">
//                 <AddCircleIcon color="primary"/>
//             </IconButton>
//
//             <BasicDataDialog
//                 open={open}
//                 handleClose={() => setOpen(false)}
//                 title={"Choose the users to add"}
//                 size={'large'}
//             >
//
//                 <UserList
//                     users={users}
//
//                     addButtons={true}
//                     toggleUserId={toggleUserId}
//                     selectedIds={selectedIds}
//
//                     loading={loading}
//                     page={page}
//                     setPage={setPage}
//                     pagesCount={pagesCount}
//                     searchFirstName={searchFirstName}
//                     searchLastName={searchLastName}
//                     searchEmail={searchEmail}
//                     setSearchEmail={setSearchEmail}
//                     setSearchFirstName={setSearchFirstName}
//                     setSearchLastName={setSearchLastName}
//
//                     actions={
//                         <Button onClick={onAssign} variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
//                             Save
//                         </Button>
//                     }
//                 />
//             </BasicDataDialog>
//         </>
//     )
// };
//
// export default AssignUsersToCLass;