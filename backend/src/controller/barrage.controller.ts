import {Body, Controller, Get, HttpException, HttpStatus, Post, Query, Req, UseGuards} from '@nestjs/common';
import {BarrageService} from '../service/barrage.service';
import {Barrage} from '../entity/barrage';
import {BarrageDto} from "../dto/barrage.dto";

@Controller('barrage')
export class BarrageController {
    constructor(private readonly barrageService: BarrageService) {
    }

    @Get('list')
    async getBarrages(
        @Query("roomId") roomId: number
    ): Promise<Barrage[]> {
        return await this.barrageService.findBarrageByRoom(roomId);
    }

}
