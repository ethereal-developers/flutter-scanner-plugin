import 'dart:async';

import 'package:flutter/services.dart';

class FlutterScannerCropper {
  static const MethodChannel _channel =
      const MethodChannel('flutter_scanner_cropper');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> openCrop({
    String src,
    String dest,
  }) async {
    Map<String, String> path = {
      'src': src,
      'dest': dest,
    };
    final String res = await _channel.invokeMethod('startCamera', path);
    return res;
  }

  static Future<String> compressImage(
      {String src, String dest, int desiredQuality}) async {
    final String res = await _channel.invokeMethod('compress', {
      'src': src,
      'dest': dest,
      'desiredQuality': '$desiredQuality',
    });
    return res;
  }
}
