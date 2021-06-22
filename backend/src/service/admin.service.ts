import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { Admin } from '../entity/admin';
import { AdminDto } from '../dto/admin.dto';

@Injectable()
export class AdminService {

  constructor(
    @InjectRepository(Admin)
    private repository: Repository<Admin>,
    private readonly jwtService: JwtService,
  ) {}

  register(pass: string):Promise<string> {
    const saltRounds = 10;
    return bcrypt.hash(pass, saltRounds);
  }


  async login(adminDto:AdminDto) {
    const findAdmin = await this.findOne(adminDto)
    if (findAdmin){
      const pwdIsCorrect = await bcrypt.compare(adminDto.password, findAdmin.password)
      if (pwdIsCorrect){
        return {
          token:'Bearer '+this.jwtService.sign(adminDto),
          username:findAdmin.username,
          name:findAdmin.name
        }
      }else {
        throw new HttpException('用户名或密码错误', HttpStatus.BAD_REQUEST);
      }
    }else {
      throw new HttpException('找不到该用户', HttpStatus.NOT_FOUND);
    }
  }

  async create(adminDto: AdminDto) {
    const query  = new Admin()
    query.username = adminDto.username
    const findAdmin = await this.repository.findOne(query)
    if (findAdmin){
      throw new HttpException('该用户名已存在', HttpStatus.CONFLICT);
    }
    const user = new Admin()
    const newPass = await this.register(adminDto.password)
    user.username = adminDto.username
    user.password = newPass
    // user.createTime = new Date().toISOString()
    user.name = adminDto.name
    await this.repository.save(user)
    return {
      msg:'注册成功'
    }
  }

  async findOne(userDto: AdminDto):Promise<Admin> {
    const query = new Admin()
    query.username = userDto.username
    return await this.repository.findOne(query)
  }


}
