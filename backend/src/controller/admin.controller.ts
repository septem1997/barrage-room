import { Body, Controller, Get, HttpException, HttpStatus, Post, Req, UseGuards } from '@nestjs/common';
import { AuthGuard } from '@nestjs/passport';
import { AdminService } from '../service/admin.service';
import { AdminDto } from '../dto/admin.dto';

@Controller('admin')
export class AdminController {
  constructor(private readonly adminService: AdminService) {
  }


  @Post('login')
  login(@Body() adminDto: AdminDto) {
    if (adminDto.username && adminDto.password) {
      return this.adminService.login(adminDto)
    } else {
      throw new HttpException('请输入用户名和密码', HttpStatus.BAD_REQUEST);
    }
  }

  @Get('info')
  @UseGuards(AuthGuard('adminJwt'))
  info(
    @Req() request
  ) {
    return request.user
  }

  @Post('signUp')
  signUp(@Body() adminDto: AdminDto) {
    return this.adminService.create(adminDto)
  }
}
