import 'package:android/store/userModel.dart';
import 'package:android/widgets/joinRoomList.dart';
import 'package:android/widgets/myRoomList.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';

class RoomRoute extends StatefulWidget {
  @override
  _RoomRouteState createState() => _RoomRouteState();
}

class _RoomRouteState extends State<RoomRoute>
    with
        AutomaticKeepAliveClientMixin<RoomRoute>,
        SingleTickerProviderStateMixin {
  TabController _tabController;
  PageController _pageController;
  var tabs = ["我创建的房间", "我加入的房间"];
  var tabPages = [MyRoomList(), JoinRoomList()];
  var tabIndex = 0;

  @override
  void initState() {
    super.initState();
    _pageController = PageController(initialPage: tabIndex, keepPage: true);
    _tabController = TabController(length: tabs.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);

    var isLogin = Provider.of<UserModel>(context).isLogin;

    return isLogin?Scaffold(
        appBar: AppBar(
          title: Text("房间"),
          bottom: TabBar(
              onTap: (index) {
                setState(() {
                  tabIndex = index;
                  _pageController.jumpToPage(index);
                });
              },
              controller: _tabController,
              tabs: tabs.map((e) => Tab(text: e)).toList()),
        ),
        body: PageView(
          onPageChanged: (index) {
            tabIndex = index;
            _tabController.index = index;
          },
          children: tabPages,
          controller: _pageController,
        )):Scaffold(
        appBar: AppBar(
          title: Text("房间"),
        ),
        body: Container(
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  "登录后才能创建和加入他人的房间",
                  style: TextStyle(color: Colors.black45),
                ),
                Padding(
                  padding: EdgeInsets.only(top: 24),
                  child: ElevatedButton(
                      onPressed: () {
                        Navigator.pushNamed(context, "/login");
                      },
                      child: Container(
                        width: 80,
                        height: 26,
                        child: Text('去登录',textAlign:TextAlign.center,style: TextStyle(fontSize: 16),),
                      )),
                )
              ],
            ),
          ),
        ));
  }

  @override
  bool get wantKeepAlive => true;
}
