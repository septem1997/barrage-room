import { User } from '../entity/user';

export class BarrageDto {
  sender: string;
  content: string;
  roomId: number;
  userId:number;
  creator:User;
  createTime:string;
}
