import { UserProfile } from "src/core/user/dto/user-profile.enum";

export class CreatePasswordRequest {

    constructor (userId: string, password: string) {
        this.userId = userId;
        this.password = password;        
    }

    userId: string;
    password: string;

}