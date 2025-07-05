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
import ClassList from "./pages/classes/ClassList";
import PetitionsListPage from "./pages/petitions/PetitionsListPage";
import PetitionPage from "./pages/petitions/PetitionPage";
import ClassPage from "./pages/classes/ClassPage";
import VotingListPage from "./pages/voting/VotingListPage";
import VotingPage from "./pages/voting/VotingPage";
import SchoolPage from "./pages/school/SchoolPage";

const InitNavigation = ({children}) => {
    const navigate = useNavigate();

    useEffect(() => {
        history.navigate = navigate;
    }, [navigate]);

    return children;
};

function App() {

    const routes = [
        { path: "/", element: <Navigate to="/petitions" replace /> },

        {path: "/petitions", element: <PetitionsListPage/>},
        {path: "/petitions/:id", element: <PetitionPage/>},

        {path: "/voting", element: <VotingListPage/>},
        {path: "/voting/:id", element: <VotingPage/>},

        {path: "/school", element: <SchoolPage/>},

        {path: "/school/classes/:id", element: <ClassPage/>},

        {path: "*", element:<NotFoundPage/>},

    ];

    return (
        <BrowserRouter>
            <InitNavigation>
                <ErrorProvider>
                    <ThemeProvider theme={theme}>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Routes>
                                {routes.map((route, index) => (
                                    // <Route element={<PrivateRoute/>} key={index}>
                                        <Route
                                            path={route.path}
                                            element={<PageContainer>{route.element}</PageContainer>}
                                        />
                                    // </Route>
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
