import BaseService from "../BaseService";
import Cookies from "js-cookie";

class PetitionService extends BaseService {
    constructor() {
        super(`/schools`);
    }

    createPetition(data) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/petitions/create`, data);
    }

    deletePetition(petitionId) {
        const schoolId = Cookies.get("schoolId")
        return this.delete(`/${schoolId}/petitions/delete/${petitionId}`);
    }

    getPetition(petitionId) {
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/petitions/${petitionId}`);
    }

    getMyPetitions({page, size, name = null, status = null}) {
        const params = {page, size, ...(name && {name}), ...(status && {status})};
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/petitions/my`, {params});
    }

    getMyOwnPetitions({page, size, name = null, status = null}) {
        const params = {page, size, ...(name && {name}), ...(status && {status})};
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/petitions/createdByMe`, {params});
    }

    getPetitionsForDirector({page, size, name = null, status = null}) {
        const params = {page, size, ...(name && {name}), ...(status && {status})};
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/petitions/forDirector`, {params});
    }

    supportPetition(petitionId) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/petitions/support/${petitionId}`);
    }

    approvePetition(petitionId) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/petitions/approve/${petitionId}`);
    }

    rejectPetition(petitionId) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/petitions/reject/${petitionId}`);
    }


    createComment(petitionId, commentData) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/petitions/${petitionId}/comments/create`, commentData);
    }

    updateComment(petitionId, commentId, commentData) {
        const schoolId = Cookies.get("schoolId")
        return this.put(`/${schoolId}/petitions/${petitionId}/comments/update/${commentId}`, commentData);
    }

    deleteComment(petitionId, commentId) {
        const schoolId = Cookies.get("schoolId")
        return this.delete(`/${schoolId}/petitions/${petitionId}/comments/delete/${commentId}`);
    }

    getComments(petitionId, page, size) {
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/petitions/${petitionId}/comments`, {params: {page, size}});
    }
}

export default new PetitionService();
