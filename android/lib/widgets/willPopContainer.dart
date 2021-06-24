import 'package:flutter/cupertino.dart';
import 'package:fluttertoast/fluttertoast.dart';

class WillPopContainer extends StatefulWidget {
  const WillPopContainer({Key key, this.child}) : super(key: key);

  final Widget child;

  @override
  _WillPopContainerState createState() => _WillPopContainerState();
}

class _WillPopContainerState extends State<WillPopContainer> {

  DateTime _lastPressedAt; //上次点击时间

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
        onWillPop: () async {
          if (_lastPressedAt == null ||
              DateTime.now().difference(_lastPressedAt) >
                  Duration(seconds: 2)) {
            //两次点击间隔超过2秒则重新计时
            _lastPressedAt = DateTime.now();
            Fluttertoast.showToast(msg: "再按一次退出房间");
            return false;
          }
          return true;
        },
        child: widget.child);
  }
}
