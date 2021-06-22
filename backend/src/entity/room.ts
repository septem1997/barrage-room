import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne } from 'typeorm';
import {BaseEntity} from "./baseEntity";
import { RoomTag } from './roomTag';

@Entity()
export class Room extends BaseEntity{

    @Column({length:32})
    name: string;

    @Column({length:32})
    roomNo:string;

    @Column({nullable:true})
    password:string;

    @Column()
    roomIcon:string;

    @Column()
    isPublic: boolean;

    @ManyToOne(() => RoomTag, (roomTag) => roomTag.rooms,{
        nullable:true
    })
    tag: RoomTag;

    tagId:number;

}
