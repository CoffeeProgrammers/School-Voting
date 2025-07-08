import React, {useState} from 'react';
import FormWrapper from "../../components/layouts/FormWrapper";
import {useLocation, useNavigate} from "react-router-dom";
import {useError} from "../../contexts/ErrorContext";
import {TextField} from "@mui/material";
import Loading from "../../components/layouts/Loading";
import ClassService from "../../services/base/ext/ClassService";

const UpdateClass = () => {
    const location = useLocation();

    const classM = location.state?.state;

    const [name, setName] = useState(classM.name || '')

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const updatedSchool = await ClassService.updateClass(classM.id, {
                name: name,
            });
            navigate(`/school/class/${classM.id}`);
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return <Loading/>;
    }

    if (loading) {
        return <Loading/>;
    }

    return (
        <div>
            <FormWrapper
                label={"Create petition"}
                onCreate={handleSubmit}
            >
                <TextField
                    multiline
                    label="Name"
                    fullWidth
                    variant="outlined"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </FormWrapper>
        </div>
    );
};

export default UpdateClass;