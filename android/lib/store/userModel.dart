import 'package:android/model/user.dart';
import 'package:flutter/material.dart';

class UserModel with ChangeNotifier {
  User _user;
  bool _isLogin;

  UserModel(this._user, this._isLogin);

  User get user => _user;
  bool get isLogin => _isLogin;

  void login(User user) {
    _user = user;
    _isLogin = true;
    notifyListeners();
  }
}