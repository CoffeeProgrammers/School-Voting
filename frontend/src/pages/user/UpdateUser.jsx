import React, {useState} from 'react';
import UserService from "../../services/base/ext/UserService";
import {useLocation, useNavigate} from "react-router-dom";
import {useError} from "../../contexts/ErrorContext";
import Loading from "../../components/layouts/Loading";
import {TextField} from "@mui/material";
import FormWrapper from "../../components/layouts/FormWrapper";

const UpdateUser = () => {
    const location = useLocation();

    const user = location.state?.state;

    const [firstName, setFirstName] = useState(user.firstName || '')
    const [lastName, setLastName] = useState(user.lastName || '')

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const updatedUser = await UserService.updateUser(user.id, {
                firstName: firstName,
                lastName: lastName,
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
        <div>
            <FormWrapper
                label={"Update user"}
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

            </FormWrapper>
        </div>
    )
};

export default UpdateUser;