
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:socket_io_client/socket_io_client.dart' as IO;
import 'package:socket_io_client/socket_io_client.dart';

class WillPopScopeTestRoute extends StatefulWidget {
  @override
  WillPopScopeTestRouteState createState() {
    return new WillPopScopeTestRouteState();
  }
}

class WillPopScopeTestRouteState extends State<WillPopScopeTestRoute> {
  DateTime _lastPressedAt; //上次点击时间

  @override
  Widget build(BuildContext context) {
    return new WillPopScope(
        onWillPop: () async {
          if (_lastPressedAt == null ||
              DateTime.now().difference(_lastPressedAt) >
                  Duration(seconds: 1)) {
            //两次点击间隔超过1秒则重新计时
            _lastPressedAt = DateTime.now();
            return false;
          }
          return true;
        },
        child: Container(
          alignment: Alignment.center,
          child: Text("1秒内连续按两次返回键退出"),
        ));
  }
}

class ChatRoute extends StatefulWidget {
  ChatRoute({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

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

  var _barrageList = <String>[];

  Future<void> _checkPermission() async {
    bool hasPermission = await platform.invokeMethod('checkPermission');
    if (hasPermission) {
      platform.invokeMethod('showFloatingWindow');
    }
    setState(() {
      _hasPermission = hasPermission;
    });
  }

  initSocket() async {
    final prefs = await SharedPreferences.getInstance();

    final token = prefs.getString('token');
    socket = IO.io(
        'http://192.168.0.102:8081/',
        OptionBuilder().setQuery({'token': token}).setTransports(
                ['websocket']) // for Flutter or Dart VM
            .build());
    socket.onConnect((_) {
      print('connect');
    });
    socket.on("receiveMsg", (data) => {_onReceiveMsg(data)});
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

  _onReceiveMsg(data) {
    print('socket:receiveMsg' + data.toString());
    setState(() {
      _barrageList.add(data['userNickname']+"："+data['content']);
    });
    platform.invokeMethod("receiveMsg", data['content']);
  }

  _turnOnPermission() {
    platform.invokeMethod('turnOnPermission');
  }

  @override
  initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _checkPermission();
    _getBarrageList();
    initSocket();
  }

  @override
  void dispose() {
    super.dispose();
    WidgetsBinding.instance.removeObserver(this);
    socket.disconnect();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    if (state == AppLifecycleState.resumed) {
      _checkPermission();
    }
  }

  Widget build(BuildContext context) {
    var checkPermissionTips = Column(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        ElevatedButton(
          child: Text('启用悬浮窗权限'),
          onPressed: _turnOnPermission,
        ),
        Text(_tips)
      ],
    );

    var chatDialog = Container(
      child: ListView.separated(
        itemCount: _barrageList.length,
        itemBuilder: (context, index) {
          return ListTile(title: Text(_barrageList[index]));
        },
        separatorBuilder: (context, index) => Divider(height: .0),
      ),
    );

    return Scaffold(
        appBar: AppBar(title: Text("共享弹幕")),
        body: Padding(
            padding: const EdgeInsets.all(16.0),
            child: _hasPermission ? chatDialog : checkPermissionTips));
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
    print(response.data['result'].toString());
    setState(() {
      _barrageList.addAll(response.data['result'].map(
        (barrage) => barrage['userNickname']+"："+barrage['content']
      ).toList());
    });
  }
}
