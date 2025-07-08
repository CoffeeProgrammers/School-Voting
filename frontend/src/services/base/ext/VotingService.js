import BaseService from "../BaseService";
import Cookies from "js-cookie";

class VotingService extends BaseService {
    constructor() {
        const schoolId = Cookies.get("schoolId")
        super(`/schools/${schoolId}/votings`);
    }

    createVoting(data) {
        return this.post("/create", data);
    }

    updateVoting(votingId, data) {
        return this.put(`/update/${votingId}`, data);
    }

    deleteVoting(votingId) {
        return this.delete(`/delete/${votingId}`);
    }

    getVoting(votingId) {
        return this.get(`/${votingId}`);
    }

    vote(votingId, answerId) {
        return this.post(`/${votingId}/vote/${answerId}`);
    }

    getMyVoting({page, size, name = null, now = null, isNotVote = null}) {
        const params = {
            page,
            size,
            ...(name && {name}),
            ...(now !== null && {now}),
            ...(isNotVote !== null && {isNotVote}),
        };
        return this.get("/my", {params});
    }

    getMyCreatedVoting({page, size, name = null, now = null, notStarted = null}) {
        const params = {
            page,
            size,
            ...(name && {name}),
            ...(now !== null && {now}),
            ...(notStarted !== null && {notStarted}),
        };
        return this.get("/createdByMe", {params});
    }

    getVotingForDirector({page, size, name = null, now = null}) {
        const params = {
            page,
            size,
            ...(name && {name}),
            ...(now !== null && {now}),
        };
        return this.get("/forDirector", {params});
    }
}

export default new VotingService();
