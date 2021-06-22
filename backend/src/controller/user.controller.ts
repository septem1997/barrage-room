import { UserService } from '../service/user.service';
import { UserDto } from '../dto/user.dto';
import { Body, Controller, Get, HttpException, HttpStatus, Param, Post, Query, Req, UseGuards } from '@nestjs/common';
import { AuthGuard } from '@nestjs/passport';

@Controller('user')
export class UserController {
  constructor(private readonly userService: UserService) {
  }


  @Post('login')
  login(@Body() userDto: UserDto) {
    if (userDto.username && userDto.password) {
      return this.userService.login(userDto)
    } else {
      throw new HttpException('请输入用户名和密码', HttpStatus.BAD_REQUEST);
    }
  }


  @Get('usernameAvailable')
  async usernameAvailable(
    @Query("username") username: string
  ) {
    const userDto = new UserDto();
    userDto.username = username
    return await this.userService.findOne(userDto) == null
  }

  @Get('info')
  @UseGuards(AuthGuard('userJwt'))
  info(
    @Req() request
  ) {
    return request.user
  }

  @Post('signup')
  signUp(@Body() userDto: UserDto) {
    return this.userService.create(userDto)
  }
}
