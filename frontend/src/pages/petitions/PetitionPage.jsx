import React, {useState} from 'react';
import {useParams} from "react-router-dom";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Divider from "@mui/material/Divider";
import Groups2Icon from '@mui/icons-material/Groups2';
import {Button, Stack} from "@mui/material";
import {CustomPieChart} from "../../components/layouts/statistics/CustomPieChart";
import Utils from "../../utils/Utils";
import {blueGrey} from "@mui/material/colors";
import ThumbUpAltIcon from '@mui/icons-material/ThumbUpAlt';
import theme from "../../assets/theme";

const petition = {
    id: 1,
    name: "Increase funding for school libraries",
    description: "This petition calls for increased budget allocation to school libraries in order to enhance access to educational resources for students. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab alias\n" +
        "                        aperiam\n" +
        "                        aspernatur dolorem, ducimus eaque earum esse ex minima, necessitatibus quaerat quas\n" +
        "                        reprehenderit\n" +
        "                        vero. Aperiam dolores ex maiores! Omnis, quibusdam. Lorem ipsum dolor sit amet, consectetur\n" +
        "                        adipisicing elit. Et tenetur, voluptatibus? Culpa dignissimos doloremque labore optio, possimus\n" +
        "                        repellat saepe soluta? Itaque minus sit voluptates. Doloribus error iure modi possimus veniam.\n" +
        "                        Lorem\n" +
        "                        ipsum dolor sit amet, consectetur adipisicing elit. Lorem ipsum dolor sit amet, consectetur\n" +
        "                        adipisicing elit. Ab alias aperiam aspernatur dolorem, ducimus eaque earum esse ex minima,\n" +
        "                        necessitatibus quaerat quas reprehenderit vero. Aperiam dolores ex maiores! Omnis, quibusdam.\n" +
        "                        Lorem\n" +
        "                        ipsum dolor sit amet, consectetur adipisicing elit. Et tenetur, voluptatibus? Culpa dignissimos\n" +
        "                        doloremque labore optio, possimus repellat saepe soluta? Itaque minus sit voluptates. Doloribus\n" +
        "                        error iure modi possimus veniam. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Co\n" +
        "                        Lorem\n" +
        "                        ipsum dolor sit amet, consectetur adipisicing elit. Ab alias aperiam aspernatur dolorem, ducimus\n" +
        "                        eaque earum esse ex minima, necessitatibus quaerat quas reprehenderit vero. Aperiam dolores ex\n" +
        "                        maiores! Omnis, quibusdam. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Et tenetur,\n" +
        "                        voluptatibus? Culpa dignissimos doloremque labore optio, possimus repellat saepe soluta? Itaque\n" +
        "                        minus sit voluptates. Doloribus error iure modi possimus veniam. Lorem ipsum dolor sit amet,\n" +
        "                        consectetur adipisicing elit. Co Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab\n" +
        "                        alias\n" +
        "                        aperiam aspernatur dolorem, ducimus eaque earum esse ex minima, necessitatibus quaerat quas\n" +
        "                        reprehenderit vero. Aperiam dolores ex maiores! Omnis, quibusdam. Lorem ipsum dolor sit amet,\n" +
        "                        consectetur adipisicing elit. Et tenetur, voluptatibus? Culpa dignissimos doloremque labore\n" +
        "                        optio,\n" +
        "                        possimus repellat saepe soluta? Itaque minus sit voluptates. Doloribus error iure modi possimus\n" +
        "                        veniam. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Co Lorem ipsum dolor sit amet,\n" +
        "                        consectetur adipisicing elit. Ab alias aperiam aspernatur dolorem, ducimus eaque earum esse ex\n" +
        "                        minima, necessitatibus quaerat quas reprehenderit vero. Aperiam dolores ex maiores! Omnis,\n" +
        "                        quibusdam. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Et tenetur, voluptatibus?\n" +
        "                        Culpa\n" +
        "                        dignissimos doloremque labore optio, possimus repellat saepe soluta? Itaque minus sit\n" +
        "                        voluptates.\n" +
        "                        Doloribus error iure modi possimus veniam. Lorem ipsum dolor sit amet, consectetur adipisicing\n" +
        "                        elit.\n" +
        "                        CoCommodi consectetur natus nemo quisquam quod rem similique sint sit tempore voluptatum. A\n" +
        "                        cumque\n" +
        "                        eligendi excepturi libero, officiis qui similique ut. Numquam? Lorem ipsum dolor sit amet,\n" +
        "                        consectetur adipisicing elit. Cupiditate magni nisi sequi. Dicta ex illo illum labore nostrum?\n" +
        "                        Beatae iusto officiis perferendis perspiciatis ratione, tempora. A laborum molestias natus\n" +
        "                        tenetur?",
    endTime: "2025-09-30T23:59:59",
    levelType: "class",
    status: "ACTIVE",
    countSupport: 35,
    countNeeded: 100,
    supportedByCurrentUser: true,
    creator: {
        id: 10,
        email: "jane.doe@example.com",
        firstName: "Jane",
        lastName: "Doe"
    }
};

const PetitionPage = () => {
    const {petitionId} = useParams();

    const [tab, setTab] = useState("Description")

    const viewDate = Utils.getDaysLeft(petition.endTime) + ' days left';

    const renderSuccessSupportButton = () => {
        return (
            <Button disabled startIcon={<ThumbUpAltIcon color='success'/>} variant="contained"
                    color="success"
                    sx={{
                        height: 32,
                        borderRadius: 10,
                        fontWeight: 500,
                        "&.Mui-disabled": {
                            backgroundColor: blueGrey[50],
                            color: theme => theme.palette.success.main,
                            opacity: 1,
                        }
                    }}>
                Supported
            </Button>
        )
    }

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
            case 'Description':
                return (
                    <Typography variant='body1' mt={1.25}>
                        {petition.description}
                    </Typography>)
            case 'Comments':
                return <Box mt={2.5}>Comments</Box>
        }
    }
    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: '3fr 1.3fr',
            paddingX: 5,
            paddingTop: 2,
            paddingBottom: 4
        }}>
            <Box paddingRight={4} mt={4.5}>
                <Typography variant='h4'>
                    {petition.name}
                </Typography>


                <Stack direction='column' sx={{marginBottom: 4, marginTop: 1.2, gap: 0.5, paddingX: 1}}>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <AccountCircleIcon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.creator.firstName + " " + petition.creator.lastName}
                        </Typography>
                        <Divider orientation="vertical" sx={{height: 15, color: 'black', marginX: 0.5}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.creator.email}
                        </Typography>

                    </Box>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5,}}>
                        <Groups2Icon sx={{fontSize: 20, color: 'primary'}}/>
                        <Typography color='primary' sx={{fontSize: 13}}>
                            {petition.levelType === 'class' ? 'Class' : 'School'}
                        </Typography>
                    </Box>
                </Stack>

                <Stack direction="row" width={'100%'}>
                    {renderTabButton('Description', 105)}
                    {renderTabButton('Comments', 100)}
                </Stack>

                <Divider/>

                <Box paddingX={1}>
                    {renderTab()}
                </Box>


            </Box>

            <Box>
                <Box sx={{border: '1px solid #ddd', borderRadius: '5px', padding: '15px', marginX: 1}}>
                    <Box sx={{alignItems: 'center', display: 'flex', justifyContent: 'center',}}>
                        <CustomPieChart
                            supportedCount={petition.countSupport}
                            totalCount={petition.countNeeded}
                            status={petition.status}
                        />

                    </Box>
                    <Box mt={0.25} sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Box display="flex" alignItems="center">
                            {Utils.getStatus(
                                petition.status,
                                {mr: 0.5, fontSize: 20},
                                {fontSize: 14.5}
                            )}
                        </Box>
                        {petition.status === 'ACTIVE' ? (
                            <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                <Typography mt={0.55}>
                                    {viewDate}
                                </Typography>
                                {petition.supportedByCurrentUser ? (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                        {renderSuccessSupportButton()}
                                    </Box>
                                ) : (
                                    <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                        <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                                            Support petition
                                        </Button>
                                    </Box>
                                )}

                            </Box>
                        ) : (
                            petition.supportedByCurrentUser && (
                                <Box alignItems="center" display="flex" justifyContent="center" mt={5} mb={1.5}>
                                    {renderSuccessSupportButton()}
                                </Box>
                            ))}

                    </Box>

                </Box>
            </Box>
        </Box>
    );
};

export default PetitionPage;