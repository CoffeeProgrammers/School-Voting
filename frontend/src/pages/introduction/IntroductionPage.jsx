import React from 'react';
import Box from '@mui/material/Box';
import {Button, Container, Stack} from "@mui/material";
import Typography from "@mui/material/Typography";
import PsychologyAltIcon from '@mui/icons-material/PsychologyAlt';
import yourVoiceMattersImage from "../../images/Frame 4.png"
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import HistoryEduIcon from '@mui/icons-material/HistoryEdu';
import PetitionPageImage from '../../images/image 27.png'
import PetitionStatisticImage from '../../images/image 28.png'
import DoneIcon from '@mui/icons-material/Done';
import BalanceIcon from '@mui/icons-material/Balance';
import HomeWorkIcon from '@mui/icons-material/HomeWork';
import VotingPageImage from '../../images/voting1.png';
import VotingStatistickImage from '../../images/voting2.png'
import AuthService from "../../services/auth/AuthService";
import {useNavigate} from "react-router-dom";
import SchoolPageImage from '../../images/school1.jpg'
import SchoolInformationImage from '../../images/school2.jpg'
import "./introduction.css"

function CustomTabPanel(props) {
    const {children, value, index, ...other} = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`simple-tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
            {...other}
        >
            {value === index && <Box sx={{p: 0, width: '100%'}}>{children}</Box>}
        </div>
    );
}

function a11yProps(index) {
    return {
        id: `simple-tab-${index}`,
        'aria-controls': `simple-tabpanel-${index}`,
    };
}

const IntroductionPage = () => {
    const [value, setValue] = React.useState(0);
    const navigate = useNavigate()

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <Box>
            <Container sx={{padding: 0}}>
                <Box sx={{display: 'flex', alignItems: 'center', justifyContent: "space-between", padding: "20px 0"}}>
                    <Stack sx={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}>
                        <PsychologyAltIcon color='primary' sx={{mt: '3px', mr: '6px', fontSize: '35px'}}/>
                        <Typography variant="h6" fontWeight='bold' noWrap>
                            School Governance
                        </Typography>
                    </Stack>
                    <Button onClick={() => {
                        AuthService.redirectToKeycloak()
                    }} variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}>
                        Login
                    </Button>
                </Box>
                <Box sx={{display: "grid", gridTemplateColumns: "1fr 1fr", alignItems: "center", marginTop: '20px'}}
                     className='mainBlock'>
                    <Box className='mainBlockText'>
                        <Typography variant='h3' className="titleText" sx={{fontWeight: 500, marginBottom: "20px"}}>
                            Your Voice Matters: School governance
                        </Typography>
                        <Typography>
                            Welcome to our new interactive platform where every student, teacher, and staff member of
                            our
                            school can express their opinion, propose an idea, and influence change. This is your space
                            for
                            active participation in shaping a better school environment.
                        </Typography>
                        <Button variant="contained" color="primary" onClick={() => {navigate('/create-school')}}
                                sx={{height: 32, borderRadius: 10, width: 250, marginTop: "20px"}}>
                            Register a school
                        </Button>
                    </Box>
                    <img src={yourVoiceMattersImage} alt="" width="646px"/>
                </Box>
                <Box sx={{marginTop: '30px', width: '100%'}}>
                    <Box sx={{display: 'flex', justifyContent: 'center'}} className='tabsContainer'>
                        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example"
                              TabIndicatorProps={{style: {display: 'none'}}} className='tabBlock'>
                            <Tab className='tabs' sx={{
                                width: '345px', height: '100px',
                                '&.Mui-selected': {
                                    textDecoration: 'underline',
                                    textDecorationColor: 'primary.main',
                                    textUnderlineOffset: '4px',
                                    textDecorationThickness: '3px',
                                }
                            }} label={
                                <Box sx={{display: 'flex', alignItems: 'center', gap: '15px'}}>
                                    <Box sx={{
                                        width: "50px",
                                        height: "50px",
                                        bgcolor: '#546E7A',
                                        borderRadius: 100,
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center'
                                    }}>
                                        <HistoryEduIcon sx={{color: "#CFD8DC", fontSize: '30px'}}/>
                                    </Box>
                                    <Typography
                                        sx={{
                                            fontSize: '16px',
                                            fontWeight: "bold",
                                            color: '#000'
                                        }}>Petitions</Typography>
                                </Box>

                            } {...a11yProps(0)} />
                            <Tab className='tabs' sx={{
                                width: '345px', height: '100px',
                                '&.Mui-selected': {
                                    textDecoration: 'underline',
                                    textDecorationColor: 'primary.main',
                                    textUnderlineOffset: '4px',
                                    textDecorationThickness: '3px',
                                }
                            }} label={
                                <Box sx={{display: 'flex', alignItems: 'center', gap: '15px'}}>
                                    <Box sx={{
                                        width: "50px",
                                        height: "50px",
                                        bgcolor: '#546E7A',
                                        borderRadius: 100,
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center'
                                    }}>
                                        <BalanceIcon sx={{color: "#CFD8DC", fontSize: '30px'}}/>
                                    </Box>
                                    <Typography
                                        sx={{fontSize: '16px', fontWeight: "bold", color: '#000'}}>Voting</Typography>
                                </Box>

                            } {...a11yProps(1)} />
                            <Tab className='tabs' sx={{
                                width: '345px', height: '100px',
                                '&.Mui-selected': {
                                    textDecoration: 'underline',
                                    textDecorationColor: 'primary.main',
                                    textUnderlineOffset: '4px',
                                    textDecorationThickness: '3px',
                                }
                            }} label={
                                <Box sx={{display: 'flex', alignItems: 'center', gap: '15px'}}>
                                    <Box sx={{
                                        width: "50px",
                                        height: "50px",
                                        bgcolor: '#546E7A',
                                        borderRadius: 100,
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center'
                                    }}>
                                        <HomeWorkIcon sx={{color: "#CFD8DC", fontSize: '28px'}}/>
                                    </Box>
                                    <Typography
                                        sx={{fontSize: '16px', fontWeight: "bold", color: '#000'}}>School</Typography>
                                </Box>

                            } {...a11yProps(2)} />
                        </Tabs>
                    </Box>
                </Box>
            </Container>
            <Box>
                <CustomTabPanel value={value} index={0}>
                    <Box sx={{display: 'grid', gridTemplateColumns: "2fr 1fr", padding: "30px", bgcolor: "#F8F8F8"}}
                         className='petitionBlock'>
                        <Box className='imageContainer'>
                            <Box sx={{position: "relative"}} className='imageBox'>
                                <img src={PetitionPageImage} alt="" width="700px"/>
                                <img src={PetitionStatisticImage}
                                     style={{position: "absolute", left: '490px', top: "134px", width: 270}} alt=""/>
                            </Box>
                        </Box>
                        <Box>
                            <Typography sx={{fontSize: "30px", fontWeight: 'bold'}}>Your Voice, Our
                                Changes!</Typography>
                            <Typography>This section is the hub for initiatives and proposals from the entire school
                                community. Here, your ideas transform into real change, and your signature becomes a
                                driving force for improving our school. <br/>
                                <br/>
                                How it works:</Typography>
                            <Box sx={{marginTop: '20px', display: "flex", alignItems: "center", gap: "10px"}}>
                                <Box sx={{
                                    display: "block",
                                    bgcolor: '#388E3C',
                                    width: '52px',
                                    height: "24px",
                                    borderRadius: 100
                                }} className='circle'>
                                    <Box sx={{
                                        height: "24px",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center"
                                    }}>
                                        <DoneIcon sx={{color: "white", fontSize: '20px'}}></DoneIcon>
                                    </Box>
                                </Box>
                                <Typography>Browse and Sign: Explore active petitions and support the ones you care
                                    about. Every signature counts!</Typography>
                            </Box>
                            <Box sx={{marginTop: '10px', display: "flex", alignItems: "center", gap: "10px"}}>
                                <Box sx={{
                                    display: "block",
                                    bgcolor: '#388E3C',
                                    width: '49px',
                                    height: "24px",
                                    borderRadius: 100
                                }} className='circle'>
                                    <Box sx={{
                                        height: "24px",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center"
                                    }}>
                                        <DoneIcon sx={{color: "white", fontSize: '20px'}}></DoneIcon>
                                    </Box>
                                </Box>
                                <Typography>Create: Have your own idea or see a problem? Formulate your proposal and
                                    launch your petition.</Typography>
                            </Box>
                            <Box sx={{marginTop: '10px', display: "flex", alignItems: "center", gap: "10px"}}>
                                <Box sx={{
                                    display: "block",
                                    bgcolor: '#388E3C',
                                    width: '65px',
                                    height: "24px",
                                    borderRadius: 100
                                }} className='circle'>
                                    <Box sx={{
                                        height: "24px",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center"
                                    }}>
                                        <DoneIcon sx={{color: "white", fontSize: '20px'}}></DoneIcon>
                                    </Box>
                                </Box>
                                <Typography>Track Progress: Next to each petition, you'll see the number of signatures
                                    gathered, showing the level of support for the initiative.</Typography>
                            </Box>
                        </Box>
                    </Box>
                </CustomTabPanel>
                <CustomTabPanel value={value} index={1}>
                    <Box className='votingBlock'
                         sx={{display: 'grid', gridTemplateColumns: "1fr 2fr", padding: "30px", bgcolor: "#F8F8F8"}}>
                        <Box>
                            <Typography sx={{fontSize: "30px", fontWeight: 'bold'}}>Voting Page: Your Choices, Our
                                Future!</Typography>
                            <Typography>This section is where your decisions directly shape our school's path. Here, you
                                can participate in key votes on important school matters, ensuring your preferences and
                                priorities are reflected in the decisions that affect our <community
                                    class=""></community> <br/>
                                <br/>
                                How Voting Works Here:</Typography>
                            <Box sx={{marginTop: '20px', display: "flex", alignItems: "center", gap: "10px"}}>
                                <Box sx={{
                                    display: "block",
                                    bgcolor: '#388E3C',
                                    width: '65px',
                                    height: "24px",
                                    borderRadius: 100
                                }} className='circle'>
                                    <Box sx={{
                                        height: "24px",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center"
                                    }}>
                                        <DoneIcon sx={{color: "white", fontSize: '20px'}}></DoneIcon>
                                    </Box>
                                </Box>
                                <Typography>View Active Polls: See current voting initiatives on topics like school
                                    events, extracurricular activities, facility improvements, and more.</Typography>
                            </Box>
                            <Box sx={{marginTop: '10px', display: "flex", alignItems: "center", gap: "10px"}}>
                                <Box sx={{
                                    display: "block",
                                    bgcolor: '#388E3C',
                                    width: '53px',
                                    height: "24px",
                                    borderRadius: 100
                                }} className='circle'>
                                    <Box sx={{
                                        height: "24px",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center"
                                    }}>
                                        <DoneIcon sx={{color: "white", fontSize: '20px'}}></DoneIcon>
                                    </Box>
                                </Box>
                                <Typography>Cast Your Vote: Simply select your preferred option. Your vote is crucial
                                    for reaching a collective decision.</Typography>
                            </Box>
                            <Box sx={{marginTop: '10px', display: "flex", alignItems: "center", gap: "10px"}}>
                                <Box sx={{
                                    display: "block",
                                    bgcolor: '#388E3C',
                                    width: '55px',
                                    height: "24px",
                                    borderRadius: 100
                                }} className='circle'>
                                    <Box sx={{
                                        height: "24px",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center"
                                    }}>
                                        <DoneIcon sx={{color: "white", fontSize: '20px'}}></DoneIcon>
                                    </Box>
                                </Box>
                                <Typography>Track Results: After polls close, you'll see the outcomes, demonstrating how
                                    the community's voice has been heard.</Typography>
                            </Box>
                        </Box>
                        <Box className='imageContainer votingImage'>
                            <Box sx={{position: "relative", display: 'flex', justifyContent: 'flex-end'}}
                                 className='imageBox'>
                                <img src={VotingPageImage} alt="" width="700px"/>
                                <img src={VotingStatistickImage} width='276px'
                                     style={{position: "absolute", right: '490px', top: "200px"}} alt=""/>
                            </Box>
                        </Box>
                    </Box>
                </CustomTabPanel>
                <CustomTabPanel value={value} index={2}>
                    <Box sx={{
                        display: 'grid',
                        gridTemplateColumns: "2fr 1fr",
                        padding: "30px",
                        bgcolor: "#F8F8F8",
                        alignItems: "center"
                    }} className='schoolBlock'>
                        <Box className="imageContainer">
                            <Box sx={{position: "relative"}} className='imageBox'>
                                <img src={SchoolPageImage} alt="" width="700px"/>
                                <img src={SchoolInformationImage}
                                     style={{position: "absolute", left: '490px', top: "134px", width: 250}} alt=""/>
                            </Box>
                        </Box>
                        <Box>
                            <Typography sx={{fontSize: "30px", fontWeight: 'bold'}}>Our School Page: Get to Know Us
                                Better!</Typography>
                            <Typography>Welcome to our Our School Page! This is your central hub to quickly find key
                                information about our school's structure and leadership. <br/>
                                <br/>
                                Here, you'll find a comprehensive list of our classes, detailing the various educational
                                stages and streams available. You'll also be introduced to our school principal,
                                understanding the leadership that guides our institution.<br/>
                                <br/>
                                This page is designed for quick access to these fundamental aspects of our school
                                community.</Typography>
                        </Box>
                    </Box>
                </CustomTabPanel>
            </Box>
            <Box sx={{
                padding: '40px 40px 20px 40px',
                display: 'grid',
                gritTemplateRows: "1fr 1fr",
                gap: '10px',
                bgcolor: '#F8F8F8'
            }}>
                <Box sx={{display: "flex", justifyContent: 'space-between', alignItems: "center"}}>
                    <Stack sx={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}>
                        <PsychologyAltIcon color='primary' sx={{mt: '3px', mr: '6px', fontSize: '35px'}}/>
                        <Typography variant="h6" fontWeight='bold' noWrap>
                            School Governance
                        </Typography>
                    </Stack>
                    <Button variant="contained" color="primary" sx={{height: 32, borderRadius: 10}}
                    onClick={() => {AuthService.redirectToKeycloak()}}>
                        Login
                    </Button>
                </Box>
                <Box sx={{display: "flex", justifyContent: "center"}}>
                    <Typography sx={{color: '#AFB5C0'}}>©2025 Coffee Programmers</Typography>
                </Box>
            </Box>
        </Box>
    )
        ;
};

export default IntroductionPage;