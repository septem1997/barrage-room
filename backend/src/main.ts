import { NestFactory } from '@nestjs/core';
import { AppModule } from './module/app.module';
import { Server } from 'socket.io'
import { AllExceptionsFilter } from './config/any-exception.filter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.useGlobalFilters(new AllExceptionsFilter())
  app.enableCors()

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
