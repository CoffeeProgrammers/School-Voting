import React from 'react';
import IconButton from "@mui/material/IconButton";
import DeleteIcon from "@mui/icons-material/Delete";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import Box from "@mui/material/Box";
import Search from "../../layouts/list/Search";
import Typography from "@mui/material/Typography";

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


const UserList = () => {
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
        },
        {
            id: 'actions',
            label:
                <IconButton size="small"
                    // onClick={(e) => handleDelete(e, user.id)}
                >
                    <DeleteIcon sx={{color: 'error'}}/>
                </IconButton>,
            // <CreateUserDialog handleCreate={handleCreate}/>,
            align: 'center',
            render: (user) => (
                <IconButton size="small"
                    // onClick={(e) => handleDelete(e, user.id)}
                >
                    <DeleteIcon sx={{color: 'error.main'}}/>
                </IconButton>
            )
        }
    ];
    return (
        <>
            <Box sx={{paddingX: '15px', mb: 0.55}}>
                <Typography variant={"h6"} fontWeight={'bold'}>My class: 11-A</Typography>
            </Box>

            <Box sx={{border: '1px solid #ddd', borderRadius: '5px'}}>
                <Box sx={{}}>
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    {columns.map(col => (
                                        <TableCell key={col.id} align={col.align || "left"} sx={{padding: "10px"}}>
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
                                        sx={{cursor: 'pointer', height: "36px"}}
                                    >
                                        {columns.map(col => (
                                            <TableCell key={col.id} align={col.align || "left"}
                                                       sx={{paddingY: "7px", paddingLeft: "20px"}}>
                                                {col.render(user, index)}
                                            </TableCell>
                                        ))}
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </Box>

        </>
    );
};

export default UserList;