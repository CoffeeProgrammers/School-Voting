import React, {useState} from 'react';
import CreateWrapper from "../../components/layouts/CreateWrapper";
import ClassService from "../../services/base/ext/ClassService";
import {useNavigate} from "react-router-dom";
import {useError} from "../../contexts/ErrorContext";
import {TextField} from "@mui/material";
import AddStudentsToClassDialog from "../../components/basic/user/AddStudentsToClassDialog";
import Loading from "../../components/layouts/Loading";

const CreateClass = () => {
    const [name, setName] = useState('')
    const [userIds, setUserIds] = useState([])

    const navigate = useNavigate()
    const {showError} = useError()
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const createdClass = await ClassService.createClass({
                name: name,
                userIds: userIds,
            });
            navigate(`/school/class/${createdClass.id}`);
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

    if (loading) {
        return <Loading/>;
    }

    return (
        <div>
            <CreateWrapper
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

                <AddStudentsToClassDialog userIds={userIds} selectedIds={userIds} toggleUserId={toggleUserId}/>
            </CreateWrapper>
        </div>
    );
};

export default CreateClass;