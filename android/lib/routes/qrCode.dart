import 'package:flutter/material.dart';

import '../widgets/willPopContainer.dart';

class QRCodeRoute extends StatefulWidget {
  @override
  _QRCodeRouteState createState() => _QRCodeRouteState();
}

class _QRCodeRouteState extends State<QRCodeRoute> {

  @override
  Widget build(BuildContext context) {
    // var gm = GmLocalizations.of(context);
    return Scaffold(
        appBar: AppBar(title: Text("二维码分享")),
        body: Container(
          color: Colors.white,
          padding: EdgeInsets.all(20),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Image(image: NetworkImage("https://barrage.oss-cn-guangzhou.aliyuncs.com/%E4%BA%8C%E7%BB%B4%E7%A0%81%E5%9B%BE%E7%89%87_5%E6%9C%8821%E6%97%A520%E6%97%B625%E5%88%8604%E7%A7%92.png")),
              Text("扫描二维码进入房间",style:TextStyle(color: Colors.black26,fontSize: 18),textAlign: TextAlign.center,)
            ],
          ),
        ));
  }
}
