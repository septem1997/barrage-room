import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Barrage } from '../entity/barrage';
import { BarrageController } from '../controller/barrage.controller';
import { BarrageService } from '../service/barrage.service';

@Module({
  imports: [
    TypeOrmModule.forFeature([Barrage]),
  ],
  controllers:[BarrageController],
  providers:[BarrageService]
})
export class BarrageModule {
}
