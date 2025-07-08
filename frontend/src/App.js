import {BrowserRouter, Route, Routes, useNavigate} from 'react-router-dom';
import {ThemeProvider} from '@mui/material';
import {LocalizationProvider} from '@mui/x-date-pickers';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import theme from './assets/theme';
import {history} from "./utils/history";
import {useEffect, useState} from "react";
import {ErrorProvider} from "./contexts/ErrorContext";
import PageContainer from "./components/layouts/appbar_with_drawer/PageContainer";
import NotFoundPage from "./pages/not_found_page/NotFoundPage";
import PetitionsListPage from "./pages/petitions/PetitionsListPage";
import PetitionPage from "./pages/petitions/PetitionPage";
import ClassPage from "./pages/class/ClassPage";
import VotingListPage from "./pages/voting/VotingListPage";
import VotingPage from "./pages/voting/VotingPage";
import IntroductionPage from "./pages/introduction/IntroductionPage";
import SchoolPage from "./pages/school/SchoolPage";
import ControlPanel from "./pages/control_panel/ControlPanel";
import Profile from "./pages/user/Profile";
import PetitionsReviewPage from "./pages/petitions/PetitionsReviewPage";
import PrivateRoute from "./security/PrivateRoute";
import Callback from "./security/callback/Callback";
import Cookies from "js-cookie";
import CreatePetition from "./pages/petitions/CreatePetition";
import CreateVoting from "./pages/voting/CreateVoting";
import CreateClass from "./pages/class/CreateClass";
import CreateUsers from "./pages/user/CreateUsers";
import CreateSchools from "./pages/school/CreateSchools";
import UpdateUser from "./pages/user/UpdateUser";

const InitNavigation = ({children}) => {
    const navigate = useNavigate();

    useEffect(() => {
        history.navigate = navigate;
    }, [navigate]);

    return children;
};

function App() {
    const [role, setRole] = useState(Cookies.get('role'))
    const isStudent = role === 'STUDENT';
    const isTeacher = role === 'TEACHER';
    const isDirector = role === 'DIRECTOR';


    const routes = [
        {path: "/callback", element: <Callback setRole={setRole}/>},

        (isStudent || isDirector) && {path: "/petitions", element: <PetitionsListPage/>},
        (isStudent || isDirector) && {path: "/petitions/:id", element: <PetitionPage/>},
        (isStudent) && {path: "/petitions/create", element: <CreatePetition/>},


        {path: "/voting", element: <VotingListPage/>},
        {path: "/voting/:id", element: <VotingPage/>},
        {path: "/voting/create", element: <CreateVoting/>},


        isDirector && {path: "/petitions-review", element: <PetitionsReviewPage/>},

        {path: "/school", element: <SchoolPage/>},
        (isTeacher || isDirector) && {path: "/school/class/:id", element: <ClassPage/>},
        (isTeacher || isDirector) && {path: "/school/class/create", element: <CreateClass/>},

        (isTeacher || isDirector) && {path: "/control-panel", element: <ControlPanel/>},
        (isTeacher || isDirector) && {path: "/control-panel/createUsers", element: <CreateUsers/>},

        {path: "/profile", element: <Profile/>},
        {path: "/profile/update", element: <UpdateUser/>},


        {path: "*", element: <NotFoundPage/>},

    ];

    return (
        <BrowserRouter>
            <InitNavigation>
                <ErrorProvider>
                    <ThemeProvider theme={theme}>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Routes>
                                <Route path={''} element={<IntroductionPage/>}/>
                                <Route path={'/create-school'} element={<CreateSchools/>}/>
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
