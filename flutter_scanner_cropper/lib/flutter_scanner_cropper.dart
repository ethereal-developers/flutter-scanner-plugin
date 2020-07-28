import 'dart:async';

import 'package:flutter/services.dart';

class FlutterScannerCropper {
  static const MethodChannel _channel =
      const MethodChannel('flutter_scanner_cropper');

  static Future<String> get platformVersion async {
    final String methodName = "willThisMethodCallWork";
    final String version = await _channel.invokeMethod(methodName);
    return version;
  }
}