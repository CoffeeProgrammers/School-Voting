import BaseService from "../BaseService";
import Cookies from "js-cookie";

class ClassService extends BaseService {
    constructor() {
        super(`/schools`);
    }

    createClass(data) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/classes/create`, data);
    }

    updateClass(classId, data) {
        const schoolId = Cookies.get("schoolId")
        return this.put(`/${schoolId}/classes/update/${classId}`, data);
    }

    deleteClass(classId, deletedUsers = false) {
        const schoolId = Cookies.get("schoolId")
        return this.delete(`/${schoolId}/classes/delete/${classId}`, {
            params: {deletedUsers}
        });
    }

    getClassById(classId) {
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/classes/${classId}`);
    }

    getMyClass() {
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/classes/my`);
    }

    getAllClasses({name, page, size}) {
        const params = {name, page, size};
        const schoolId = Cookies.get("schoolId")
        return this.get(`/${schoolId}/classes`, {params});
    }

    assignUsers(classId, userIds) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/classes/${classId}/assign-users`, userIds);
    }

    unassignUsers(classId, userIds) {
        const schoolId = Cookies.get("schoolId")
        return this.post(`/${schoolId}/classes/${classId}/unassign-users`, userIds);
    }
}

export default new ClassService();
