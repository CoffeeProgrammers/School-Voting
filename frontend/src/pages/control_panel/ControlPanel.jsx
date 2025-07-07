import React, {useState} from 'react';
import {Box, Button, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import UserList from "../../components/basic/user/UserList";
import theme from "../../assets/theme";
import BuildCircleOutlinedIcon from "@mui/icons-material/BuildCircleOutlined";

const users = [
    {id: 1, firstName: "Alice", lastName: "Johnson", email: "alice.johnson@example.com"},
    {id: 2, firstName: "Bob", lastName: "Smith", email: "bob.smith@example.com"},
    {id: 3, firstName: "Charlie", lastName: "Brown", email: "charlie.brown@example.com"},
    {id: 4, firstName: "Diana", lastName: "Williams", email: "diana.williams@example.com"},
    {id: 5, firstName: "Ethan", lastName: "Davis", email: "ethan.davis@example.com"},
    {id: 6, firstName: "Fiona", lastName: "Clark", email: "fiona.clark@example.com"},
    {id: 7, firstName: "George", lastName: "Miller", email: "george.miller@example.com"},
    {id: 8, firstName: "Hannah", lastName: "Taylor", email: "hannah.taylor@example.com"},
    {id: 9, firstName: "Ian", lastName: "Anderson", email: "ian.anderson@example.com"},
    {id: 10, firstName: "Julia", lastName: "Thomas", email: "julia.thomas@example.com"}
];

const ControlPanel = () => {
    const [tab, setTab] = useState("Students")

    const renderTabButton = (title, width) => {
        return (
            <Button onClick={() => setTab(title)} sx={{height: 33, borderRadius: 0, width: width}}>
                <Typography variant='body1' color={tab === title ? 'primary' : 'text.secondary'}
                            sx={{
                                borderBottom: "2.5px solid",
                                borderBottomColor: tab === title ? theme.palette.primary.main : "transparent",
                            }}>
                    {title}
                </Typography>
            </Button>
        )
    }

    const renderTab = () => {
        switch (tab) {
            case 'Students':
                return <UserList users={users} actions={true}/>
            case 'Teachers':
                return <UserList users={users} actions={true}/>
        }
    }

    return (
        <Box>
            <Box sx={{border: '1px solid #ddd', borderRadius: '5px', marginY: '10px'}}>
                <Box sx={{marginTop: '15px'}}>
                    <Box sx={{paddingX: '15px', mb: 0.55, display: 'flex', alignItems: 'center' }}>
                        <BuildCircleOutlinedIcon color="primary" sx={{fontSize: 29, marginRight: 0.5}}/>
                        <Typography variant={"h5"} fontWeight={'bold'}>Control panel</Typography>
                    </Box>

                    <Stack direction="row" width={'100%'} ml={1.25}>
                        {renderTabButton('Students', 110)}
                        {renderTabButton('Teachers', 105)}
                    </Stack>
                    <Divider/>

                    {renderTab()}
                </Box>
            </Box>
        </Box>
    );
};

export default ControlPanel;