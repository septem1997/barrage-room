import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne } from 'typeorm';
import {BaseEntity} from "./baseEntity";
import { RoomTag } from './roomTag';
import { User } from './user';
import { Room } from './room';

@Entity()
export class Barrage extends BaseEntity{

    @Column()
    content: string;

    @ManyToOne(() => User,{
        nullable:true
    })
    creator: User;

    @ManyToOne(() => Room)
    room: Room;
}
