import PeopleRoundedIcon from "@mui/icons-material/PeopleRounded";
import Typography from "@mui/material/Typography";
import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import CheckCircleOutlineIcon from "@mui/icons-material/CheckCircleOutline";
import theme from "../assets/theme";

class Utils {

    static getDaysLeft(date) {
        const now = new Date();
        const target = new Date(date);

        const diffMs = target - now;

        const msPerDay = 1000 * 60 * 60 * 24;
        const diffDays = Math.ceil(diffMs / msPerDay);

        return diffDays;
    }

    static getStatus(status, iconStyles, textStyles) {
        switch (status) {
            case 'ACTIVE':
                return (
                    <>
                        <PeopleRoundedIcon color="secondary" sx={iconStyles}/>
                        <Typography sx={textStyles}>
                            Active
                        </Typography>
                    </>
                );
            case 'UNSUCCESSFUL':
                return (
                    <>
                        <CancelOutlinedIcon color="error" sx={iconStyles}/>
                        <Typography sx={textStyles}>
                            Unsuccessful
                        </Typography>
                    </>
                );
            case 'WAITING_FOR_CONSIDERATION':
                return (
                    <>
                        <AccessTimeIcon color="secondary" sx={iconStyles}/>
                        <Typography sx={textStyles}>
                            Waiting for consideration
                        </Typography>
                    </>
                );
            case 'APPROVED':
                return (
                    <>
                        <CheckCircleOutlineIcon color="success" sx={iconStyles}/>
                        <Typography sx={textStyles}>
                            Approved
                        </Typography>
                    </>
                );
            case 'REJECTED':
                return (
                    <>
                        <CancelOutlinedIcon color="error" sx={iconStyles}/>
                        <Typography sx={textStyles}>
                            Rejected
                        </Typography>
                    </>
                );
            default:
                return null;
        }
    };

    static getStatusColor(status) {
        switch (status) {
            case 'ACTIVE':
            case 'WAITING_FOR_CONSIDERATION':
                return theme.palette.secondary.main;
            case 'APPROVED':
                return theme.palette.success.main;
            case 'UNSUCCESSFUL':
            case 'REJECTED':
                return theme.palette.error.main;
            default:
                return theme.palette.grey[500];
        }
    };

    static getStatusMUIColor(status) {
        switch (status) {
            case 'ACTIVE':
            case 'WAITING_FOR_CONSIDERATION':
                return 'secondary'
            case 'APPROVED':
                return 'success';
            case 'UNSUCCESSFUL':
            case 'REJECTED':
                return 'error';
            default:
                return 'primary';
        }
    };
}

export default Utils;