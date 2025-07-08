import BaseService from "../BaseService";
import Cookies from "js-cookie";

class SchoolService extends BaseService {
    constructor() {
        super("/schools");
    }

    createSchool(schoolCreateData) {
        return this.post("/create", schoolCreateData, {
            withCredentials: true
        });
    }

    getMySchool() {
        const schoolId = Cookies.get("schoolId");
        return this.get(`/${schoolId}`);
    }

    updateSchool(schoolId, schoolUpdateData) {
        return this.put(`/update/${schoolId}`, schoolUpdateData);
    }

    deleteSchool(schoolId) {
        return this.delete(`/delete/${schoolId}`);
    }
}

export default new SchoolService();
