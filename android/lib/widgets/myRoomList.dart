import 'package:android/common/request.dart';
import 'package:android/store/roomData.dart';
import 'package:android/widgets/roomList.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class MyRoomList extends StatefulWidget {
  @override
  _MyRoomListState createState() => _MyRoomListState();
}

class _MyRoomListState extends State<MyRoomList>
    with AutomaticKeepAliveClientMixin {

  @override
  void initState() {
    super.initState();
    print("获取我创建的房间");
    Future.delayed(Duration.zero, () async {
      await Request(context).getMyRoom();
    });
  }

  // todo 下拉刷新

  @override
  Widget build(BuildContext context) {
    super.build(context);

    var list = Provider.of<RoomData>(context).myRoomList;

    return Scaffold(
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            Navigator.pushNamed(context, '/createRoom');
          },
          child: const Icon(Icons.add),
          backgroundColor: Colors.red,
        ),
        body: list.isEmpty
            ? Container(
                width: double.infinity,
                height: double.infinity,
                child: Center(
                  child: Text(
                    '点击右下角按钮新建房间',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: Colors.black45),
                  ),
                ))
            : RoomList(roomList: list));
  }

  @override
  bool get wantKeepAlive => true;
}
