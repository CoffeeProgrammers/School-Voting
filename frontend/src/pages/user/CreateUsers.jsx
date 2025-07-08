import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import {useError} from "../../contexts/ErrorContext";
import Loading from "../../components/layouts/Loading";
import UserService from "../../services/base/ext/UserService";
import {Button, Checkbox, FormControlLabel, Stack, TextField} from "@mui/material";
import CreateWrapper from "../../components/layouts/CreateWrapper";
import Typography from "@mui/material/Typography";
import theme from "../../assets/theme";
import Divider from "@mui/material/Divider";
import Cookies from "js-cookie";
import Box from "@mui/material/Box";

const CreateUsers = () => {
    const isDirector = Cookies.get('role') === 'DIRECTOR'

    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [role, setRole] = useState('Student')
    const [isVerified, setIsVerified] = useState(false)

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const createdUser = await UserService.createUser({
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password,
                role: role.toUpperCase(),
                emailVerified: isVerified,
            });
            navigate(`/control-panel`);
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false);
        }
    }

    const renderTabButton = (tabRole, width) => {
        return (
            <Button onClick={() => setRole(tabRole)} sx={{height: 33, borderRadius: 0, width: width}}>
                <Typography variant='body1' color={role === tabRole ? 'primary' : 'text.secondary'}
                            sx={{
                                borderBottom: "2.5px solid",
                                borderBottomColor: role === tabRole ? theme.palette.primary.main : "transparent",
                            }}>
                    {tabRole}s
                </Typography>
            </Button>
        )
    }

    if (loading) {
        return <Loading/>;
    }
    return (
        <div>
            <CreateWrapper
                label={"Create user"}
                onCreate={handleSubmit}
            >
                <Stack direction="row" width={'100%'} ml={1.25}>
                    {renderTabButton('Student', 100)}
                    {renderTabButton('Parent', 100)}
                    {isDirector && (
                        renderTabButton('Teacher', 100)
                    )}
                </Stack>

                <Divider/>

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
                <Box px={2}>
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={isVerified}
                                onChange={(e) => setIsVerified(e.target.checked)}
                                color="primary"
                            />
                        }
                        label="Verified email"
                    />
                </Box>

            </CreateWrapper>
        </div>
    );
};

export default CreateUsers;