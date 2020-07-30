import 'dart:async';

import 'package:flutter/services.dart';

class FlutterScannerCropper {
  static const MethodChannel _channel =
      const MethodChannel('flutter_scanner_cropper');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> get testMethod async {
    final String res = await _channel.invokeMethod('testMethod');
    return res;
  }
}
