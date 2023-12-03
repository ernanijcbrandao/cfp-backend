import { BlockReason } from "./block-reason.enum";

export class CreateTemporaryBlockRequest {

    constructor (userId: string,
        reason: BlockReason,
        expire: Date) {
        this.userId = userId;
        this.reason = reason;
        this.expire = expire;
    }

    userId: string;
    reason: BlockReason;
    expire: Date;
    description: string;

}  