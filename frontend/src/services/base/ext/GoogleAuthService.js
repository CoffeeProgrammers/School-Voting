import BaseService from "../BaseService";

class GoogleAuthService extends BaseService {
    constructor() {
        super(`/auth/google`);
    }

    isConnected() {
        return this.get("/isConnected")
    }

    connect() {
        return this.get("/auth");
    }

    revoke() {
        return this.get("/revoke")
    }
}

export default new GoogleAuthService();
