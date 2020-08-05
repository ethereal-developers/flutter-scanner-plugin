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

public class ScannerCropperDelegate implements PluginRegistry.ActivityResultListener {
    private static final int REQUEST_CODE = 99;
    private final Activity activity;

    public ScannerCropperDelegate(Activity activity) {
        this.activity = activity;
    }

    public void openCamera(MethodChannel.Result result, String imgPath) {
        Intent intent = new Intent(activity, ScanActivity.class);
        intent.putExtra("ImagePath", imgPath);

        activity.startActivityForResult(intent, ScanConstants.START_CAMERA_REQUEST_CODE);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);

            Bitmap bitmap = null;
            try {
//                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                bitmap = BitmapFactory.decodeFile(uri.getPath());
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/save.jpg");
                FileOutputStream outStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(activity, "Image is Sucessfully Created and Saved !", Toast.LENGTH_LONG).show();
            activity.getContentResolver().delete(uri, null, null);
            return false;
        }
        return true;
    }
}
