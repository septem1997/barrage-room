import 'package:android/model/room.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';

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
          var roomNo = room.roomNo;
          var roomPwd = room.password;
          return InkWell(
            onTap: () {},
            child: Container(
              padding: EdgeInsets.all(12),
              child: Row(
                children: [
                  Container(
                    width: 42,
                    height: 42,
                    child: Icon(
                      Icons.home_outlined,
                      size: 28,
                    ),
                  ),
                  Expanded(
                      child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('房间：' + room.name),
                      Text('编号：' + room.roomNo)
                    ],
                  )),
                  IconButton(
                      onPressed: () {
                        Fluttertoast.showToast(
                            msg: "复制房间信息成功"
                        );
                        Clipboard.setData(ClipboardData(
                            text: '快来共享弹幕加入我的房间吧，编号：$roomNo，口令：$roomPwd'));
                      },
                      icon: Icon(
                        Icons.copy,
                        size: 20,
                        color: Colors.blueGrey,
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
