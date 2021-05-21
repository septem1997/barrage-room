import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Server } from 'socket.io'
import * as fs from 'fs';
import * as https from 'https';
import * as express from 'express';


async function bootstrap() {
  // const httpsOptions = {
  //   https:true,
  //   key: fs.readFileSync('./secrets/localhost+3-key.pem'),
  //   cert: fs.readFileSync('./secrets/localhost+3.pem'),
  // };
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
  });
  await app.listen(4000);
}
bootstrap();
