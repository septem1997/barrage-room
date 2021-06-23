import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { Admin } from '../entity/admin';
import { AdminDto } from '../dto/admin.dto';
import { Room } from '../entity/room';
import { RoomTag } from '../entity/roomTag';

@Injectable()
export class RoomService {

  constructor(
    @InjectRepository(Room)
    private repository: Repository<Room>,
    @InjectRepository(RoomTag)
    private roomTagRepository: Repository<RoomTag>,
  ) {}




  async editTag(roomTag: RoomTag) {
    return await this.roomTagRepository.save(roomTag)
  }

  async findAllTags(): Promise<RoomTag[]> {
    return await this.roomTagRepository.createQueryBuilder('roomTag')
      .select([
        'roomTag.id',
        'roomTag.name',
        'room.id',
        'room.name',
        'room.roomIcon'
      ])
      .leftJoin('roomTag.rooms', 'room')
      .getMany();
    // return await this.roomTagRepository.find({
    //   relations:['rooms']
    // });
  }


  async editRoom(request,room: Room) {
    // todo roomDto
    let saveRoom
    if (room.id){
      saveRoom = this.repository.findOne(room.id)
    }else {
      saveRoom = new Room()
      const number = await this.repository.count();
      const roomNoPre = room.tagId?room.tagId.toString().padStart(3,'0'):'000'
      room.roomNo = roomNoPre+(number + 1).toString().padStart(6,'0')
    }
    if (request){
      room.host = request.user;
    }
    if (room.tagId){
      const tag = await this.roomTagRepository.findOne(room.tagId);
      room.tag = tag;
    }
    saveRoom = Object.assign(saveRoom,room)

    return await this.repository.save(saveRoom);
  }

  async findRoomListByUser(user) {
    return await this.repository.createQueryBuilder('room')
      .select([
        'room.id',
        'room.name',
        'room.roomNo',
        'room.password',
        'room.createTime'
      ])
      .where("room.hostId = :id", { id: user.id })
      .getMany();
  }
}
