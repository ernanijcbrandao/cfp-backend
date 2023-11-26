export class RequestChangePassword {

    constructor (userId: string,
            password: string,
            newpassword: string) {
        this.userId = userId;
        this.password = password;
        this.newpassword = newpassword;
    }

    userId: string;
    password: string;
    newpassword: string;

}