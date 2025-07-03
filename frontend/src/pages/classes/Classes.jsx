import React from 'react';
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Search from "../../components/layouts/list/Search";
import {Button, Stack} from "@mui/material";
import Class from "../../components/Class";


const Classes = () => {
    const schoolClassesCombined = [
        {
            className: "1-А", // Об'єднана цифра і літера
            numberOfStudents: 28,
            classTeacher: "Олена Іванівна Шевченко",
            specialization: "Загальноосвітній",
            isPrimary: true
        },
        {
            className: "5-Б",
            numberOfStudents: 25,
            classTeacher: "Ігор Петрович Ковальчук",
            specialization: "Поглиблене вивчення англійської мови",
            isPrimary: false
        },
        {
            className: "9-В",
            numberOfStudents: 22,
            classTeacher: "Наталія Олегівна Бондаренко",
            specialization: "Фізико-математичний профіль",
            isPrimary: false
        },
        {
            className: "11-А",
            numberOfStudents: 20,
            classTeacher: "Сергій Миколайович Мельник",
            specialization: "Гуманітарний профіль",
            isPrimary: false
        },
        {
            className: "7-Г",
            numberOfStudents: 27,
            classTeacher: "Лариса Вікторівна Поліщук",
            specialization: "Загальноосвітній",
            isPrimary: false
        }
    ];

    const classes = schoolClassesCombined.map((clas, index) => ({
        ...clas ,
        id: clas .id || `clas-${index}`
    }));

    return(
        <>
            <Stack direction="row" sx={{alignItems: 'center', display: "flex", justifyContent: "space-between "}}>
                <Typography variant="h6" fontWeight={'bold'}>Classes</Typography>
                <Box sx={{alignItems: 'center', display: "flex", justifyContent: "space-between"}} gap={0.5}>
                    <Search
                        // label={"Title"}
                        // searchQuery={searchName}
                        // setSearchQuery={setSearchName}
                        // sx={{mr: 1.5}}
                    />
                    <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>Create a class
                    </Button>
                </Box>
            </Stack>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, marginTop:'20px'}}>
                {
                    classes.map(clas =>(
                        <Class name={clas.className}></Class>
                    ))
                }
            </Box>
        </>
    )
};

export default Classes;