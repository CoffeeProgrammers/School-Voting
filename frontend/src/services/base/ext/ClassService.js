import BaseService from "../BaseService";

class ClassService extends BaseService {
    constructor() {
        super(`/schools/${1}/classes`);
    }

    createClass(data) {
        return this.post("/create", data);
    }

    updateClass(classId, data) {
        return this.put(`/update/${classId}`, data);
    }

    deleteClass(classId, deletedUsers = false) {
        return this.delete(`/delete/${classId}`, {
            params: {deletedUsers}
        });
    }

    getClassById(classId) {
        return this.get(`/${classId}`);
    }

    getMyClass() {
        return this.get("/my");
    }

    getAllClasses({name, page, size}) {
        const params = {name, page, size};
        return this.get("", {params});
    }

    assignUsers(classId, userIds) {
        return this.post(`/${classId}/assign-users`, userIds);
    }

    unassignUsers(classId, userIds) {
        return this.post(`/${classId}/unassign-users`, userIds);
    }
}

export default new ClassService();
