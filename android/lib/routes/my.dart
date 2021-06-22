import 'package:android/store/userModel.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';

class MyRoute extends StatefulWidget {
  @override
  _MyRouteState createState() => _MyRouteState();
}

class _MyRouteState extends State<MyRoute> {
  @override
  Widget build(BuildContext context) {
    var isLogin = Provider.of<UserModel>(context).isLogin;
    var user = Provider.of<UserModel>(context).user;

    return Scaffold(
      appBar: AppBar(title: Text("我的")),
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Expanded(
                child: Container(
              child: Column(
                children: [
                  GestureDetector(
                    child: Row(
                      children: [
                        Container(
                          child: Icon(
                            Icons.account_circle,
                            size: 64,
                            color: Colors.black38,
                          ),
                          margin: EdgeInsets.only(right: 10),
                        ),
                        Expanded(
                            child: Text(
                          isLogin ? user.nickname : "点击登录",
                          style: TextStyle(fontSize: 18),
                        ))
                      ],
                    ),
                    onTap: () {
                      if (!isLogin) {
                        Navigator.pushNamed(context, "/login");
                      }
                    },
                  )
                ],
              ),
            )),
            Container(
              padding: EdgeInsets.only(left: 20, right: 20),
              height: 36,
              child: isLogin
                  ? ElevatedButton(
                      onPressed: () {
                        context.read<UserModel>().logout();
                      },
                      child: Text('退出登录'),
                    )
                  : null,
            ),
          ],
        ),
      ),
    );
  }
}
