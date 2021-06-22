import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Room } from '../entity/room';
import { RoomService } from '../service/room.service';
import { RoomTag } from '../entity/roomTag';
import { RoomController } from '../controller/room.controller';

@Module({
  imports: [
    TypeOrmModule.forFeature([Room]),
    TypeOrmModule.forFeature([RoomTag])
  ],
  controllers:[RoomController],
  providers:[RoomService]
})
export class RoomModule {
}
