package com.ethereal.flutter_scanner_cropper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class ScannerCropperDelegate implements PluginRegistry.ActivityResultListener {
    private static final int REQUEST_CODE = 99;
    private final Activity activity;
//    private List<Uri> scannedBitmaps = new ArrayList<>();
    private MethodChannel.Result pendingResult;
    private FileUtils fileUtils;

    public ScannerCropperDelegate(Activity activity) {
        this.activity = activity;
        fileUtils = new FileUtils();
    }

    public void openCamera(MethodChannel.Result result){
//        scannedBitmaps.clear();

        this.pendingResult = result;

        Intent intent = new Intent(activity, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA);

        activity.startActivityForResult(intent, ScanConstants.START_CAMERA_REQUEST_CODE);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            activity.getContentResolver().delete(uri, null, null);
            pendingResult.success(fileUtils.getPathFromUri(activity, uri));
            return false;
        }
        return true;
    }
}
