import 'dart:convert';
import 'dart:ffi';

import 'package:android/model/roomTag.dart';
import 'package:android/model/user.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class HallData with ChangeNotifier {
  List<RoomTag> _roomTags;

  HallData(this._roomTags);

  List<RoomTag> get roomTags => _roomTags;

  void setData(List<RoomTag> roomTags) {
    _roomTags = roomTags;
    notifyListeners();
  }

}