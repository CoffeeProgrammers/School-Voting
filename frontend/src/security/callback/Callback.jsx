import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import Cookies from "js-cookie";
import Loading from "../../components/layouts/Loading";

const Callback = ({setRole}) => {
    const navigate = useNavigate();

    const role = Cookies.get('role')


    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');
        console.log("HERE")
        if (code) {
            fetch('http://localhost:8081/api/auth/callback', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({code}),
                credentials: 'include'
            }).then(res => {
                if (res.ok) {
                    setRole(Cookies.get('role'))
                    (role === 'STUDENT' || role === 'DIRECTOR') ? navigate('/petitions') : navigate('/voting');
                } else {
                    alert('Помилка авторизації');
                }
            });
        }
    }, [navigate, role]);

    return <Loading/>;
};

export default Callback;
