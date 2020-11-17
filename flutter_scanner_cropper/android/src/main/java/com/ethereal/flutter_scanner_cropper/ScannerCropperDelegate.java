package com.ethereal.flutter_scanner_cropper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.flutter.Log;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.util.PathUtils;

public class ScannerCropperDelegate implements PluginRegistry.ActivityResultListener {
    private static final int REQUEST_CODE = 99;
    private final Activity activity;
    private MethodChannel.Result result;

    public ScannerCropperDelegate(Activity activity) {
        this.activity = activity;
    }

    public void openCamera(MethodChannel.Result result, String imgPath, String tempSavePath, String shouldCompress) {
        String appDocDir = tempSavePath;
        this.result = result;
        if (tempSavePath == null) {
            appDocDir = "/storage/emulated/0/Download/";
        }
        Intent intent = new Intent(activity, ScanActivity.class);
        intent.putExtra(ScanConstants.IMAGE_PATH, imgPath);
        intent.putExtra(ScanConstants.TEMP_DIR, appDocDir);
        intent.putExtra(ScanConstants.SHOULD_COMPRESS, shouldCompress);

        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String uri = data.getStringExtra(ScanConstants.SCANNED_RESULT);
            Log.d("afterEverythingComplete", uri);
            result.success(uri);
            return false;
        }
        return true;
    }
}
