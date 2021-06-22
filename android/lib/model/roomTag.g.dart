// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'roomTag.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RoomTag _$RoomTagFromJson(Map<String, dynamic> json) {
  return RoomTag(
    json['id'] as int,
    json['name'] as String,
    (json['rooms'] as List)
        ?.map(
            (e) => e == null ? null : Room.fromJson(e as Map<String, dynamic>))
        ?.toList(),
  );
}

Map<String, dynamic> _$RoomTagToJson(RoomTag instance) => <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'rooms': instance.rooms,
    };
