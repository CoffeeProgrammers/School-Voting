
class DateUtils {

    static getDaysLeft(date) {
        const now = new Date();
        const target = new Date(date);

        const diffMs = target - now;

        const msPerDay = 1000 * 60 * 60 * 24;
        const diffDays = Math.ceil(diffMs / msPerDay);

        return diffDays;
    }

}

export default DateUtils;