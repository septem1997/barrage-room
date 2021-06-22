import 'package:android/routes/hall.dart';
import 'package:android/routes/my.dart';
import 'package:android/routes/room.dart';
import 'package:flutter/material.dart';

class HomeRoute extends StatefulWidget {
  @override
  _HomeRouteState createState() => _HomeRouteState();
}

class _HomeRouteState extends State<HomeRoute> {
  var pageList = [RoomRoute(), HallRoute(), MyRoute()];
  var currentIndex = 1;
  PageController _pageController;

  @override
  void initState() {
    this._pageController =
        PageController(initialPage: this.currentIndex, keepPage: true);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // var gm = GmLocalizations.of(context);
    return Scaffold(
      bottomNavigationBar: BottomNavigationBar(
        onTap: (value) {
          setState(() {
            currentIndex = value;
            _pageController.jumpToPage(value);
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
      body: PageView(
          children: pageList,
          controller: _pageController,
          physics: NeverScrollableScrollPhysics()),
    );
  }
}
