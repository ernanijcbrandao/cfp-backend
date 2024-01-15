export default class AuthenticateRequest {

    constructor (username: string, password: string) {
        this.username = username;
        this.password = password;
    }

    username: string;
    password: string;

}  