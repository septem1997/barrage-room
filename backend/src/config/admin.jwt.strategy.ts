import { ExtractJwt, Strategy } from 'passport-jwt';
import { PassportStrategy } from '@nestjs/passport';
import { Injectable} from '@nestjs/common';
import { jwtConstants } from './constants';
import { AdminService } from '../service/admin.service';
import { AdminDto } from '../dto/admin.dto';

@Injectable()
export class AdminJwtStrategy extends PassportStrategy(Strategy,'adminJwt') {
  constructor(private readonly  adminService: AdminService) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: jwtConstants.secretForAdmin,
    });
  }

  async validate(adminDto:AdminDto) {
    return this.adminService.findOne(adminDto);
  }
}
