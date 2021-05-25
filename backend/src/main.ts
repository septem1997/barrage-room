import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Server } from 'socket.io'
import * as fs from 'fs';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  const server = app.getHttpServer()
  const io = new Server(server,{
    allowEIO3:true,
    cors:{
      origin:'*'
    }
  });

  io.on('connection',function (socket) {
    console.log(socket.id)

    socket.on("sendMsg",(data)=>{
      io.emit("receiveMsg",data)
    })
    io.emit("connectSuccess",null)
  });
  await app.listen(4000);
}
bootstrap();
