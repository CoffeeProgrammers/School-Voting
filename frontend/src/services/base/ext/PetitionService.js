import BaseService from "../BaseService";
import Cookies from "js-cookie";

class PetitionService extends BaseService {
    constructor() {
        const schoolId = Cookies.get("schoolId")
        super(`/schools/${schoolId}/petitions`);
    }

    createPetition(data) {
        return this.post("/create", data);
    }

    deletePetition(petitionId) {
        return this.delete(`/delete/${petitionId}`);
    }

    getPetition(petitionId) {
        return this.get(`/${petitionId}`);
    }

    getMyPetitions({page, size, name = null, status = null}) {
        const params = {page, size, ...(name && {name}), ...(status && {status})};
        return this.get("/my", {params});
    }

    getMyOwnPetitions({page, size, name = null, status = null}) {
        const params = {page, size, ...(name && {name}), ...(status && {status})};
        return this.get("/createdByMe", {params});
    }

    getPetitionsForDirector({page, size, name = null, status = null}) {
        const params = {page, size, ...(name && {name}), ...(status && {status})};
        return this.get("/forDirector", {params});
    }

    supportPetition(petitionId) {
        return this.post(`/support/${petitionId}`);
    }

    approvePetition(petitionId) {
        return this.post(`/approve/${petitionId}`);
    }

    rejectPetition(petitionId) {
        return this.post(`/reject/${petitionId}`);
    }


    createComment(petitionId, commentData) {
        return this.post(`/${petitionId}/comments/create`, commentData);
    }

    updateComment(petitionId, commentId, commentData) {
        return this.put(`/${petitionId}/comments/update/${commentId}`, commentData);
    }

    deleteComment(petitionId, commentId) {
        return this.delete(`/${petitionId}/comments/delete/${commentId}`);
    }

    getComments(petitionId, page, size) {
        return this.get(`/${petitionId}/comments`, {params: {page, size}});
    }
}

export default new PetitionService();
