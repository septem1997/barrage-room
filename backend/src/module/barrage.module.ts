import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Barrage } from '../entity/barrage';
import { BarrageController } from '../controller/barrage.controller';
import { BarrageService } from '../service/barrage.service';
const barrageRep = TypeOrmModule.forFeature([Barrage]);
@Module({
  imports: [
    barrageRep,
  ],
  controllers:[BarrageController],
  providers:[BarrageService],
  exports:[barrageRep,BarrageService]
})
export class BarrageModule {
}
