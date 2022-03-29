import {Body, Controller, Get, HttpException, HttpStatus, Post, Query, Req, UseGuards} from '@nestjs/common';
import {BarrageService} from '../service/barrage.service';
import {Barrage} from '../entity/barrage';
import {BarrageDto} from "../dto/barrage.dto";

@Controller('barrage')
export class BarrageController {
    constructor(private readonly barrageService: BarrageService) {
    }

    @Get()
    getHello() {
        for (let i = 0; i < 100; i++) {
            // @ts-ignore
            const barrageDto: BarrageDto = {
                content: `这是第${i}条弹幕消息`
            }
            this.barrageService.createBarrage(barrageDto)
        }
    }

    @Get('list')
    async getBarrages(
        @Query("roomId") roomId: number
    ): Promise<Barrage[]> {
        return await this.barrageService.findBarrageByRoom(roomId);
    }

}
