import 'dart:convert';

import 'package:android/common/request.dart';
import 'package:android/model/barrage.dart';
import 'package:android/model/room.dart';
import 'package:android/store/currentRoom.dart';
import 'package:android/store/userModel.dart';
import 'package:android/widgets/willPopContainer.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:socket_io_client/socket_io_client.dart' as IO;
import 'package:socket_io_client/socket_io_client.dart';

class ChatRoute extends StatefulWidget {
  @override
  _ChatRouteState createState() => _ChatRouteState();
}

class _ChatRouteState extends State<ChatRoute> with WidgetsBindingObserver {
  static const CHANNEL = 'com.septem1997.flutter/barrageRoom';
  static const platform = const MethodChannel(CHANNEL);
  var _hasPermission = false;
  var msgList = ["消息1", "消息2", "消息3"];
  String _tips = '请启用悬浮窗权限以正常使用功能';
  IO.Socket socket;

  var _barrageList = <Barrage>[];

  Future<void> _checkPermission() async {
    bool hasPermission = await platform.invokeMethod('checkPermission');
    if (hasPermission) {
      platform.invokeMethod('showFloatingWindow');
    }
    setState(() {
      _hasPermission = hasPermission;
    });
  }

  initSocket(Room room) async {
    var user = Provider.of<UserModel>(context, listen: false).user;

    // todo 改成全局变量
    socket = IO.io(
        Request.baseUrl,
        OptionBuilder().setQuery({
          'roomId':room.id,
          'token': user == null ? '' : user.token
        }).setTransports(['websocket']).build());
    socket.onConnect((_) {
      print('connect');
    });
    socket.on("receiveMsg", (data) {
      print('socket:receiveMsg ----- ' + data.toString());
      var barrage = Barrage.fromJson(data);
      setState(() {
        _barrageList.add(barrage);
      });
      platform.invokeMethod("receiveMsg", barrage.content);
    });
    socket.onDisconnect((_) => print('disconnect'));

    BasicMessageChannel<String> _basicMessageChannel =
        BasicMessageChannel(CHANNEL, StringCodec());

    _basicMessageChannel
        .setMessageHandler((String message) => Future<String>(() {
              print('socket:sendMsg' + message);
              Map<String, String> map = new Map();
              map["content"] = message;
              socket.emit("sendMsg", map);
              return "success";
            }));
  }

  @override
  initState() {
    super.initState();
    print('初始化房间');
    Future.delayed(Duration.zero, () async {
      var room = Provider.of<CurrentRoom>(context,listen: false).currentRoom;
      if(room==null){
        Navigator.pop(context);
        return;
      }
      WidgetsBinding.instance.addObserver(this);
      _checkPermission();
      // _getBarrageList();
      await initSocket(room);
      Fluttertoast.showToast(
          gravity: ToastGravity.CENTER,
          msg: "进入房间成功，现在可以切到其他应用一边聊天了",
          toastLength: Toast.LENGTH_LONG);
    });
  }

  @override
  void dispose() {
    if (socket != null && socket.connected) {
      socket.disconnect();
    }
    print('销毁房间');
    platform.invokeMethod("unbindService");
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    if (state == AppLifecycleState.resumed) {
      _checkPermission();
    }
  }

  Widget buildPage(Room room) {
    if (!_hasPermission) {
      return Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          ElevatedButton(
            child: Text('启用悬浮窗权限'),
            onPressed: () {
              platform.invokeMethod('turnOnPermission');
            },
          ),
          Text(_tips)
        ],
      );
    }

    return Container(
      child: ListView.separated(
        itemCount: _barrageList.length,
        itemBuilder: (context, index) {
          var barrage = _barrageList[index];
          return Padding(
              padding: EdgeInsets.all(12),
              child: Row(
                children: [
                  Container(
                    width: 42,
                    height: 42,
                    child: Icon(
                      Icons.account_circle_outlined,
                      size: 28,
                    ),
                  ),
                  Expanded(
                      child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(barrage.sender+"："),
                      Text(barrage.content)
                    ],
                  )),
                ],
              ));
        },
        separatorBuilder: (context, index) => Divider(height: .0),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    var room = Provider.of<CurrentRoom>(context).currentRoom;
    if (room == null) {
      return Container(
          width: double.infinity,
          height: double.infinity,
          child: Center(
            child: Text(
              '无房间信息，请返回重新进入',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.black45),
            ),
          ));
    }

    return Scaffold(
        appBar: AppBar(title: Text(room.name)),
        body: WillPopContainer(
          child: buildPage(room),
        ));
  }

  Future<void> _getBarrageList() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('token');
    var response = await Dio().get(
      "http://192.168.0.102:8080/barrage/list",
      options: Options(
        headers: {"token": token},
      ),
    );
    setState(() {
      _barrageList.addAll(response.data['result']
          .map((barrage) => barrage['userNickname'] + "：" + barrage['content'])
          .toList());
    });
  }
}
