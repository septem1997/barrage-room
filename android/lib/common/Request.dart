import 'dart:io';

import 'package:android/model/user.dart';
import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';

class Request{
  // 在网络请求过程中可能会需要使用当前的context信息，比如在请求失败时
  // 打开一个新路由，而打开新路由需要context信息。
  Request([this.context]) {
    _options = Options(extra: {"context": context});
  }

  BuildContext context;
  Options _options;
  static Dio dio = new Dio(BaseOptions(
    baseUrl: 'http://192.168.0.103:4000/',
    headers: {
    },
  ));

  static void init() {
    // 设置用户token（可能为null，代表未登录）
    // dio.options.headers[HttpHeaders.authorizationHeader] = Global.profile.token;

  }

  // 查询用户名是否可用
  Future<bool> usernameAvailable(String username) async {
    var r = await dio.get(
      "/user/usernameAvailable?username=$username",
    );
    return r.data['data'];
  }

  Future<Map<String,dynamic>> signup(User user) async {
    var r = await dio.post("user/signup",
        data: user);
    return r.data;
  }

  Future<Map<String,dynamic>> login(User user) async {
    var r = await dio.post("user/login",
        data: user);
    return r.data;
  }
}