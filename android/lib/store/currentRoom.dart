import 'dart:convert';
import 'dart:ffi';

import 'package:android/model/room.dart';
import 'package:android/model/roomTag.dart';
import 'package:android/model/user.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CurrentRoom with ChangeNotifier {
  Room _currentRoom;

  CurrentRoom(this._currentRoom);

  Room get currentRoom => _currentRoom;

  void setCurrentRoom(Room currentRoom) {
    _currentRoom = currentRoom;
    notifyListeners();
  }

}