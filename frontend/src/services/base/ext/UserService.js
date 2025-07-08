import BaseService from "../BaseService";
import Cookies from "js-cookie";

class UserService extends BaseService {
    constructor() {
        super(`/schools`);
    }

    createUser(data) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/users/create`, data);
    }

    updateUser(userId, data) {
        const schoolId = Cookies.get("schoolId")
        return this.put(`/${schoolId}/users/update/${userId}`, data);
    }

    deleteUser(userId) {
        const schoolId = Cookies.get("schoolId")
        return this.delete(`/${schoolId}/users/delete/${userId}`);
    }

    getMyUser() {
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/users/my`);
    }

    getUsersByVoting(votingId, {email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/users/voting/${votingId}`, {params});
    }

    getUsersByRole(role, {email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/users/role/${role}`, {params});
    }

    getUsersByClass(classId, {email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/users/class/${classId}`, {params});
    }

    getUsersOfMyClass({email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/users/class/my`, {params});
    }


    getUsersWithoutClass({email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/users/withoutClass`, {params});
    }
}

export default new UserService();
