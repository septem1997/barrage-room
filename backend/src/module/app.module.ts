import { Module } from '@nestjs/common';
import { AppController } from '../controller/app.controller';
import { AppService } from '../service/app.service';
import { UserModule } from './user.module';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule } from '@nestjs/config';
import { AdminModule } from './admin.module';
import { Admin } from '../entity/admin';
import { User } from '../entity/user';
import { AppGateway } from '../app.gateway';
import { RoomModule } from './room.module';
import { RoomTag } from '../entity/roomTag';
import { Room } from '../entity/room';

@Module({
  imports: [
    ConfigModule.forRoot({
      envFilePath: ['.env.local', '.env'],
    }),
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
    UserModule,
    AdminModule,
    RoomModule
  ],
  controllers: [AppController],
  providers: [AppService,AppGateway],
})
export class AppModule {}
