import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Room } from '../entity/room';
import { AdminController } from '../controller/admin.controller';
import { AdminService } from '../service/admin.service';
import { AdminJwtStrategy } from '../config/admin.jwt.strategy';
import { RoomService } from '../service/room.service';

@Module({
  imports: [
    TypeOrmModule.forFeature([Room]),
  ],
  controllers:[Room],
  providers:[RoomService]
})
export class RoomModule {
}
