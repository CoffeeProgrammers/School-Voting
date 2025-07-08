import React, {useState} from 'react';
import {useLocation, useNavigate} from "react-router-dom";
import {useError} from "../../contexts/ErrorContext";
import Loading from "../../components/layouts/Loading";
import {TextField} from "@mui/material";
import FormWrapper from "../../components/layouts/FormWrapper";
import SchoolService from "../../services/base/ext/SchoolService";
import Cookies from "js-cookie";

const UpdateSchool = () => {
    const location = useLocation();

    const school = location.state?.state;
    const schoolId = Cookies.get("schoolId");

    const [name, setName] = useState(school.name || '')

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            console.log(school)
            setLoading(true);
            const updatedSchool = await SchoolService.updateSchool(schoolId, {
                name: name,
            });
            navigate(`/school`);
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
                label={"Update school"}
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
            </FormWrapper>
        </div>
    )
};

export default UpdateSchool;