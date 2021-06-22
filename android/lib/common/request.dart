import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:android/model/roomTag.dart';
import 'package:android/model/user.dart';
import 'package:android/store/hallData.dart';
import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AppInterceptors extends Interceptor {
  @override
  Future<void> onRequest(
      RequestOptions options, RequestInterceptorHandler handler) async {
    final prefs = await SharedPreferences.getInstance();
    var userStr = prefs.getString('user');
    if (userStr != null) {
      var user = User.fromJson(json.decode(userStr));
      var token = user.token;
      options.headers.addAll({"Authorization": "Bearer $token"});
    }
    super.onRequest(options, handler);
  }

  @override
  void onError(DioError err, ErrorInterceptorHandler handler) {
    print('错误信息:' + err.response.toString());
    var context = err.requestOptions.extra['context'];
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
      content: Text(err.response.data['message']),
    ));
    super.onError(err, handler);
  }
}

class Request {
  Options _options;
  BuildContext context;

  // 在网络请求过程中可能会需要使用当前的context信息，比如在请求失败时
  // 打开一个新路由，而打开新路由需要context信息。
  Request([this.context]) {
    _options = Options(extra: {"context": context});
  }

  static Dio dio = new Dio(BaseOptions(
    baseUrl: 'http://192.168.0.103:4000/',
    headers: {},
  ));

  static void init() {
    // 设置用户token（可能为null，代表未登录）
    // dio.options.headers[HttpHeaders.authorizationHeader] = Global.profile.token;
    dio.interceptors.add(AppInterceptors());
  }

  // 查询用户名是否可用
  Future<bool> usernameAvailable(String username) async {
    var r = await dio.get(
      "/user/usernameAvailable?username=$username",
    );
    return r.data['data'];
  }

  Future<List<RoomTag>> getHallData() async {
    var r = await dio.get(
      "/room/tags",
    );
    List<RoomTag> list = [];
    var data = r.data['data'];
    data.forEach((item) => list.add(RoomTag.fromJson(item)));
    return list;
  }

  Future<Map<String, dynamic>> signup(User user) async {
    var r = await dio.post("user/signup", data: user, options: _options);
    return r.data;
  }

  Future<Map<String, dynamic>> login(User user) async {
    var r = await dio.post(
      "user/login",
      data: user,
      options: _options,
    );
    return r.data;
  }
}
