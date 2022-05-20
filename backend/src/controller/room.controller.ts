import {
  Body,
  Controller,
  Delete,
  Get,
  HttpException,
  HttpStatus,
  Post,
  Put,
  Query,
  Req,
  UseGuards
} from '@nestjs/common';
import { RoomService } from '../service/room.service';
import { UserDto } from '../dto/user.dto';
import { AuthGuard } from '@nestjs/passport';
import { RoomTag } from '../entity/roomTag';
import { Room } from '../entity/room';
import {PageListConvert} from "../annotations/converter";
import {RoomDto, TagDto} from "../dto/room.dto";

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


  @Get('detail')
  async detail(
      @Query("id") id: number,
  ) {
    return await this.roomService.findById(id);
  }

  @Delete('tag')
  async deleteTag( @Query("id") id: number,) {
    return await this.roomService.delTagById(id);
  }

  @Get('tags')
  async tags():Promise<RoomTag[]> {
    return await this.roomService.findAllTags();
  }

  @Get('tags/rooms')
  async tagsRooms():Promise<RoomTag[]> {
    return await this.roomService.findAllTagsAndRooms();
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

  @Post('editHall')
  async editHall(
      @Body() room: RoomDto
  ) {
    return await this.roomService.editHall(room);
  }


  @Post('joinRoom')
  @UseGuards(AuthGuard('userJwt'))
  async joinRoom(
    @Body() room: Room,@Req() request
  ) {
    return await this.roomService.joinRoom(request,room);
  }
}
