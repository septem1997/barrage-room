import {RoomTag} from "../entity/roomTag";

export class TagDto {
    id: number;
    name: string;
}

export class RoomDto{
    id:number;
    name:string;
    roomIcon:string;
    tagId:number;
    roomNo:string
    tag:RoomTag
}
