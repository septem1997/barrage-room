// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'barrage.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Barrage _$BarrageFromJson(Map<String, dynamic> json) {
  return Barrage(
    id: json['id'] as int,
    content: json['content'] as String,
    createTime: json['createTime'] as String,
    sender: json['sender'] as String,
  );
}

Map<String, dynamic> _$BarrageToJson(Barrage instance) => <String, dynamic>{
      'id': instance.id,
      'content': instance.content,
      'sender': instance.sender,
      'createTime': instance.createTime,
    };
