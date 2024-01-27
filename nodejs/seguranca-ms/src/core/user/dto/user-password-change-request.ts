import { UserProfile } from "src/core/user/dto/user-profile.enum";

export class UserPasswordChangeRequest {

    constructor (password: string, newpassword: string) {
        this.password = password;
        this.newpassword = newpassword;
    }

    password: string;
    newpassword: string;

}