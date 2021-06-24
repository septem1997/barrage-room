import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from '../entity/user';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { UserDto } from '../dto/user.dto';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class UserService {

  constructor(
    @InjectRepository(User)
    private repository: Repository<User>,
    private readonly jwtService: JwtService,
  ) {}

  register(pass: string):Promise<string> {
    const saltRounds = 10;
    return bcrypt.hash(pass, saltRounds);
  }

  async findUserByToken(token: string):Promise<User> {
    if (!token||token==='null') {
      return new Promise((resolve)=>{
        resolve(null)
      })
    }
    const userDto = new UserDto();
    userDto.username = (<any>this.jwtService.decode(token.substr(7))).username;
    return await this.findOne(userDto);
  }


  async login(userDto:UserDto) {
    const findUser = await this.findOne(userDto)
    if (findUser){
      const pwdIsCorrect = await bcrypt.compare(userDto.password, findUser.password)
      if (pwdIsCorrect){
        userDto.password = null
        return {
          token:'Bearer '+this.jwtService.sign(userDto),
          username:findUser.username,
          nickname:findUser.nickname
        }
      }else {
        throw new HttpException('用户名或密码错误', HttpStatus.BAD_REQUEST);
      }
    }else {
      throw new HttpException('找不到该用户', HttpStatus.NOT_FOUND);
    }
  }

  async create(userDto: UserDto) {
    const query  = new User()
    query.username = userDto.username
    const findAdmin = await this.repository.findOne(query)
    if (findAdmin){
      throw new HttpException('该用户名已存在', HttpStatus.CONFLICT);
    }
    const user = new User()
    const newPass = await this.register(userDto.password)
    user.username = userDto.username
    user.password = newPass
    // user.createTime = new Date().toISOString()
    user.nickname = userDto.nickname
    user.avatar = ""
    await this.repository.save(user)
    userDto.password = null
    return {
      token:'Bearer '+this.jwtService.sign(userDto),
      username:user.username,
      nickname:user.nickname
    }
  }

  async findOne(userDto: UserDto):Promise<User> {
    const query = new User()
    query.username = userDto.username
    return await this.repository.findOne(query)
  }


}
