export class RequestValidatePassword {

    constructor(userId: string, password: string) {
        this.userId = userId;
        this.password = password;
    }

    userId: string;
    password: string;

}