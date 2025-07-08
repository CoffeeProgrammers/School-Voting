import React, {useState} from 'react';
import FormWrapper from "../../components/layouts/FormWrapper";
import {TextField} from "@mui/material";
import {useError} from "../../contexts/ErrorContext";
import {useLocation, useNavigate} from "react-router-dom";
import Loading from "../../components/layouts/Loading";
import VotingService from "../../services/base/ext/VotingService";
import Divider from "@mui/material/Divider";
import CreateAnswers from "../../components/basic/voting/CreateAnswers";

const UpdateVoting = () => {
    const location = useLocation();

    const voting = location.state?.state;

    const [name, setName] = useState(voting.name || '')
    const [description, setDescription] = useState(voting.description || '')
    const [answers, setAnswers] = useState(voting.answers || [
        'Yes',
        'No'
    ])

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const createdVoting = await VotingService.updateVoting(voting.id, {
                name: name,
                description: description,
                answers: answers
            });
            navigate(`/voting/${createdVoting.id}`);
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false);
        }
    }

    const addAnswer = (answer) => {
        setAnswers(prev => [...prev, answer])
    }

    const removeAnswer = (index) => {
        setAnswers(prev => prev.filter((_, i) => i !== index))
    }

    if (loading) {
        return <Loading/>;
    }
    return (
        <div>
            <FormWrapper
                label={"Create voting"}
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

                <Divider/>

                <CreateAnswers
                    addAnswer={addAnswer}
                    deleteAnswer={removeAnswer}
                    answersList={answers}
                />

                <Divider/>

            </FormWrapper>
        </div>
    );
};

export default UpdateVoting;