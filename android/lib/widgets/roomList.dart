import 'package:android/model/room.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class RoomList extends StatelessWidget {
  const RoomList({Key key, this.roomList}) : super(key: key);

  final List<Room> roomList;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView.separated(
        itemCount: roomList.length,
        itemBuilder: (context, index) {
          var room = roomList.elementAt(index);
          return InkWell(
            onTap: () {},
            child: Container(
              padding: EdgeInsets.all(12),
              child: Row(
                children: [
                  Container(
                    width: 42,
                    height: 42,
                    child: Icon(Icons.home_outlined),
                  ),
                  Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('房间：' + room.name),
                          Text('编号：' + room.roomNo)
                        ],
                      ))
                ],
              ),
            ),
          );
        },
        separatorBuilder: (context, index) => Divider(height: .0),
      ),
    );
  }
}
