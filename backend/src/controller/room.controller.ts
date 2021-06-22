import { Body, Controller, Get, HttpException, HttpStatus, Post, UseGuards } from '@nestjs/common';
import { RoomService } from '../service/room.service';
import { UserDto } from '../dto/user.dto';
import { AuthGuard } from '@nestjs/passport';
import { RoomTag } from '../entity/roomTag';
import { Room } from '../entity/room';

@Controller('room')
export class RoomController {
  constructor(private readonly roomService: RoomService) {
  }

  @Get('tags')
  async tags():Promise<RoomTag[]> {
    return await this.roomService.findAllTags();
  }

  @Post('editTag')
  async editTag(
    @Body() roomTag: RoomTag
  ) {
    return await this.roomService.editTag(roomTag);
  }

  @Post('editRoom')
  async editRoom(
    @Body() room: Room
  ) {
    return await this.roomService.editRoom(room);
  }
}
