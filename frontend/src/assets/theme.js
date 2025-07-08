import {createTheme} from "@mui/material/styles";
import "@fontsource/open-sans";
import {blue, blueGrey} from "@mui/material/colors";

const theme = createTheme({
    palette: {
        primary: {
            main: blueGrey[600],
            dark: blueGrey[700],
            light: blueGrey[300]

        },
        secondary: {
            main: '#D8A25E',
            dark: '#9c7545',
            light: '#f6e880'
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
        MuiTypography: {
            styleOverrides: {
                root: {
                    wordBreak: "break-word",
                },
            },
        },
        MuiLink: {
            styleOverrides: {
                root: {
                    textDecoration: 'none',
                    color: blueGrey[600],
                    fontWeight: 500,
                    transition: 'color 0.2s ease',
                    '&:hover': {
                        textDecoration: 'none',
                        color: blue[500],
                    },
                    '&:active': {
                        color: blue[500],
                    }
                },
            },
            defaultProps: {
                underline: 'hover',
            },
        }
    },
});

export default theme;
