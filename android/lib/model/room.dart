import 'package:json_annotation/json_annotation.dart';

part 'room.g.dart';

@JsonSerializable()
class Room{
  int id;
  String name;
  String password;
  String roomNo;
  String roomIcon;


  Room(this.id,this.name, this.password, this.roomNo, this.roomIcon);

  factory Room.fromJson(Map<String, dynamic> json) => _$RoomFromJson(json);
  Map<String, dynamic> toJson() => _$RoomToJson(this);
}