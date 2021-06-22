import 'package:flutter/material.dart';

class HallRoute extends StatefulWidget {

  @override
  _HallRouteState createState() => _HallRouteState();
}

class _HallRouteState extends State<HallRoute> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("大厅")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Text('大厅'),
      ),
    );
  }
}
