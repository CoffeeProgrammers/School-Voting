import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import {useError} from "../../contexts/ErrorContext";
import Loading from "../../components/layouts/Loading";
import {Box, TextField} from "@mui/material";
import FormWrapper from "../../components/layouts/FormWrapper";
import SchoolService from "../../services/base/ext/SchoolService";
import Divider from "@mui/material/Divider";
import Typography from "@mui/material/Typography";

const CreateSchools = () => {
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [name, setName] = useState('')

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const createSchool = await SchoolService.createSchool({
                name: name,
                director: {
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    password: password
                }
            });
            navigate(`/profile`);
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return <Loading/>;
    }
    return (
        <Box py={3}>
            <FormWrapper
                label={"Create school"}
                onCreate={handleSubmit}
            >
                <TextField
                    multiline
                    label="Name of school"
                    fullWidth
                    variant="outlined"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />

                <Divider sx={{width:'100%', my: 0.5}}/>
                <Typography variant="h4" sx={{fontSize: 17, mb: 0.5, ml: 0.5}}>Director</Typography>
                <Box pl={2} display={'flex'} flexDirection={'column'} gap={1}>
                    <TextField
                        multiline
                        label="First name"
                        fullWidth
                        variant="outlined"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                    />
                    <TextField
                        multiline
                        label="Last name"
                        fullWidth
                        variant="outlined"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                    />
                    <TextField
                        multiline
                        label="Email"
                        fullWidth
                        variant="outlined"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <TextField
                        multiline
                        label="Password"
                        fullWidth
                        variant="outlined"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </Box>

            </FormWrapper>
        </Box>
    );
};

export default CreateSchools;