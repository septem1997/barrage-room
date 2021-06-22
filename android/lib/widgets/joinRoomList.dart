import 'package:flutter/material.dart';

class JoinRoomList extends StatefulWidget {
  @override
  _JoinRoomListState createState() => _JoinRoomListState();
}

class _JoinRoomListState extends State<JoinRoomList>
    with AutomaticKeepAliveClientMixin {
  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // Add your onPressed code here!
        },
        child: const Icon(Icons.sensor_door_outlined),
        backgroundColor: Colors.green,
      ),
      body: Text('加入'),
    );
  }

  @override
  bool get wantKeepAlive => true;
}
