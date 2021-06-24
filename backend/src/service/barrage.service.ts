import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { Admin } from '../entity/admin';
import { AdminDto } from '../dto/admin.dto';
import { Room } from '../entity/room';
import { RoomTag } from '../entity/roomTag';
import { Barrage } from '../entity/barrage';
import { BarrageDto } from '../dto/barrage.dto';

@Injectable()
export class BarrageService {

  constructor(
    @InjectRepository(Barrage)
    private repository: Repository<Barrage>
  ) {
  }

  createBarrage(barrageDto:BarrageDto){
    const barrage = new Barrage();
    const room = new Room();
    room.id = barrageDto.roomId
    barrage.room = room
    barrage.creator = barrageDto.creator.id?barrageDto.creator:null
    barrage.content = barrageDto.content
    this.repository.save(barrage)
  }

  async findBarrageByRoom(roomId): Promise<Barrage[]> {
    return await this.repository.createQueryBuilder('barrage')
      .select([
        'barrage.id',
        'barrage.content',
        'creator.nickname',
        'barrage.createTime'
      ])
      .leftJoin('barrage.creator', 'creator')
      .leftJoin('barrage.room', 'room')
      .where('room.id = :id', { id: roomId })
      .getMany();
  }

}
