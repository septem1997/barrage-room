import {Entity, Column} from 'typeorm';
import {BaseEntity} from "./baseEntity";

@Entity()
export class Admin extends BaseEntity{

    @Column({comment:'用户名',length:32})
    username: string;

    @Column({comment:'管理人员姓名',length:32})
    name:string;

    @Column()
    password: string;
}
