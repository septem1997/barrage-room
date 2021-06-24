import 'package:android/common/request.dart';
import 'package:android/model/roomTag.dart';
import 'package:android/store/currentRoom.dart';
import 'package:android/store/hallData.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';

class HallRoute extends StatefulWidget {
  @override
  _HallRouteState createState() => _HallRouteState();
}

class _HallRouteState extends State<HallRoute>
    with AutomaticKeepAliveClientMixin<HallRoute> {
  var currentIndex = 0;

  @override
  void initState() {
    super.initState();
    print("获取大厅数据");
    Future.delayed(Duration.zero, () async {
      var list = await Request(context).getHallData();
      context.read<HallData>().setData(list);
    });
  }

  Widget buildRoom(List<RoomTag> tags) {
    // todo 房间图标缓存
    if (tags == null || tags.length == 0) {
      return Container();
    }
    var currentRooms = tags[currentIndex].rooms;
    return Container(
      padding: EdgeInsets.all(12),
      color: Colors.white,
      child: Material(
          child: Ink(
              color: Colors.white,
              child: GridView.builder(
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2, //每行三列
                      childAspectRatio: 1.0 //显示区域宽高相等
                      ),
                  itemCount: currentRooms.length,
                  itemBuilder: (context, index) {
                    return InkWell(
                      onTap: () {
                        context.read<CurrentRoom>().setCurrentRoom(currentRooms[index]);
                        Navigator.of(context).pushNamed('/chatRoom');
                      },
                      child: Container(
                        padding: EdgeInsets.all(12),
                        child: Column(
                          children: [
                            Expanded(
                                child: Image.network(
                                    currentRooms[index].roomIcon)),
                            Text(currentRooms[index].name)
                          ],
                        ),
                      ),
                    );
                  }))),
    );
  }

  Widget buildTags(List<RoomTag> tags) {
    return Container(
      child: ListView.separated(
        itemCount: tags.length,
        itemBuilder: (context, index) {
          return InkWell(
              onTap: () {
                setState(() {
                  currentIndex = index;
                });
              },
              child: Container(
                  color:
                      currentIndex == index ? Colors.white : Colors.transparent,
                  padding: EdgeInsets.all(12),
                  child: Text(
                    tags[index].name,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        fontSize: currentIndex == index ? 16 : 14,
                        color: currentIndex == index
                            ? Colors.black
                            : Colors.black54),
                  )));
        },
        separatorBuilder: (context, index) => Divider(height: 0),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    var tags = Provider.of<HallData>(context).roomTags;
    return Scaffold(
      appBar: AppBar(title: Text("大厅")),
      body: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Container(
            color: Colors.black12,
            width: 100,
            child: buildTags(tags),
          ),
          Expanded(child: buildRoom(tags))
        ],
      ),
    );
  }

  @override
  bool get wantKeepAlive => true;
}
