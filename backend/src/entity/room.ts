import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne, ManyToMany, JoinTable } from 'typeorm';
import {BaseEntity} from "./baseEntity";
import { RoomTag } from './roomTag';
import { User } from './user';

@Entity()
export class Room extends BaseEntity{

    @Column({length:32})
    name: string;

    @Column({length:32})
    roomNo:string;

    @Column({nullable:true})
    password:string;

    @Column({nullable:true})
    roomIcon:string;

    @Column({default:false})
    isPublic: boolean;

    @ManyToOne(() => RoomTag, (roomTag) => roomTag.rooms,{
        nullable:true
    })
    tag: RoomTag;

    @ManyToOne(() => User, (user) => user.rooms,{
        nullable:true
    })
    host: User;

    @ManyToMany(() => User, user => user.joinRooms)
    @JoinTable()
    members: User[];

    tagId:number;

}
