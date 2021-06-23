import 'dart:convert';

import 'package:android/common/request.dart';
import 'package:android/model/user.dart';
import 'package:android/routes/createRoom.dart';
import 'package:android/routes/home.dart';
import 'package:android/routes/login.dart';
import 'package:android/store/hallData.dart';
import 'package:android/store/roomData.dart';
import 'package:android/store/userModel.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  var prefs = await SharedPreferences.getInstance();
  print(prefs);
  var userStr = prefs.getString('user');
  print("userStr$userStr");
  User user;
  if (userStr != null && userStr.isNotEmpty) {
    user = User.fromJson(json.decode(userStr));
  }
  Request.init();
  Provider.debugCheckInvalidValueType = null;
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider<UserModel>(create: (context) => UserModel(user, user != null)),
        ChangeNotifierProvider<HallData>(create: (context) => HallData([])),
        ChangeNotifierProvider<RoomData>(create: (context) => RoomData([],[])),
      ],
      child: MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '共享弹幕',
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
      routes: {
        "/": (context) => HomeRoute(),
        "/login": (context) => LoginRoute(),
        "/createRoom": (context) => CreateRoomRoute(),
      },
    );
  }
}
