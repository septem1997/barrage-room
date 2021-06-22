import 'dart:convert';

import 'package:android/model/user.dart';
import 'package:android/routes/chat.dart';
import 'package:android/routes/hall.dart';
import 'package:android/routes/login.dart';
import 'package:android/routes/my.dart';
import 'package:android/routes/room.dart';
import 'package:android/store/userModel.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class HomeRoute extends StatefulWidget {
  @override
  _HomeRouteState createState() => _HomeRouteState();
}

class _HomeRouteState extends State<HomeRoute> {
  var pageList = [RoomRoute(), HallRoute(), MyRoute()];
  var currentIndex = 1;

  @override
  Widget build(BuildContext context) {
    // var gm = GmLocalizations.of(context);
    return Scaffold(
      bottomNavigationBar: BottomNavigationBar(
        onTap: (value) {
          setState(() {
            currentIndex = value;
          });
        },
        currentIndex: currentIndex,
        items: [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: "房间"),
          BottomNavigationBarItem(icon: Icon(Icons.apps), label: "大厅"),
          BottomNavigationBarItem(
              icon: Icon(Icons.account_circle_outlined), label: "我的")
        ],
      ),
      body: pageList[currentIndex],
    );
  }
}
