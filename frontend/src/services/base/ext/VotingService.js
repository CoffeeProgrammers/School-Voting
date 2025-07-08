import BaseService from "../BaseService";
import Cookies from "js-cookie";

class VotingService extends BaseService {
    constructor() {
        super(`/schools`);
    }

    createVoting(data) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/votings/create`, data);
    }

    updateVoting(votingId, data) {
        const schoolId = Cookies.get("schoolId")
        return this.put(`/${schoolId}/votings/update/${votingId}`, data);
    }

    deleteVoting(votingId) {
        const schoolId = Cookies.get("schoolId")
        return this.delete(`/${schoolId}/votings/delete/${votingId}`);
    }

    getVoting(votingId) {
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/votings/${votingId}`);
    }

    vote(votingId, answerId) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/votings/${votingId}/vote/${answerId}`);
    }

    getMyVoting({page, size, name = null, now = null, isNotVote = null}) {
        const params = {
            page,
            size,
            ...(name && {name}),
            ...(now !== null && {now}),
            ...(isNotVote !== null && {isNotVote}),
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/votings/my`, {params});
    }

    getMyCreatedVoting({page, size, name = null, now = null, notStarted = null}) {
        const params = {
            page,
            size,
            ...(name && {name}),
            ...(now !== null && {now}),
            ...(notStarted !== null && {notStarted}),
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/votings/createdByMe`, {params});
    }

    getVotingForDirector({page, size, name = null, now = null}) {
        const params = {
            page,
            size,
            ...(name && {name}),
            ...(now !== null && {now}),
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/votings/forDirector`, {params});
    }
}

export default new VotingService();
