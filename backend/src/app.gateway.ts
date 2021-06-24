import {
  SubscribeMessage,
  WebSocketGateway,
  OnGatewayInit,
  WebSocketServer,
  OnGatewayConnection,
  OnGatewayDisconnect,
} from '@nestjs/websockets';
import { Logger } from '@nestjs/common';
import { Socket, Server } from 'socket.io';
import { UserService } from './service/user.service';
import { User } from './entity/user';
import { RoomService } from './service/room.service';
import { BarrageService } from './service/barrage.service';
import { BarrageDto } from './dto/barrage.dto';

@WebSocketGateway()
export class AppGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {

  @WebSocketServer()
  server: Server;

  roomMap:Map<number,Set<Socket>> = new Map<number, Set<Socket>>()
  userMap:Map<string,User> = new Map<string, User>()

  constructor(
    private readonly userService: UserService,
    private readonly roomService: RoomService,
    private readonly barrageService:BarrageService
  ) {}

  private logger: Logger = new Logger('AppGateway');

  @SubscribeMessage('sendMsg')
  handleMessage(client: Socket, barrageDto: BarrageDto): void {
    const user = this.userMap.get(client.id)
    // const barrageDto = new BarrageDto();
    const roomId = parseInt(<string>client.handshake.query.roomId)
    barrageDto.creator = user
    barrageDto.roomId = roomId
    barrageDto.sender = user.nickname
    // barrageDto.content = payload.content
    Array.from(this.roomMap.get(roomId)).forEach(client =>{
      client.emit('receiveMsg', barrageDto);
    })
    this.barrageService.createBarrage(barrageDto)
    this.logger.log('receiveMsg:'+JSON.stringify(barrageDto));
  }

  afterInit(server: Server) {
    this.logger.log('Init');
  }

  handleDisconnect(client: Socket) {
    const roomId = parseInt(client.handshake.query.roomId as string);
    this.roomMap.get(roomId).delete(client)
    this.userMap.delete(client.id)
    this.logger.log(`Client disconnected: ${client.id},from room:${roomId}`);
    // this.logger.log(`${roomId}房间在线用户: ${Array.from(this.roomMap.get(roomId)).map(item => item.id).toString()}`);
  }

  async handleConnection(client: Socket, ...args: any[]) {
    // todo 转redis储存用户和房间信息
    // todo 鉴定是否是该房间的成员
    if (!client.handshake.query.roomId) {
      client.disconnect(true);
      return
    }
    let user = await this.userService.findUserByToken(<string>client.handshake.query.token)
    if (!user) {
      user = new User()
      user.nickname = '匿名用户'
    }
    this.userMap.set(client.id,user);

    const roomId = parseInt(client.handshake.query.roomId as string);
    if (!this.roomMap.has(roomId)) {
      this.roomMap.set(roomId, new Set());
    }
    this.roomMap.get(roomId).add(client)
    this.logger.log(`Client connected: ${client.id},from room:${roomId}`);
    // this.logger.log(`${roomId}房间在线用户: ${Array.from(this.roomMap.get(roomId)).map(item => item.id).toString()}`);
  }
}
