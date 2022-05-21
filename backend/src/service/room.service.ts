import {HttpException, HttpStatus, Injectable} from '@nestjs/common';
import {InjectRepository} from '@nestjs/typeorm';
import {Repository} from 'typeorm';
import * as bcrypt from 'bcrypt';
import {JwtService} from '@nestjs/jwt';
import {Admin} from '../entity/admin';
import {AdminDto} from '../dto/admin.dto';
import {Room} from '../entity/room';
import {RoomTag} from '../entity/roomTag';
import {RoomDto} from "../dto/room.dto";

@Injectable()
export class RoomService {

    constructor(
        @InjectRepository(Room)
        private repository: Repository<Room>,
        @InjectRepository(RoomTag)
        private roomTagRepository: Repository<RoomTag>,
    ) {
    }


    async getRoomList(page: number, pageSize: number) {
        return await this.repository.findAndCount({
            skip: (page - 1) * pageSize,
            take: pageSize,
            where: {
                isPublic: false,
                disabled: false
            },
            relations: ['host']
        })
    }

    findById(id: number) {
        return this.repository.findOne(id, {
            relations: ['tag']
        })
    }

    async delTagById(id: number) {
        const tag = await this.roomTagRepository.findOne(id)
        tag.disabled = true
        return this.roomTagRepository.save(tag)
    }

    async getHallList(page: number, pageSize: number) {
        return await this.repository.findAndCount({
            skip: (page - 1) * pageSize,
            take: pageSize,
            where: {
                isPublic: true,
                disabled: false
            },
            relations: ['tag']
        })
    }


    async editTag(roomTag: RoomTag) {
        return await this.roomTagRepository.save(roomTag);
    }

    async findAllTags(): Promise<RoomTag[]> {
        const tag = new RoomTag();
        tag.disabled = false
        return await this.roomTagRepository.find(tag)
    }

    async findAllTagsAndRooms(): Promise<RoomTag[]> {
        return await this.roomTagRepository.createQueryBuilder('roomTag')
            .select([
                'roomTag.id',
                'roomTag.name',
                'room.id',
                'room.name',
                'room.roomIcon',
            ])
            .leftJoin('roomTag.rooms', 'room')
            .where('room.disabled = 0')
            .andWhere('roomTag.disabled = 0')
            .getMany();
        // return await this.roomTagRepository.find({
        //   relations:['rooms']
        // });
    }

    async editHall(room: RoomDto) {
        let saveRoom;
        if (room.id) {
            saveRoom = this.repository.findOne(room.id);
        } else {
            saveRoom = new Room();
            const number = await this.repository.count();
            const roomNoPre = room.tagId ? room.tagId.toString().padStart(3, '0') : '001';
            room.roomNo = roomNoPre + (number + 1).toString().padStart(6, '0');
            saveRoom.isPublic = true
        }
        if (room.tagId) {
            const tag = await this.roomTagRepository.findOne(room.tagId);
            room.tag = tag;
        }
        saveRoom = Object.assign(saveRoom, room);
        return await this.repository.save(saveRoom);
    }

    async editRoom(request, room: Room) {
        // todo roomDto
        let saveRoom;
        if (room.id) {
            saveRoom = this.repository.findOne(room.id);
        } else {
            saveRoom = new Room();
            const number = await this.repository.count();
            const roomNoPre = room.tagId ? room.tagId.toString().padStart(3, '0') : '000';
            room.roomNo = roomNoPre + (number + 1).toString().padStart(6, '0');
        }
        if (request) {
            room.host = request.user;
        }
        if (room.tagId) {
            const tag = await this.roomTagRepository.findOne(room.tagId);
            room.tag = tag;
        }
        saveRoom = Object.assign(saveRoom, room);

        return await this.repository.save(saveRoom);
    }

    async findRoomListByUser(user) {
        return await this.repository.createQueryBuilder('room')
            .select([
                'room.id',
                'room.name',
                'room.roomNo',
                'room.password',
                'room.createTime',
            ])
            .where('room.hostId = :id', {id: user.id})
            .getMany();
    }

    async joinRoom(request, room: Room) {
        const foundRoom = await this.repository.findOne({
            relations: ['members', 'host'],
            where: {roomNo: room.roomNo},
        });
        if (foundRoom == null) {
            throw new HttpException('该房间不存在，请确认编号是否正确', HttpStatus.NOT_FOUND);
        }
        if (foundRoom.password !== room.password) {
            throw new HttpException('房间编号与口令不匹配', HttpStatus.BAD_REQUEST);
        }
        if (!foundRoom.members) {
            foundRoom.members = [];
        }
        if (!foundRoom.members.map(user => user.id).includes(foundRoom.host.id)) {
            foundRoom.members.push(request.user);
            await this.repository.save(foundRoom);
        }
        foundRoom.members = null
        foundRoom.host = null
        return foundRoom;
    }

    async findJoinRoomListByUser(user) {
        return await this.repository.createQueryBuilder('room')
            .select([
                'room.id',
                'room.name',
                'room.roomNo',
                'room.password',
                'room.createTime',
            ])
            .leftJoinAndSelect('room.members', 'member')
            .where('member.id = :id', {id: user.id})
            .getMany();
    }
}
