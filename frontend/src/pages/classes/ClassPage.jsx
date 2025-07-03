import React, {useEffect, useState} from 'react';
import {
    Box,
    Container,
    Divider,
    IconButton,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";

const ClassPage = () => {
    const singleClassMinifiedWithStudents = {
        id: 1,
        className: "1-A",
        students: [
            {id: "s001", firstName: "Oleksii", lastName: "Ivanenko", gender: "Male", birthday: "2018-09-01"},
            {id: "s002", firstName: "Mariia", lastName: "Petrenko", gender: "Female", birthday: "2018-09-10"},
            {id: "s003", firstName: "Dmytro", lastName: "Sydorenko", gender: "Male", birthday: "2018-09-15"},
            {id: "s004", firstName: "Anastasiia", lastName: "Kovalenko", gender: "Female", birthday: "2018-10-01"},
            {id: "s005", firstName: "Vitalii", lastName: "Melnyk", gender: "Male", birthday: "2018-10-05"},
            {id: "s006", firstName: "Sofiia", lastName: "Kovalchuk", gender: "Female", birthday: "2018-10-12"},
            {id: "s007", firstName: "Artem", lastName: "Bondarenko", gender: "Male", birthday: "2018-10-20"},
            {id: "s008", firstName: "Yuliia", lastName: "Tkachenko", gender: "Female", birthday: "2018-11-01"},
            {id: "s009", firstName: "Volodymyr", lastName: "Onyshchenko", gender: "Male", birthday: "2018-11-08"},
            {id: "s010", firstName: "Kateryna", lastName: "Kravchenko", gender: "Female", birthday: "2018-11-15"},
            {id: "s011", firstName: "Mykhailo", lastName: "Shevchenko", gender: "Male", birthday: "2018-11-22"},
            {id: "s012", firstName: "Anna", lastName: "Lysenko", gender: "Female", birthday: "2018-12-01"},
            {id: "s013", firstName: "Pavlo", lastName: "Polishchuk", gender: "Male", birthday: "2018-12-07"},
            {id: "s014", firstName: "Nataliia", lastName: "Moroz", gender: "Female", birthday: "2019-01-01"},
            {id: "s015", firstName: "Hlib", lastName: "Havryliuk", gender: "Male", birthday: "2019-01-09"},
            {id: "s016", firstName: "Veronika", lastName: "Semenyuk", gender: "Female", birthday: "2019-01-16"},
            {id: "s017", firstName: "Danylo", lastName: "Marchenko", gender: "Male", birthday: "2019-01-25"},
            {id: "s018", firstName: "Yelyzaveta", lastName: "Vasylenko", gender: "Female", birthday: "2019-02-03"},
            {id: "s019", firstName: "Oleksandr", lastName: "Rudenko", gender: "Male", birthday: "2019-02-10"},
            {id: "s020", firstName: "Khrystyna", lastName: "Savchenko", gender: "Female", birthday: "2019-02-17"},
            {id: "s021", firstName: "Bohdan", lastName: "Pavlenko", gender: "Male", birthday: "2019-02-28"},
            {id: "s022", firstName: "Oksana", lastName: "Kuzmenko", gender: "Female", birthday: "2019-03-05"},
            {id: "s023", firstName: "Roman", lastName: "Fedorenko", gender: "Male", birthday: "2019-03-12"},
            {id: "s024", firstName: "Viktoriia", lastName: "Hryhorenko", gender: "Female", birthday: "2019-03-20"},
            {id: "s025", firstName: "Maksym", lastName: "Lytvynenko", gender: "Male", birthday: "2019-03-28"},
            {id: "s026", firstName: "Iryna", lastName: "Shevchuk", gender: "Female", birthday: "2019-04-05"},
            {id: "s027", firstName: "Andrii", lastName: "Koval", gender: "Male", birthday: "2019-04-12"},
            {id: "s028", firstName: "Polina", lastName: "Melnyk", gender: "Female", birthday: "2019-04-20"},
            {id: "s029", firstName: "Serhii", lastName: "Bondar", gender: "Male", birthday: "2019-04-25"},
            {id: "s030", firstName: "Tetiana", lastName: "Voloshyna", gender: "Female", birthday: "2019-05-01"},
        ]
    };

    const columns = [
        {
            id: 'number',
            label: <Typography variant="body2">№</Typography>,
            render: (_, i) => i + 1
        },
        {
            id: 'firstName',
            label: <Typography variant="body2">First name</Typography>,
            render: student => student.firstName
        },
        {
            id: 'lastName',
            label: <Typography variant="body2">Last name</Typography>,
            render: student => student.lastName
        },
        {
            id: 'email',
            label: <Typography variant="body2">Email</Typography>,
            render: student => student.email || 'none'
        },
        {
            id: 'actions',
            label: <Typography variant="body2" align="center">Actions</Typography>,
            align: 'center',
            render: (student) => (
                <IconButton size="small">
                    <DeleteIcon color="error"/>
                </IconButton>
            )
        }
    ];


    return (
        <Box sx={{
            padding: '20px',
            borderRadius: "10px",
            display: "flex",
            flexDirection: "column"
        }}>
            <Stack direction="row" sx={{
                alignItems: 'center',
                display: "flex",
                justifyContent: "space-between "
            }}>
                <Box sx={{display: 'flex', alignItems: 'top', gap: 0.5}}>
                    <Typography variant="h4">{singleClassMinifiedWithStudents.className}</Typography>
                </Box>
            </Stack>
            <Divider sx={{mb: 0.3}}/>
            <TableContainer>
                <Table>
                    <TableHead>
                        <TableRow>
                            {columns.map(col => (
                                <TableCell
                                    key={col.id}
                                    align={col.align || "left"}
                                    sx={{padding: "5px"}}>
                                    {col.label}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {singleClassMinifiedWithStudents.students.map((student, index) => (
                            <TableRow
                                key={student.id}
                                hover
                                sx={{cursor: 'pointer', height: "36px"}}
                            >
                                {columns.map(col => (
                                    <TableCell key={col.id} align={col.align || "left"}
                                               sx={{padding: "8px"}}>
                                        {col.render(student, index)}
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default ClassPage;