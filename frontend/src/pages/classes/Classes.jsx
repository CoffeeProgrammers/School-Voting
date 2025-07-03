import React from 'react';
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Search from "../../components/layouts/list/Search";
import {Button, Grid, Stack} from "@mui/material";
import ClassListBox from "../../components/basic/class/ClassListBox";
import Divider from "@mui/material/Divider";


const Classes = () => {
    const schoolClassesCombined = [
        {
            id: 1, // Унікальний ідентифікатор
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
        },
        {
            id: 11,
            className: "1-А",
            numberOfStudents: 28,
            classTeacher: "Олена Іванівна Шевченко",
            specialization: "Загальноосвітній",
            isPrimary: true
        },
        {
            id: 12,
            className: "5-Б",
            numberOfStudents: 25,
            classTeacher: "Ігор Петрович Ковальчук",
            specialization: "Поглиблене вивчення англійської мови",
            isPrimary: false
        },
        {
            id: 13,
            className: "9-В",
            numberOfStudents: 22,
            classTeacher: "Наталія Олегівна Бондаренко",
            specialization: "Фізико-математичний профіль",
            isPrimary: false
        },
        {
            id: 14,
            className: "11-А",
            numberOfStudents: 20,
            classTeacher: "Сергій Миколайович Мельник",
            specialization: "Гуманітарний профіль",
            isPrimary: false
        },
        {
            id: 15,
            className: "7-Г",
            numberOfStudents: 27,
            classTeacher: "Лариса Вікторівна Поліщук",
            specialization: "Загальноосвітній",
            isPrimary: false
        },
        {
            id: 16,
            className: "1-А",
            numberOfStudents: 28,
            classTeacher: "Олена Іванівна Шевченко",
            specialization: "Загальноосвітній",
            isPrimary: true
        },
        {
            id: 17,
            className: "5-Б",
            numberOfStudents: 25,
            classTeacher: "Ігор Петрович Ковальчук",
            specialization: "Поглиблене вивчення англійської мови",
            isPrimary: false
        },
        {
            id: 18,
            className: "9-В",
            numberOfStudents: 22,
            classTeacher: "Наталія Олегівна Бондаренко",
            specialization: "Фізико-математичний профіль",
            isPrimary: false
        },
        {
            id: 19,
            className: "11-А",
            numberOfStudents: 20,
            classTeacher: "Сергій Миколайович Мельник",
            specialization: "Гуманітарний профіль",
            isPrimary: false
        },
        {
            id: 20,
            className: "7-Г",
            numberOfStudents: 27,
            classTeacher: "Лариса Вікторівна Поліщук",
            specialization: "Загальноосвітній",
            isPrimary: false
        }
    ]



    return (
        <>
            <Stack direction="row" sx={{alignItems: 'center', display: "flex", justifyContent: "space-between "}}>
                <Typography variant="h6" fontWeight={'bold'}>Classes</Typography>
                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.5}>
                    <Search
                        // label={"Title"}
                        // searchQuery={searchName}
                        // setSearchQuery={setSearchName}
                        sx={{mr: 1}}
                    />
                    <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                        Create a class
                    </Button>
                </Box>
            </Stack>

            <Divider sx={{mt: 0.75, mb: 0.75}}/>


            <Grid container rowSpacing={1} columnSpacing={0.7}  paddingX={1}>
                {schoolClassesCombined.map(studentsClass => (
                    <Grid size={{xs: 12, sm: 6, md: 4, lg: 6}} key={studentsClass.id}>
                        <ClassListBox  studentClass={studentsClass}></ClassListBox>
                    </Grid>
                ))}
            </Grid>
        </>
    )
};

export default Classes;