import BaseService from "../BaseService";
import Cookies from "js-cookie";

class ClassService extends BaseService {
    constructor() {
        const schoolId = Cookies.get("schoolId")
        super(`/schools/${schoolId}/classes`);
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
