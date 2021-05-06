import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Server } from 'socket.io'
import * as fs from 'fs';

async function bootstrap() {
  const httpsOptions = {
    https:true,
    key: fs.readFileSync('./secrets/localhost+2-key.pem'),
    cert: fs.readFileSync('./secrets/localhost+2.pem'),
  };
  const app = await NestFactory.create(AppModule, { httpsOptions });
  await app.listen(3000);
}
function initSocketIO(){
  const io = new Server(3001,{
    cors:{
      origin: "*"
    }
  });

  io.on('connection', (socket) => {
    console.log("用户连接",socket.id)
    socket.emit("hello", "world");
  });


}
bootstrap();
initSocketIO()
