import 'package:flutter/material.dart';

class RoomRoute extends StatefulWidget {

  @override
  _RoomRouteState createState() => _RoomRouteState();
}

class _RoomRouteState extends State<RoomRoute> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("房间")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Text('房间'),
      ),
    );
  }
}
