import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import {useError} from "../../contexts/ErrorContext";
import Loading from "../../components/layouts/Loading";
import UserService from "../../services/base/ext/UserService";
import {TextField} from "@mui/material";
import CreateWrapper from "../../components/layouts/CreateWrapper";

const CreateUsers = () => {
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [role, setRole] = useState('Students')
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
                role: role,
            });
            navigate(`/control-panel`);
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
        <div>
            <CreateWrapper
                label={"Create user"}
                onCreate={handleSubmit}
            >
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

            </CreateWrapper>
        </div>
    );
};

export default CreateUsers;