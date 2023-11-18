import { UserProfile } from "src/core/user/dto/user-profile.enum";

export class RequestCreatePassword {

    profile: UserProfile;
    userId: string;
    password: string;

}