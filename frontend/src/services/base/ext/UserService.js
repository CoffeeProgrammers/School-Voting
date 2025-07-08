import BaseService from "../BaseService";
import Cookies from "js-cookie";

class UserService extends BaseService {
    constructor() {
        const schoolId = Cookies.get("schoolId")
        super(`/schools/${schoolId}/users`);
    }

    createUser(data) {
        return this.post("/create", data);
    }

    updateUser(userId, data) {
        return this.put(`/update/${userId}`, data);
    }

    deleteUser(userId) {
        return this.delete(`/delete/${userId}`);
    }

    getMyUser() {
        return this.get("/my");
    }

    getUsersByVoting(votingId, {email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        return this.get(`/voting/${votingId}`, {params});
    }

    getUsersByRole(role, {email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        return this.get(`/role/${role}`, {params});
    }

    getUsersByClass(classId, {email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        return this.get(`/class/${classId}`, {params});
    }

    getUsersOfMyClass({email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        return this.get(`/class/my`, {params});
    }


    getUsersWithoutClass({email, firstName, lastName, page, size}) {
        const params = {
            ...(email && {email}),
            ...(firstName && {firstName}),
            ...(lastName && {lastName}),
            page,
            size
        };
        return this.get(`/withoutClass`, {params});
    }
}

export default new UserService();
