import 'package:android/model/room.dart';
import 'package:json_annotation/json_annotation.dart';

part 'roomTag.g.dart';

@JsonSerializable()
class RoomTag{
  int id;
  String name;
  List<Room> rooms;


  RoomTag(this.id,this.name, this.rooms);

  factory RoomTag.fromJson(Map<String, dynamic> json) => _$RoomTagFromJson(json);
  Map<String, dynamic> toJson() => _$RoomTagToJson(this);
}