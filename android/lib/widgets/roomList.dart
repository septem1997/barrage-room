import 'package:android/model/room.dart';
import 'package:flutter/material.dart';

class RoomList extends StatelessWidget {
  const RoomList({Key key,this.roomList}) : super(key: key);

  final List<Room> roomList;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView.separated(
        itemCount: roomList.length,
        itemBuilder: (context, index) {
          return ListTile(title: Text(roomList[index].name));
        },
        separatorBuilder: (context, index) => Divider(height: .0),
      ),
    );
  }
}
