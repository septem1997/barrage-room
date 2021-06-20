import { Entity, Column, PrimaryGeneratedColumn, OneToMany } from 'typeorm';
import {BaseEntity} from "./baseEntity";

@Entity()
export class User extends BaseEntity{

    @Column({comment:'用户名',length:32})
    username: string;

    @Column({comment:'用户昵称',length:32})
    nickname: string;

    @Column()
    password: string;
}
