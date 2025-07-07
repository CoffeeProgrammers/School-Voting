import BaseService from "../BaseService";

class UserService extends BaseService {
    constructor() {
        super(`/schools/${1}/users`);
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
