package com.ethereal.flutter_scanner_cropper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.flutter.Log;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class ScannerCropperDelegate implements PluginRegistry.ActivityResultListener {
    private static final int REQUEST_CODE = 99;
    private final Activity activity;
    private MethodChannel.Result pendingResult;
    private FileUtils fileUtils;

    public ScannerCropperDelegate(Activity activity) {
        this.activity = activity;
        fileUtils = new FileUtils();
    }

    public void openCamera(MethodChannel.Result result) {
        this.pendingResult = result;

        Intent intent = new Intent(activity, ScanActivity.class);
//        Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADownload%2Ftest.jpg");
//        intent.putExtra("ImagePath", "/storage/emulated/0/Download/test.jpg");
        Uri uri = Uri.parse("/storage/emulated/0/Download/test.jpg");
        intent.putExtra("ImagePath", uri);

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
