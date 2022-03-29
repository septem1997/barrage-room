import 'dart:async';
import 'dart:convert';

import 'package:android/common/request.dart';
import 'package:android/model/room.dart';
import 'package:android/model/user.dart';
import 'package:android/routes/chat.dart';
import 'package:android/store/userModel.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:rounded_loading_button/rounded_loading_button.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CreateRoomRoute extends StatefulWidget {
  @override
  _CreateRoomRouteState createState() => _CreateRoomRouteState();
}

class _CreateRoomRouteState extends State<CreateRoomRoute> {
  TextEditingController _nameController = new TextEditingController();
  TextEditingController _pwdController = new TextEditingController();
  GlobalKey _formKey = new GlobalKey<FormState>();
  RoundedLoadingButtonController _btnController;

  @override
  void initState() {
    super.initState();
    _btnController = RoundedLoadingButtonController();
  }

  List<Widget> buildForm() {
    var list = <Widget>[
      TextFormField(
          autofocus: true,
          controller: _nameController,
          maxLength: 32,
          decoration: InputDecoration(
            labelText: "房间名",
            hintText: "房间名",
            prefixIcon: Icon(Icons.home),
          ),
          validator: (v) {
            return v.trim().isNotEmpty ? null : "房间名不能为空";
          }),
      TextFormField(
        controller: _pwdController,
        maxLength: 16,
        decoration: InputDecoration(
            labelText: "房间口令", hintText: "房间口令", prefixIcon: Icon(Icons.lock)),
        validator: (v) {
          return v.trim().isNotEmpty ? null : "房间口令不能为空";
        },
      ),
      Padding(
        padding: const EdgeInsets.only(top: 25),
        child: ConstrainedBox(
          constraints: BoxConstraints.expand(height: 55.0),
          child: RoundedLoadingButton(
            onPressed: signup,
            animateOnTap: false,
            controller: _btnController,
            child: Text(
              '提交',
              style: TextStyle(fontSize: 16),
            ),
          ),
        ),
      )
    ];

    return list;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("创建房间")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          autovalidateMode: AutovalidateMode.always,
          child: SingleChildScrollView(
            child: Column(children: buildForm()),
          ),
        ),
      ),
    );
  }

  Future<void> signup() async {
    if ((_formKey.currentState as FormState).validate()) {
      _btnController.start();
      var room =
          Room(null, _nameController.text, _pwdController.text, null, null);
      Request(context).createRoom(room).then((value){
        Request(context).getMyRoom();
        _btnController.success();
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          duration: Duration(milliseconds: 500),
          content: Text("创建房间成功"),
        ));
        Timer(Duration(milliseconds: 500), () async {
          Navigator.pop(context);
        });
      }).catchError((_){
        _btnController.error();
        Timer(Duration(seconds: 1), () async {
          _btnController.reset();
        });
      });
    }

  }
}
