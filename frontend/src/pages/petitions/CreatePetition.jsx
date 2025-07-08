import React, {useState} from 'react';
import FormWrapper from "../../components/layouts/FormWrapper";
import {TextField} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import {useError} from "../../contexts/ErrorContext";
import {useNavigate} from "react-router-dom";
import Loading from "../../components/layouts/Loading";
import PetitionService from "../../services/base/ext/PetitionService";

const CreatePetition = () => {
    const [name, setName] = useState('')
    const [description, setDescription] = useState('')
    const [levelType, setLevelType] = useState('')

    const levelTypeOptions = [
        {value: 'SCHOOL', label: 'School'},
        {value: 'CLASS', label: 'Class'},
    ];

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const createdPetition = await PetitionService.createPetition({
                name: name,
                description: description,
                levelType: levelType,
            });
            navigate(`/petitions/${createdPetition.id}`);
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
                <TextField
                    multiline
                    rows={4}
                    label="Description"
                    fullWidth
                    variant="outlined"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
                <TextField
                    label="Level type"
                    fullWidth
                    variant="outlined"
                    select
                    value={levelType}
                    onChange={(e) => setLevelType(e.target.value)}
                >
                    {levelTypeOptions.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>

                    ))}
                </TextField>
            </FormWrapper>
        </div>
    );
};

export default CreatePetition;