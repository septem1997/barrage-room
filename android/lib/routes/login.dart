import 'dart:async';
import 'dart:convert';

import 'package:android/common/request.dart';
import 'package:android/model/user.dart';
import 'package:android/routes/chat.dart';
import 'package:android/store/userModel.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:rounded_loading_button/rounded_loading_button.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginRoute extends StatefulWidget {
  @override
  _LoginRouteState createState() => _LoginRouteState();
}

enum Status { nextStep, login, signup }

class _LoginRouteState extends State<LoginRoute> {
  TextEditingController _unameController = new TextEditingController();
  TextEditingController _nicknameController = new TextEditingController();
  TextEditingController _pwdController = new TextEditingController();
  TextEditingController _pwdConfirmController = new TextEditingController();
  bool pwdShow = false; //密码是否显示明文
  GlobalKey _formKey = new GlobalKey<FormState>();
  var _status = Status.nextStep;
  var _buttonText = {
    Status.nextStep: "下一步",
    Status.login: "登录",
    Status.signup: "注册",
  };
  final RoundedLoadingButtonController _btnController =
      RoundedLoadingButtonController();
  FocusNode pwdFocusNode;
  FocusNode unameFocusNode;

  @override
  void initState() {
    super.initState();

    pwdFocusNode = FocusNode();
    unameFocusNode = FocusNode();
    unameFocusNode.addListener(() async {
      if (!unameFocusNode.hasFocus) {
        print("uname失去焦点");
        if (_unameController.text.isEmpty) {
          // ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          //   content: Text("请输入用户名"),
          // ));
          return;
        }
        checkUserExist();
      }
    });
  }

  void checkUserExist() {
    Timer(Duration(seconds: 1), () async {
      var usernameAvailable =
          await Request(context).usernameAvailable(_unameController.text);
      setState(() {
        _status = usernameAvailable ? Status.signup : Status.login;
      });
      _btnController.stop();
      pwdFocusNode.requestFocus();
    });
    _btnController.start();
  }

  List<Widget> buildForm() {
    var list = <Widget>[
      TextFormField(
          autofocus: true,
          controller: _unameController,
          focusNode: unameFocusNode,
          decoration: InputDecoration(
            labelText: "用户名",
            hintText: "请输入用户名",
            prefixIcon: Icon(Icons.person),
          ),
          // 校验用户名（不能为空）
          validator: (v) {
            return v.trim().isNotEmpty ? null : "用户名不能为空";
          }),
    ];
    if (_status == Status.signup || _status == Status.login) {
      list.add(TextFormField(
        controller: _pwdController,
        focusNode: pwdFocusNode,
        decoration: InputDecoration(
            labelText: "密码",
            hintText: "请输入密码",
            prefixIcon: Icon(Icons.lock),
            suffixIcon: IconButton(
              icon: Icon(pwdShow ? Icons.visibility_off : Icons.visibility),
              onPressed: () {
                setState(() {
                  pwdShow = !pwdShow;
                });
              },
            )),
        obscureText: !pwdShow,
        //校验密码（不能为空）
        validator: (v) {
          return v.trim().isNotEmpty ? null : "密码不能为空";
        },
      ));
    }
    if (_status == Status.signup) {
      list.addAll([
        TextFormField(
          controller: _pwdConfirmController,
          decoration: InputDecoration(
              labelText: "确认密码",
              hintText: "请再次输入密码",
              prefixIcon: Icon(Icons.lock),
              suffixIcon: IconButton(
                icon: Icon(pwdShow ? Icons.visibility_off : Icons.visibility),
                onPressed: () {
                  setState(() {
                    pwdShow = !pwdShow;
                  });
                },
              )),
          obscureText: !pwdShow,
          //校验密码（不能为空）
          validator: (v) {
            if(!v.trim().isNotEmpty){
              return "确认密码不能为空";
            }
            if(v!=_pwdController.text){
              return "两次密码不一致";
            }
            return null;
          },
        ),
        TextFormField(
            controller: _nicknameController,
            decoration: InputDecoration(
              labelText: "昵称",
              hintText: "请输入昵称",
              prefixIcon: Icon(Icons.person),
            ),
            // 校验用户名（不能为空）
            validator: (v) {
              return v.trim().isNotEmpty ? null : "昵称不能为空";
            }),
      ]);
    }

    if (_status == Status.signup || _status == Status.login) {
      list.add(Container(
        padding: const EdgeInsets.only(top: 25),
        alignment: Alignment.centerLeft,
        child: GestureDetector(
          onTap: () {
            ScaffoldMessenger.of(context).showSnackBar(SnackBar(
              content: Text(_status == Status.signup
                  ? "找不到该用户，检查下是否填写正确了吧"
                  : "该用户名已被占用，重新输入一个吧"),
            ));
          },
          child: Text(_status == Status.signup ? "想要登录？" : "想要注册？",
              style: TextStyle(color: Colors.blue)),
        ),
      ));
    }

    list.addAll([
      Padding(
        padding: const EdgeInsets.only(top: 25),
        child: ConstrainedBox(
          constraints: BoxConstraints.expand(height: 55.0),
          child: RoundedLoadingButton(
            onPressed: _onBtnPress,
            animateOnTap: false,
            controller: _btnController,
            child: Text(
              _buttonText[_status],
              style: TextStyle(fontSize: 16),
            ),
          ),
        ),
      )
    ]);

    return list;
  }

  @override
  Widget build(BuildContext context) {
    // var gm = GmLocalizations.of(context);
    return Scaffold(
      appBar: AppBar(title: Text("登录")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          autovalidate: true,
          child: Column(children: buildForm()),
        ),
      ),
    );
  }

  void _onBtnPress() async {
    switch (_status) {
      case Status.nextStep:
        if (_unameController.text.isEmpty) {
          ScaffoldMessenger.of(context).showSnackBar(SnackBar(
            content: Text("请输入用户名"),
          ));
          return;
        }
        unameFocusNode.unfocus();
        break;
      case Status.login:
        login();
        break;
      case Status.signup:
        signup();
    }
  }

  void toRegister() {}

  Future<void> saveUser(data) async {
    if (data['code'] == 0) {
      var user = User.fromJson(data['data']);
      context.read<UserModel>().login(user);
      Navigator.pop(context);
    } else {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text(data['message'])));
    }
  }

  Future<void> login() async {
    if ((_formKey.currentState as FormState).validate()) {
      var user = User(_unameController.text, null, null,_pwdController.text);
      var response = await Request(context).login(user);
      saveUser(response);
    }
  }
  
  Future<void> signup() async {
    if ((_formKey.currentState as FormState).validate()) {
      var user = User(_unameController.text, _nicknameController.text, null,_pwdController.text);
      var response = await Request(context).signup(user);
      saveUser(response);
    }
  }
}
