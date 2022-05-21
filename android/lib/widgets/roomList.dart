import 'package:android/model/room.dart';
import 'package:android/routes/home.dart';
import 'package:android/store/currentRoom.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:provider/provider.dart';

class RoomList extends StatelessWidget {
  RoomList({Key key, this.roomList}) : super(key: key);

  final List<Room> roomList;

  final List<Color> _colors = [
    Color(0xffb74093),
    Color(0xff1976D2),
    Color(0xffCDDC39),
    Color(0xffC2185B),
    Color(0xff2196F3),
    Color(0xff689F38)
  ];

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
            onTap: () {
              context.read<CurrentRoom>().setCurrentRoom(room);
              Navigator.of(context).pushNamed('/chatRoom');
            },
            child: Container(
              padding: EdgeInsets.all(12),
              child: Row(
                children: [
                  Container(
                    width: 42,
                    height: 42,
                    child: Icon(
                      Icons.home_outlined,
                      color: _colors[index%_colors.length],
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
                        Navigator.of(context).pushNamed('/QRCode');
                      },
                      icon: Icon(
                        Icons.qr_code,
                        size: 20,
                        color: Colors.blueGrey,
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
