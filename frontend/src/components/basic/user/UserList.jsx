import React from 'react';
import IconButton from "@mui/material/IconButton";
import DeleteIcon from "@mui/icons-material/Delete";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import Box from "@mui/material/Box";
import Search from "../../layouts/list/Search";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import Divider from "@mui/material/Divider";
import PaginationBox from "../../layouts/list/PaginationBox";
import Typography from "@mui/material/Typography";

const UserList = (
    {
        users,
        actions = false,
        search = true,
        searchFirstName,
        setSearchFirstName,
        searchLastName,
        setSearchLastName,
        searchEmail,
        setSearchEmail,
        loading,
        page,
        pagesCount,
        setPage
    }) => {
        const columns = [
            {
                id: 'firstName',
                label: <Search
                    label={"First Name"}
                    searchQuery={searchFirstName}
                    setSearchQuery={setSearchFirstName}
                    sx={{width: '100%'}}

                />,
                render: user => (
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <AccountCircleIcon color="primary"/>
                        {user.firstName}
                    </Box>
                )
            },
            {
                id: 'lastName',
                label: <Search
                    label={"Last Name"}
                    searchQuery={searchLastName}
                    setSearchQuery={setSearchLastName}
                    sx={{width: '100%'}}


                />,
                render: user => user.lastName
            },
            {
                id: 'email',
                label: <Search
                    label={"Email"}
                    searchQuery={searchEmail}
                    setSearchQuery={setSearchEmail}
                    sx={{width: '100%'}}

                />,
                render: user => user.email
            }
        ];

        if (actions) {
            columns.push({
                id: 'actions',
                label:
                    <IconButton sx={{
                        borderRadius: 15,
                        width: "37px",
                        height: "37px",
                        mr: 0.85
                    }}>
                        <AddCircleIcon sx={{
                            fontSize: 30, color: 'primary.main', borderRadius: 15,
                        }}/>
                    </IconButton>
                ,
                align: 'right',
                render: user => (
                    <IconButton size="small">
                        <DeleteIcon sx={{color: 'error.main'}}/>
                    </IconButton>
                )
            });
        }
        return (
            <>
                <Box>
                    <TableContainer>
                        <Table>
                            {search &&
                                <TableHead>
                                    <TableRow>
                                        {columns.map(col => (
                                            <TableCell key={col.id} align={col.align || "left"}
                                                       sx={{paddingY: '10px', paddingX: "7.5px"}}>
                                                {col.label}
                                            </TableCell>
                                        ))}
                                    </TableRow>
                                </TableHead>
                            }

                            <TableBody>
                                {loading ? (
                                    <Typography p={2}>
                                        Loading...
                                    </Typography>
                                ) : (
                                    users.map((user, index) => (
                                        <TableRow
                                            key={user.id}
                                            hover
                                            sx={{height: "36px"}}
                                        >
                                            {columns.map(col => (
                                                <TableCell key={col.id} align={col.align || "left"}
                                                           sx={{
                                                               borderBottom: index === users.length - 1 ? 'none' : '1px solid #ddd',
                                                               paddingY: "7px", paddingLeft: "20px"
                                                           }}>
                                                    {col.render(user, index)}
                                                </TableCell>
                                            ))}
                                        </TableRow>
                                    )))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <Divider/>

                    {loading &&
                        (pagesCount > 1 && (
                            <Box sx={{marginTop: "auto"}}>
                                <PaginationBox
                                    page={page}
                                    pagesCount={pagesCount}
                                    setPage={setPage}
                                />
                            </Box>
                        ))}
                </Box>
            </>
        );
    }
;

export default UserList;