import 'dart:convert';
import 'dart:ffi';

import 'package:android/model/room.dart';
import 'package:android/model/roomTag.dart';
import 'package:android/model/user.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class RoomData with ChangeNotifier {
  List<Room> _myRoomList;
  List<Room> _joinRoom;

  RoomData(this._myRoomList,this._joinRoom);

  List<Room> get myRoomList => _myRoomList;
  List<Room> get joinRoom => _joinRoom;

  void setMyRoom(List<Room> myRoomList) {
    _myRoomList = myRoomList;
    notifyListeners();
  }
  void setJoinRoom(List<Room> joinRoom) {
    _joinRoom = joinRoom;
    notifyListeners();
  }

}