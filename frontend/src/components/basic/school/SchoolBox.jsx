import React, {useState} from 'react';
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import HomeWorkIcon from "@mui/icons-material/HomeWork";
import {blueGrey} from "@mui/material/colors";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import EmailIcon from "@mui/icons-material/Email";
import NotesRoundedIcon from '@mui/icons-material/NotesRounded';
import EditButton from "../../layouts/EditButton";
import DeleteButton from "../../layouts/DeleteButton";
import Cookies from "js-cookie";
import SchoolService from "../../../services/base/ext/SchoolService";
import {useNavigate} from "react-router-dom";
import {useError} from "../../../contexts/ErrorContext";

const SchoolBox = ({school}) => {

    const navigate = useNavigate();
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleDelete = async () => {
        try {
            setLoading(true)
            await SchoolService.deleteSchool(Cookies.get("schoolId"));
            navigate("/", {replace: true});
        } catch (error) {
            showError(error);
        }finally {
            setLoading(false);
        }
    };

    return (
        <Box sx={{
            border: '1px solid',
            borderColor: blueGrey[100],
            borderRadius: '5px',
            backgroundColor: blueGrey[50],
            paddingX: '20px',
            paddingY: '30px',
            paddingBottom: "15px",
            widths: '100%',
        }}>

            <Box mb={1} sx={{display: 'flex', flexDirection: 'column', alignItems: 'center',}}>
                <HomeWorkIcon color='primary' sx={{fontSize: 140,}}/>
                <Typography variant='h4' fontWeight='bold'>School</Typography>
            </Box>
            {Cookies.get("role") === "TEACHER" || Cookies.get("role") === "DIRECTOR" ? (
                <Box display="flex" alignItems="center" justifyContent={'center'} gap={1} mb={1}>
                    <DeleteButton
                        text={'Are you sure you want to delete this school?'}
                        deleteFunction={handleDelete}
                        fontSize={20}
                    />

                    <EditButton path={'update'} state={school}/>
                </Box>) : ""}
            <Box marginX={0.5} display='flex' flexDirection='column' justifyContent='center'>
                <Typography sx={{fontSize: 15, display: 'flex', justifyContent: 'center'}}
                            fontWeight='bold'>Name:</Typography>
                <Box sx={{display: 'flex', gap: 0.375}}>
                    <NotesRoundedIcon sx={{fontSize: 18, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 14.75}}>{school.name}</Typography>
                </Box>
                <Typography sx={{fontSize: 15, display: 'flex', justifyContent: 'center'}}
                            fontWeight='bold'> Director:</Typography>

                <Box sx={{display: 'flex', gap: 0.375}}>
                    <AccountCircleIcon sx={{fontSize: 20, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 14.75}}>
                        {school.director.firstName + " " + school.director.lastName}
                    </Typography>
                </Box>

                <Box sx={{display: 'flex', gap: 0.375}}>
                    <EmailIcon sx={{fontSize: 20, marginLeft: 1, mt: 0.3}} color='primary'/>
                    <Typography sx={{fontSize: 14.75,}}>
                        {school.director.email}
                    </Typography>
                </Box>
            </Box>
        </Box>
    );
};

export default SchoolBox;