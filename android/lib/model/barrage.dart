import 'package:json_annotation/json_annotation.dart';

part 'barrage.g.dart';

@JsonSerializable()
class Barrage {
  int id;
  String content;
  String sender;
  String createTime;

  Barrage({this.id, this.content, this.createTime, this.sender});

  factory Barrage.fromJson(Map<String, dynamic> json) => _$BarrageFromJson(json);

  Map<String, dynamic> toJson() => _$BarrageToJson(this);
}
