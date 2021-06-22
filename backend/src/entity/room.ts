import { Entity, Column, PrimaryGeneratedColumn, OneToMany } from 'typeorm';
import {BaseEntity} from "./baseEntity";

@Entity()
export class Room extends BaseEntity{

    @Column({length:32})
    name: string;

    @Column({length:32})
    roomNo:string;

    @Column()
    password:string;

    @Column()
    isPublic: boolean;


}
