import {Controller, Get, Post, UploadedFile, UseInterceptors} from '@nestjs/common';
import {AppService} from '../service/app.service';
import {v4} from 'uuid'
import {FileInterceptor} from "@nestjs/platform-express";
const fs = require('fs-extra')
const OSS = require('ali-oss')
const path = require('path')

@Controller()
export class AppController {
    constructor(private readonly appService: AppService) {
    }

    @Get()
    getHello(): string {
        return this.appService.getHello();
    }

    @Post('upload')
    @UseInterceptors(FileInterceptor('file'))
    async upload(@UploadedFile() file): Promise<any> {
        const fileName = v4() + '_' + file.originalname;
        await fs.writeFileSync(fileName, file.buffer)
        const client = new OSS({
            region: process.env.region,
            accessKeyId: process.env.access_key_id,
            accessKeySecret: process.env.secret_access_key,
            bucket: process.env.bucket,
        });
        const result = await client.put(fileName, path.normalize(fileName));
        await fs.unlinkSync(fileName)
        return result.url
    }
}
