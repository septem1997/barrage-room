import 'package:flutter/material.dart';

class MyRoomList extends StatefulWidget {
  @override
  _MyRoomListState createState() => _MyRoomListState();
}

class _MyRoomListState extends State<MyRoomList>
    with AutomaticKeepAliveClientMixin {
  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // Add your onPressed code here!
        },
        child: const Icon(Icons.post_add_outlined),
        backgroundColor: Colors.red,
      ),
      body: Text('加入'),
    );
  }

  @override
  bool get wantKeepAlive => true;
}
