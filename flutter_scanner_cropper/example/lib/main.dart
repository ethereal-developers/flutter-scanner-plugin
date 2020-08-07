import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_scanner_cropper/flutter_scanner_cropper.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterScannerCropper.platformVersion;
      String willItWork = await FlutterScannerCropper.testMethod;
      print('Testing whether it will work: $willItWork');
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            Center(
              child: Text('Running on: $_platformVersion\n'),
            ),
            RaisedButton(
              child: Text('Crop'),
              onPressed: () async {
                String pathToCroppedImage = await FlutterScannerCropper.openCrop(<String, String> {
                  'src': '/storage/emulated/0/Download/test.jpg',
                  'dest': '/storage/emulated/0/Download/',
                });
                print("\n\n\n\n\nResult from plugin which gives path to save file");
                print(pathToCroppedImage);
              },
            ),
          ],
        ),
        ),
    );
  }
}
