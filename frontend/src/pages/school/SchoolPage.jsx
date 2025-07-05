import React from 'react';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import HomeWorkIcon from '@mui/icons-material/HomeWork';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import EmailIcon from '@mui/icons-material/Email';
import Search from "../../components/layouts/list/Search";
import {Button, Grid, Stack} from "@mui/material";
import ClassListBox from "../../components/basic/class/ClassListBox";
import UserListBox from "../../components/basic/user/UserListBox";

const school = {
    "id": 1,
    "name": "Lyceum N6 named after Ivan Revchyk",
    "director": {
        "id": 1,
        "firstName": "Igor",
        "lastName": "Pelmen",
        "email": "pelmen.love.pelmens123@gmail.com"
    }
}

const schoolClassesCombined = [
    {
        id: 1,
        className: "1-А",
        numberOfStudents: 28,
        classTeacher: "Олена Іванівна Шевченко",
        specialization: "Загальноосвітній",
        isPrimary: true
    },
    {
        id: 2,
        className: "5-Б",
        numberOfStudents: 25,
        classTeacher: "Ігор Петрович Ковальчук",
        specialization: "Поглиблене вивчення англійської мови",
        isPrimary: false
    },
    {
        id: 3,
        className: "9-В",
        numberOfStudents: 22,
        classTeacher: "Наталія Олегівна Бондаренко",
        specialization: "Фізико-математичний профіль",
        isPrimary: false
    },
    {
        id: 4,
        className: "11-А",
        numberOfStudents: 20,
        classTeacher: "Сергій Миколайович Мельник",
        specialization: "Гуманітарний профіль",
        isPrimary: false
    },
    {
        id: 5,
        className: "7-Г",
        numberOfStudents: 27,
        classTeacher: "Лариса Вікторівна Поліщук",
        specialization: "Загальноосвітній",
        isPrimary: false
    },
    {
        id: 6,
        className: "1-А",
        numberOfStudents: 28,
        classTeacher: "Олена Іванівна Шевченко",
        specialization: "Загальноосвітній",
        isPrimary: true
    },
    {
        id: 7,
        className: "5-Б",
        numberOfStudents: 25,
        classTeacher: "Ігор Петрович Ковальчук",
        specialization: "Поглиблене вивчення англійської мови",
        isPrimary: false
    },
    {
        id: 8,
        className: "9-В",
        numberOfStudents: 22,
        classTeacher: "Наталія Олегівна Бондаренко",
        specialization: "Фізико-математичний профіль",
        isPrimary: false
    },
    {
        id: 9,
        className: "11-А",
        numberOfStudents: 20,
        classTeacher: "Сергій Миколайович Мельник",
        specialization: "Гуманітарний профіль",
        isPrimary: false
    },
    {
        id: 10,
        className: "7-Г",
        numberOfStudents: 27,
        classTeacher: "Лариса Вікторівна Поліщук",
        specialization: "Загальноосвітній",
        isPrimary: false
    }
]

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

const SchoolPage = () => {
    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '1.3fr 3fr',
            paddingX: 1,
            paddingTop: 2,
            paddingBottom: 4
        }}>
            <Box>
                <Box sx={{
                    paddingX: '20px',
                    paddingY: '10px',
                    paddingBottom: "15px"
                }}>
                    <Typography variant='h5' fontWeight='bold'>School</Typography>

                    <Divider sx={{marginBottom: 0.75}}/>

                    <Box marginX={1}>
                        <Typography sx={{fontSize: 15}} fontWeight='bold'>Name:</Typography>
                        <Box sx={{display: 'flex', alignItems: 'center', gap: 0.375}}>
                            <HomeWorkIcon sx={{fontSize: 18, marginLeft: 1,}} color='primary'/>
                            <Typography sx={{fontSize: 15}}>{school.name}</Typography>
                        </Box>
                        <Typography sx={{fontSize: 15}} fontWeight='bold'> Director:</Typography>
                        <Box sx={{display: 'flex', alignItems: 'center', gap: 0.375}}>
                            <AccountCircleIcon sx={{fontSize: 20, marginLeft: 1,}} color='primary'/>
                            <Typography sx={{fontSize: 15}}>
                                {school.director.firstName + " " + school.director.lastName}
                            </Typography>

                        </Box>

                        <Box sx={{display: 'flex', alignItems: 'center', gap: 0.375}}>
                            <EmailIcon sx={{fontSize: 20, marginLeft: 1,}} color='primary'/>
                            <Typography sx={{fontSize: 15}}>
                                {school.director.email}
                            </Typography>
                        </Box>
                    </Box>
                </Box>
            </Box>

            <Box>
                <Box sx={{ml: 5, mt: 0.5, paddingBottom: '10px',}}>
                    {/*<ClassList/>*/}
                    <Box sx={{paddingX: '15px', mb: 0.55}}>
                        <Typography variant={"h6"} fontWeight={'bold'}>My class: 11-A</Typography>
                    </Box>

                    <Divider sx={{mt: 0.75, mb: 0.75}}/>
                    <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between", paddingX: 0.25}} gap={0.5}>
                        <Search
                            label={"First Name"}
                            // searchQuery={searchFirstName}
                            // setSearchQuery={setSearchFirstName}
                            sx={{width: '100%'}}

                        />

                        <Search
                            label={"Last Name"}
                            // searchQuery={searchLastName}
                            // setSearchQuery={setSearchLastName}
                            sx={{width: '100%'}}


                        />

                        <Search
                            label={"Email"}
                            // searchQuery={searchEmail}
                            // setSearchQuery={setSearchEmail}
                            sx={{width: '100%'}}

                        />

                    </Box>
                    <Divider sx={{mt: 0.75, mb: 0.75}}/>

                    <Stack direction={'column'} spacing={0.75} sx={{paddingX: '5px'}}>
                        {users.map(user => (
                            <Box key={user.id}>
                                <UserListBox user={user}></UserListBox>
                            </Box>
                        ))}
                    </Stack>

                    <Divider sx={{mt: 0.75, mb: 0.75}}/>


                </Box>
            </Box>
        </Box>
    );
};

export default SchoolPage;