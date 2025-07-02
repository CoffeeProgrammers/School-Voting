import {createTheme} from "@mui/material/styles";
import "@fontsource/open-sans";
import {blueGrey, brown} from "@mui/material/colors";

const theme = createTheme({
    palette: {
        primary: {
            // main: '#ad1457',
            // dark: '#880e4f',
            // light: '#d81b60'
            main: blueGrey[600],
            dark: blueGrey[700],
            light: blueGrey[300]


        },
        secondary: {
            main: '#D8A25E',
            dark: '#9c7545',
            light: '#EEDF7A'
        },
    },
    typography: {
        fontFamily: "Open Sans, Arial, sans-serif",
    },
    components: {
        MuiAppBar: {
            styleOverrides: {
                colorDefault: ({theme}) => ({
                    backgroundColor:
                        theme.palette.mode === 'dark'
                            ? '#000000'
                            : '#fff',
                }),
            },
        },
        MuiButton: {
            styleOverrides: {
                root: {
                    textTransform: "none"
                },
            },
        },
    },
});

export default theme;
