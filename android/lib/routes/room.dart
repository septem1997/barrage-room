import 'package:android/widgets/joinRoomList.dart';
import 'package:android/widgets/myRoomList.dart';
import 'package:flutter/material.dart';

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
    return DefaultTabController(
      length: tabs.length,
      initialIndex: tabIndex,
      child: Scaffold(
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
            onPageChanged: (index){
              tabIndex = index;
              _tabController.index = index;
            },
            children: tabPages,
            controller: _pageController,
          )),
    );
  }

  @override
  bool get wantKeepAlive => true;
}
