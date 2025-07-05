import {styled} from "@mui/material/styles";
import IconButton from "@mui/material/IconButton";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import Divider from "@mui/material/Divider";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import * as React from "react";
import MuiDrawer from "@mui/material/Drawer";
import DrawerNavigationButton from "./DrawerNavigationButton";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import theme from "../../../assets/theme";
import BalanceRoundedIcon from '@mui/icons-material/BalanceRounded';
import HistoryEduRoundedIcon from '@mui/icons-material/HistoryEduRounded';
import Groups2Icon from '@mui/icons-material/Groups2';
import HomeWorkIcon from '@mui/icons-material/HomeWork';

const drawerWidth = 240;

const openedMixin = (theme) => ({
    width: drawerWidth,
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    overflowX: 'hidden',
});

const closedMixin = (theme) => ({
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: `calc(${theme.spacing(7)} + 1px)`,
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(8)} + 1px)`,
    },
});

const DrawerHeader = styled('div')(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
}));

const MUIStyledDrawer = styled(MuiDrawer, {shouldForwardProp: (prop) => prop !== 'open'})(
    ({theme}) => ({
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
        boxSizing: 'border-box',
        variants: [
            {
                props: ({open}) => open,
                style: {
                    ...openedMixin(theme),
                    '& .MuiDrawer-paper': openedMixin(theme),
                },
            },
            {
                props: ({open}) => !open,
                style: {
                    ...closedMixin(theme),
                    '& .MuiDrawer-paper': closedMixin(theme),
                },
            },
        ],
    }),
);



const Drawer = ({open, handleDrawerClose}) => {

    const navigation = [
        {type: "navigation", icon: <HistoryEduRoundedIcon sx={{fontSize: 28, ml: -0.25}}/>, title: "Petitions", path: "/petitions"},
        {type: "navigation", icon: <BalanceRoundedIcon sx={{fontSize: 25}}/>, title: "Voting", path: "/voting"},
        {type: "divider"},
        {type: "navigation", icon: <HomeWorkIcon sx={{fontSize: 22, ml: 0.2}}/>, title: "School", path: "/school"},
        {type: "divider"},
        {type: "navigation", icon: <AccountCircleIcon/>, title: "Profile", path: "/profile"},
    ]

    return (
        <MUIStyledDrawer variant="permanent" open={open}>
            <DrawerHeader>
                {open && <IconButton onClick={handleDrawerClose}>
                    {theme.direction === 'rtl' ? <ChevronRightIcon/> : <ChevronLeftIcon/>}
                </IconButton>}

            </DrawerHeader>
            <Divider/>
            <List>
                {navigation.map((obj, index) => (
                    obj.type === "navigation" ? (
                        <ListItem key={index} disablePadding sx={{display: 'block'}}>
                           <DrawerNavigationButton open={open} obj={obj}/>
                        </ListItem>
                    ) : ( obj.type === "divider" && (
                            <Divider key={index}/>
                        )
                    )
                ))}
            </List>
        </MUIStyledDrawer>
    )
}

export default Drawer;