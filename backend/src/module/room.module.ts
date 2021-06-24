import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Room } from '../entity/room';
import { RoomService } from '../service/room.service';
import { RoomTag } from '../entity/roomTag';
import { RoomController } from '../controller/room.controller';
const reps = [TypeOrmModule.forFeature([Room]),TypeOrmModule.forFeature([RoomTag])];
@Module({
  imports: reps,
  controllers:[RoomController],
  providers:[RoomService],
  exports:[
    ...reps,RoomService
  ]
})
export class RoomModule {
}
