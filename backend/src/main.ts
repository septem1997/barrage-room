import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Server } from 'socket.io'
import * as fs from 'fs';
import * as https from 'https';
import * as express from 'express';


async function bootstrap() {
  const httpsOptions = {
    https:true,
    key: fs.readFileSync('./secrets/localhost+2-key.pem'),
    cert: fs.readFileSync('./secrets/localhost+2.pem'),
  };
  const app = await NestFactory.create(AppModule, { httpsOptions });

  const server = app.getHttpServer()
  const io = new Server(server,{
    cors:{
      origin:'*'
    }
  });

  io.on('connection',function (socket) {
    console.log(socket.id)
  });
  await app.listen(4000);
}
bootstrap();
