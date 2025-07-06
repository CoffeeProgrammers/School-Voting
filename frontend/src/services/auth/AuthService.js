import axios from "axios";
import Cookies from 'js-cookie';

class AuthService {

    static redirectToKeycloak() {
        const keycloakUrl = "http://localhost:8080/realms/coffee-programmers/protocol/openid-connect/auth";
        const clientId = "coffee-programmers-client";
        const redirectUri = "http://localhost:3000/callback";
        const loginUrl = `${keycloakUrl}?client_id=${clientId}` +
            `&redirect_uri=${encodeURIComponent(redirectUri)}` +
            `&response_type=code&scope=openid`;
        window.location.href = loginUrl;
    };

    static async logout() {
        const userId = Cookies.get('userId');

        try {
            await axios.post('http://localhost:8081/api/auth/logout', {}, {
                params: {
                    userId: userId,
                },
                withCredentials: true,
            }).then(() => {
                AuthService.redirectToKeycloak();
            }).catch(error => {
                console.error('Logout failed:', error);
            });
        } catch (e) {
            console.error("Logout failed", e);
        }
    }

    static async refresh() {
        console.log("Refreshing token");

        const refreshToken = Cookies.get("refreshToken");
        if (!refreshToken) {
            console.log("No refresh token, redirecting to Keycloak");
            AuthService.redirectToKeycloak();
            return false;
        }

        try {
            await axios.post("http://localhost:8081/api/auth/refresh", {}, {
                params: {
                    refreshToken: encodeURIComponent(refreshToken),
                },
                withCredentials: true,
            });
            return true;
        } catch (error) {
            console.error("Token refresh failed", error);
            AuthService.redirectToKeycloak();
            return false;
        }
    }
}

export default AuthService;
