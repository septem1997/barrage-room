import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from '../entity/user';
import { UserController } from '../controller/user.controller';
import { UserService } from '../service/user.service';
import { UserJwtStrategy } from '../config/user.jwt.strategy';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { jwtConstants } from '../config/constants';

@Module({
  imports: [
    TypeOrmModule.forFeature([User]),
    PassportModule,
    JwtModule.register({
      secret: jwtConstants.secretForUser,
      signOptions: { expiresIn: 0 },
    })],
  controllers: [UserController],
  providers: [UserService, UserJwtStrategy],
})
export class UserModule {
}
