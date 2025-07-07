import React from 'react';
import {Outlet, useLocation} from 'react-router-dom';
import {useAuth} from './useAuth';
import AuthService from "../services/auth/AuthService";

const PrivateRoute = () => {
    const {isAuthenticated} = useAuth();
    const location = useLocation();

    if (location.pathname === "/callback") {
        return <Outlet/>;
    }

    return isAuthenticated() ? <Outlet/> : AuthService.refresh();
};

export default PrivateRoute;
