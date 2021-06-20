import { ExtractJwt, Strategy } from 'passport-jwt';
import { PassportStrategy } from '@nestjs/passport';
import { HttpException, HttpStatus, Injectable, UnauthorizedException } from '@nestjs/common';
import { UserService } from '../service/user.service';
import { UserDto } from '../dto/user.dto';
import { jwtConstants } from './constants';

@Injectable()
export class UserJwtStrategy extends PassportStrategy(Strategy,'userJwt') {
  constructor(private readonly  userService: UserService) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: jwtConstants.secretForUser,
    });
  }

  async validate(userDto:UserDto) {
    return this.userService.findOne(userDto);
  }
}
