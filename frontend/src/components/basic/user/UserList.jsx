import React from 'react';
import IconButton from "@mui/material/IconButton";
import DeleteIcon from "@mui/icons-material/Delete";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import Box from "@mui/material/Box";
import Search from "../../layouts/list/Search";
import AddCircleIcon from '@mui/icons-material/AddCircle';

const UserList = ({users, actions = false}) => {
        const [page, setPage] = React.useState(1);

        const columns = [
            {
                id: 'firstName',
                label: <Search
                    label={"First Name"}
                    // searchQuery={searchFirstName}
                    // setSearchQuery={setSearchFirstName}
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
                    // searchQuery={searchLastName}
                    // setSearchQuery={setSearchLastName}
                    sx={{width: '100%'}}


                />,
                render: user => user.lastName
            },
            {
                id: 'email',
                label: <Search
                    label={"Email"}
                    // searchQuery={searchEmail}
                    // setSearchQuery={setSearchEmail}
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
                            <TableBody>
                                {users.map((user, index) => (
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
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </>
        );
    }
;

export default UserList;