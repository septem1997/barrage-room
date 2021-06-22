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


  async editRoom(room: Room) {
    const tag = await this.roomTagRepository.findOne(room.tagId);
    const number = await this.repository.count();
    room.tag = tag;
    const roomNoPre = room.isPublic?tag.id.toString().padStart(3,'0'):'000'
    room.roomNo = roomNoPre+(number + 1).toString().padStart(6,'0')
    return await this.repository.save(room);
  }
}
