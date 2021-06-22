import { Entity, Column, PrimaryGeneratedColumn, OneToMany } from 'typeorm';
import { BaseEntity } from './baseEntity';
import { Room } from './room';

@Entity()
export class RoomTag extends BaseEntity {

  @Column({ length: 32, unique: true })
  name: string;

  @OneToMany(() => Room, (room) => room.tag)
  rooms: Room[];
}
