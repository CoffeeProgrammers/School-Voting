import React from 'react';
import {DateTimePicker} from "@mui/x-date-pickers";
import dayjs from "dayjs";

const DefaultDateTimePicker = ({label, value, setValue}) => {
    return (
        <DateTimePicker
            label={label}
            views={['year', 'month', 'day', 'hours', 'minutes']}
            ampm={false}
            defaultValue={value ? dayjs(value) : null}
            onChange={(newValue) => setValue(newValue ? newValue.format('YYYY-MM-DDTHH:mm:ss') : null)}
        />
    );
};

export default DefaultDateTimePicker;