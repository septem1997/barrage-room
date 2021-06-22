import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from './app.controller';
import { AppService } from '../service/app.service';
import { RoomController } from './room.controller';
import { RoomService } from '../service/room.service';
import { RoomTag } from '../entity/roomTag';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Room } from '../entity/room';
import { Admin } from '../entity/admin';
import { User } from '../entity/user';
import { RoomModule } from '../module/room.module';

describe('AppController', () => {
  let appController: AppController;
  let roomController: RoomController;

  beforeEach(async () => {
    const app: TestingModule = await Test.createTestingModule({
      imports:[
        TypeOrmModule.forRoot({
          type: 'mysql',
          host: 'localhost',
          port: 3306,
          username: 'root',
          password: 'root',
          database: 'barrage_room',
          entities: [Admin,User,RoomTag,Room],
          synchronize: true,
        }),
        RoomModule
      ],
      controllers: [AppController],
      providers: [AppService],
    }).compile();
    roomController = app.get<RoomController>(RoomController);
    appController = app.get<AppController>(AppController);
  });

  describe('root', () => {
    it('should return "Hello World!"', () => {
      expect(appController.getHello()).toBe('Hello World!');
    });

    it('测试创建房间标签',async () => {
      const roomTag = new RoomTag();
      roomTag.name = "体育赛事";
      const roomTag1 = await roomController.editTag(roomTag);
      console.log(roomTag1);
      expect(roomTag1!=null).toBeTruthy();
    })

    it('创建房间',async () => {
      const room = new Room();
      room.tagId = 2
      room.isPublic = true
      room.name = "欧洲杯"
      room.roomIcon = "https://barrage.oss-cn-guangzhou.aliyuncs.com/football.jpeg"
      const roomTag1 = await roomController.editRoom(room);
      console.log(roomTag1);
      expect(roomTag1!=null).toBeTruthy();
    })
  });
});
