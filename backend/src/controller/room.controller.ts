import {Body, Controller, Get, HttpException, HttpStatus, Post, Query, Req, UseGuards} from '@nestjs/common';
import { RoomService } from '../service/room.service';
import { UserDto } from '../dto/user.dto';
import { AuthGuard } from '@nestjs/passport';
import { RoomTag } from '../entity/roomTag';
import { Room } from '../entity/room';
import {PageListConvert} from "../annotations/converter";

@Controller('room')
export class RoomController {
  constructor(private readonly roomService: RoomService) {
  }

  @Get('list')
  @PageListConvert
  async list(
      @Query("pageSize") pageSize: number,
      @Query("page") page: number
  ) {
    return await this.roomService.getRoomList(page,pageSize)
  }

  @Get('hall/list')
  @PageListConvert
  async hallList(
      @Query("pageSize") pageSize: number,
      @Query("page") page: number
  ) {
    return await this.roomService.getHallList(page,pageSize)
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

  @Get('myJoinRoom')
  @UseGuards(AuthGuard('userJwt'))
  async myJoinRoom(
    @Req() request
  ):Promise<Room[]> {
    return await this.roomService.findJoinRoomListByUser(request.user);
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


  @Post('joinRoom')
  @UseGuards(AuthGuard('userJwt'))
  async joinRoom(
    @Body() room: Room,@Req() request
  ) {
    return await this.roomService.joinRoom(request,room);
  }
}
