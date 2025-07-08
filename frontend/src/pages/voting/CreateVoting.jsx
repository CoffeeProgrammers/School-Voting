import React, {useState} from 'react';
import FormWrapper from "../../components/layouts/FormWrapper";
import {TextField} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import {useError} from "../../contexts/ErrorContext";
import {useNavigate} from "react-router-dom";
import Loading from "../../components/layouts/Loading";
import Cookies from "js-cookie";
import VotingService from "../../services/base/ext/VotingService";
import DefaultDateTimePicker from "../../components/layouts/DefaultDateTimePicker";
import AddUsersToVotingDialog from "../../components/basic/user/AddUsersToVotingDialog";
import Divider from "@mui/material/Divider";
import CreateAnswers from "../../components/basic/voting/CreateAnswers";

const CreateVoting = () => {
    const role = Cookies.get('role')
    const isStudent = role === 'STUDENT'
    const isTeacher = role === 'TEACHER'
    const isParent = role === 'PARENT'

    const [name, setName] = useState('')
    const [description, setDescription] = useState('')
    const [startTime, setStartTime] = useState()
    const [endTime, setEndTime] = useState()
    const [levelType, setLevelType] = useState('')
    const [userIds, setUserIds] = useState([])
    const [answers, setAnswers] = useState([
        'Yes',
        'No'
    ])

    const levelTypeOptions = [
        {value: 'SCHOOL', label: 'School'},
        isStudent && {value: 'CLASS', label: 'Class'},
        // isTeacher && {value: 'GROUP_OF_TEACHERS', label: 'Group of Teachers'},
        // (isStudent || isParent) && {value: 'GROUP_OF_PARENTS_AND_STUDENTS', label: 'Group of Parents and Students'}
    ].filter(Boolean);

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            console.log({
                name: name,
                description: description,
                levelType: levelType,
                startTime: startTime,
                endTime: endTime,
                answers: answers,
                targetIds: userIds

            })
            const createdVoting = await VotingService.createVoting({
                name: name,
                description: description,
                levelType: levelType,
                startTime: startTime,
                endTime: endTime,
                answers: answers,
                targetIds: userIds

            });
            navigate(`/voting/${createdVoting.id}`);
        } catch (error) {
            showError(error);
        } finally {
            setLoading(false);
        }
    }

    const toggleUserId = (userId) => {
        setUserIds(prev =>
            prev.includes(userId)
                ? prev.filter(id => id !== userId)
                : [...prev, userId]
        );
    };

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
                <DefaultDateTimePicker
                    label={"Start time"}
                    value={startTime}
                    setValue={setStartTime}
                />
                <DefaultDateTimePicker
                    label={"End time"}
                    value={endTime}
                    setValue={setEndTime}
                />

                <Divider/>

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
                {(levelType === 'GROUP_OF_TEACHERS' || levelType === 'GROUP_OF_PARENTS_AND_STUDENTS') && (
                    <AddUsersToVotingDialog
                        userIds={userIds}
                        toggleUserId={toggleUserId}
                        selectedIds={userIds}
                        levelType={levelType}
                    />
                )}
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

export default CreateVoting;