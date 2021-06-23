import 'package:android/common/request.dart';
import 'package:flutter/material.dart';

class JoinRoomList extends StatefulWidget {
  @override
  _JoinRoomListState createState() => _JoinRoomListState();
}

class _JoinRoomListState extends State<JoinRoomList>
    with AutomaticKeepAliveClientMixin {
  GlobalKey _formKey = GlobalKey<FormState>();
  TextEditingController _roomNoController = TextEditingController();
  TextEditingController _pwdController = TextEditingController();

  Widget buildForm() {
    return Form(
      key: _formKey,
      autovalidateMode: AutovalidateMode.always,
      child: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            TextFormField(
                autofocus: true,
                controller: _roomNoController,
                maxLength: 16,
                decoration: InputDecoration(
                  labelText: "房间编号",
                  hintText: "房间编号",
                  prefixIcon: Icon(Icons.home),
                ),
                validator: (v) {
                  return v.trim().isNotEmpty ? null : "房间编号不能为空";
                }),
            TextFormField(
              controller: _pwdController,
              maxLength: 16,
              decoration: InputDecoration(
                  labelText: "房间口令",
                  hintText: "房间口令",
                  prefixIcon: Icon(Icons.lock)),
              validator: (v) {
                return v.trim().isNotEmpty ? null : "房间口令不能为空";
              },
            )
          ],
        ),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    print("获取我加入的房间");
    Future.delayed(Duration.zero, () async {
      await Request(context).getJoinRoom();
    });
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          showDialog(
            context: context,
            barrierDismissible: false,
            builder: (BuildContext context) {
              return AlertDialog(
                title: Text('加入房间'),
                content: buildForm(),
                actions: <Widget>[
                  TextButton(
                    child: Text('取消'),
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                  ),
                  TextButton(
                    child: Text('确定'),
                    onPressed: () {
                      if ((_formKey.currentState as FormState).validate()) {
                        Request(context)
                            .joinRoom(
                                _roomNoController.text, _pwdController.text)
                            .then((value) {
                          Navigator.of(context).pushNamed('/chatRoom');
                        });
                      }
                    },
                  ),
                ],
              );
            },
          );
        },
        child: const Icon(Icons.sensor_door_outlined),
        backgroundColor: Colors.green,
      ),
      body: Container(
          width: double.infinity,
          height: double.infinity,
          child: Center(
            child: Text(
              '点击右下角按钮加入房间',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.black45),
            ),
          )),
    );
  }

  @override
  bool get wantKeepAlive => true;
}
