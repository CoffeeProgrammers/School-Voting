import {BrowserRouter, Route, Routes, useNavigate} from 'react-router-dom';
import {ThemeProvider} from '@mui/material';
import {LocalizationProvider} from '@mui/x-date-pickers';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import theme from './assets/theme';
import {history} from "./utils/history";
import {useEffect} from "react";
import {ErrorProvider} from "./contexts/ErrorContext";
import PageContainer from "./components/layouts/appbar_with_drawer/PageContainer";
import First from "./pages/First";
import NotFoundPage from "./pages/not_found_page/NotFoundPage";

const InitNavigation = ({children}) => {
    const navigate = useNavigate();

    useEffect(() => {
        history.navigate = navigate;
    }, [navigate]);

    return children;
};

function App() {

    const routes = [
        {path: "/first", element: <First/>},

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
