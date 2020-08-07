package com.ethereal.flutter_scanner_cropper;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.app.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterScannerCropperPlugin */
public class FlutterScannerCropperPlugin extends FlutterActivity implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_scanner_cropper");
    channel.setMethodCallHandler(this);
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_scanner_cropper");
    channel.setMethodCallHandler(new FlutterScannerCropperPlugin());

    FlutterScannerCropperPlugin plugin = new FlutterScannerCropperPlugin();

    ScannerCropperDelegate delegate = plugin.setupActivity(registrar.activity());
    registrar.addActivityResultListener(delegate);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "testMethod":
        String path = this.getApplicationContext().getCacheDir().getPath();
        result.success(path);
        break;
      case "startCamera":
        String imagePath = call.argument("src");
        String saveLoc = call.argument("dest");
        Log.d("onMethodCall", imagePath);
        delegate.openCamera(result, imagePath, saveLoc);
        break;
      default:
        result.notImplemented();
    }
  }

//  -----------------------------------

  private ScannerCropperDelegate delegate;

//  -----------------------------------

  public ScannerCropperDelegate setupActivity(Activity activity) {
    delegate = new ScannerCropperDelegate(activity);
    return delegate;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    setupActivity(binding.getActivity());
    binding.addActivityResultListener(delegate);
//    checkPermission();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivity() {
//    TODO: complete this
  }
}
