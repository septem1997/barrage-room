import 'dart:convert';

import 'package:android/model/user.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserModel with ChangeNotifier {
  User _user;
  bool _isLogin;

  UserModel(this._user, this._isLogin);

  User get user => _user;
  bool get isLogin => _isLogin;

  Future<void> login(User user) async {
    _user = user;
    final prefs = await SharedPreferences.getInstance();
    prefs.setString('user', json.encode(user.toJson()));
    _isLogin = true;
    notifyListeners();
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    prefs.remove('user');
    _user = null;
    _isLogin = false;
    notifyListeners();
  }
}