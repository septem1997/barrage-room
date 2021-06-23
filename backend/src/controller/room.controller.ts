import { Body, Controller, Get, HttpException, HttpStatus, Post, Req, UseGuards } from '@nestjs/common';
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

  @Get('myRoom')
  @UseGuards(AuthGuard('userJwt'))
  async myRoom(
    @Req() request
  ):Promise<Room[]> {
    return await this.roomService.findRoomListByUser(request.user);
  }

  @Post('editTag')
  async editTag(
    @Body() roomTag: RoomTag
  ) {
    return await this.roomService.editTag(roomTag);
  }

  @Post('editRoom')
  @UseGuards(AuthGuard('userJwt'))
  async editRoom(
    @Body() room: Room,@Req() request
  ) {
    return await this.roomService.editRoom(request,room);
  }
}
