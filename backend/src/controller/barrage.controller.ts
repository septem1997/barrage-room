import {Body, Controller, Get, HttpException, HttpStatus, Post, Query, Req, UseGuards} from '@nestjs/common';
import {BarrageService} from '../service/barrage.service';
import {Barrage} from '../entity/barrage';
import {BarrageDto} from "../dto/barrage.dto";
import {PageListConvert} from "../annotations/converter";

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

    @Get('all')
    @PageListConvert
    async getAllBarrages(
        @Query("pageSize") pageSize: number,
        @Query("page") page: number
    ) {
        return await this.barrageService.findAll(page,pageSize);
    }

}
