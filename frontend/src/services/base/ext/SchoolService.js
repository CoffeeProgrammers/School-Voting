import BaseService from "../BaseService";

class SchoolService extends BaseService {
    constructor() {
        super("/schools");
    }

    createSchool(schoolCreateData) {
        return this.post("/create", schoolCreateData, {
            withCredentials: true
        });
    }

    getSchoolById(schoolId) {
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
