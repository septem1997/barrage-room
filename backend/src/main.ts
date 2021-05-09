import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Server } from 'socket.io'
import * as fs from 'fs';
import * as https from 'https';
import * as express from 'express';


async function bootstrap() {
  const httpsOptions = {
    https:true,
    key: fs.readFileSync('./secrets/localhost+3-key.pem'),
    cert: fs.readFileSync('./secrets/localhost+3.pem'),
  };
  const app = await NestFactory.create(AppModule, { httpsOptions });
  await app.listen(3000);
}
function initSocketIO(){
  const app = express();
  const server = https.createServer({
    key: fs.readFileSync('./secrets/localhost+3-key.pem'),
    cert: fs.readFileSync('./secrets/localhost+3.pem')
  },app);
  server.listen(3001);

  const io = new Server(server,{
    cors:{
      origin:'*'
    }
  });

  io.on('connection',function (socket) {
    console.log(socket.id)
  });



}
bootstrap();
initSocketIO()
