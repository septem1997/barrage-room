import { Entity, Column, PrimaryGeneratedColumn, OneToMany } from 'typeorm';
import {BaseEntity} from "./baseEntity";

@Entity()
export class Barrage extends BaseEntity{

    @Column()
    content: string;

}
