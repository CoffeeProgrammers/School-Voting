import {BrowserRouter, Navigate, Route, Routes, useNavigate} from 'react-router-dom';
import {ThemeProvider} from '@mui/material';
import {LocalizationProvider} from '@mui/x-date-pickers';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import theme from './assets/theme';
import {history} from "./utils/history";
import {useEffect} from "react";
import {ErrorProvider} from "./contexts/ErrorContext";
import PageContainer from "./components/layouts/appbar_with_drawer/PageContainer";
import NotFoundPage from "./pages/not_found_page/NotFoundPage";
import PetitionsListPage from "./pages/petitions/PetitionsListPage";
import PetitionPage from "./pages/petitions/PetitionPage";
import ClassPage from "./pages/class/ClassPage";
import VotingListPage from "./pages/voting/VotingListPage";
import VotingPage from "./pages/voting/VotingPage";
import SchoolPage from "./pages/school/SchoolPage";
import ControlPanel from "./pages/control_panel/ControlPanel";
import Profile from "./pages/user/Profile";
import PetitionsReviewPage from "./pages/petitions/PetitionsReviewPage";
import PrivateRoute from "./security/PrivateRoute";
import Callback from "./security/callback/Callback";

const InitNavigation = ({children}) => {
    const navigate = useNavigate();

    useEffect(() => {
        history.navigate = navigate;
    }, [navigate]);

    return children;
};

function App() {
    const isStudent = false
    const routes = [
        {path: "/", element: <Navigate to="/petitions" replace/>},
        {path: "/callback", element: <Callback/>},

        {path: "/petitions", element: <PetitionsListPage/>},
        {path: "/petitions/:id", element: <PetitionPage/>},

        {path: "/voting", element: <VotingListPage/>},
        {path: "/voting/:id", element: <VotingPage/>},

        {path: "/petitions-review", element: <PetitionsReviewPage/>},

        {path: "/school", element: <SchoolPage/>},
        !isStudent && {path: "/school/class/:id", element: <ClassPage/>},

        !isStudent && {path: "/control-panel", element: <ControlPanel/>},

        {path: "/profile", element: <Profile/>},

        {path: "*", element: <NotFoundPage/>},

    ];

    return (
        <BrowserRouter>
            <InitNavigation>
                <ErrorProvider>
                    <ThemeProvider theme={theme}>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Routes>
                                {routes.map((route, index) => (
                                    <Route element={<PrivateRoute/>} key={index}>
                                        <Route
                                            path={route.path}
                                            element={<PageContainer>{route.element}</PageContainer>}
                                        />
                                    </Route>
                                ))}
                            </Routes>
                        </LocalizationProvider>
                    </ThemeProvider>
                </ErrorProvider>
            </InitNavigation>
        </BrowserRouter>
    );
}

export default App;
