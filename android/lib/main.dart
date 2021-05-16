import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> with WidgetsBindingObserver {
  static const platform = const MethodChannel('com.septem1997.flutter/barrageRoom');
  var _hasPermission = false;
  var msgList = ["消息1","消息2","消息3"];
  String _tips = '请启用悬浮窗权限以正常使用功能';

  Future<void> _checkPermission() async {
    bool hasPermission =  await platform.invokeMethod('checkPermission');
    if(hasPermission){
      platform.invokeMethod('showFloatingWindow');
    }
    setState(() {
      _hasPermission = hasPermission;
    });
  }

  _turnOnPermission() {
    platform.invokeMethod('turnOnPermission');
  }

  @override
  initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _checkPermission();
  }

  @override
  void dispose() {
    super.dispose();
    WidgetsBinding.instance.removeObserver(this);
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
        Text(_tips),
      ],
    );

    var chatDialog = Column(
      children: [
        Expanded(
          flex: 1,
          child: ListView(
            children: [
              Text("消息1"),
              Text("消息2"),
              Text("消息3")
            ],
          )
        ),
        TextField(
          decoration: InputDecoration(
            border: OutlineInputBorder(),
            labelText: '请输入弹幕内容',
          ),
        )
      ],
    );


    return Material(
      child: Center(
        child: _hasPermission?chatDialog:checkPermissionTips,
      ),
    );
  }
}