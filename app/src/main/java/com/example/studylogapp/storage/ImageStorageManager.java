package com.example.studylogapp.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageStorageManager {
    private static final String TAG = "ImageStorageManager";
    private static final String IMAGE_DIR = "StudyLogImages";
    private Context context;

    public ImageStorageManager(Context context) {
        this.context = context;
    }

    public String saveImage(Bitmap bitmap) {
        File imageDir = getImageDirectory();
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File imageFile = new File(imageDir, imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Error saving image", e);
            return null;
        }
    }

    public String saveImageFromUri(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return saveImage(bitmap);
        } catch (IOException e) {
            Log.e(TAG, "Error saving image from URI", e);
            return null;
        }
    }

    public Bitmap loadImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return null;
            }
            return BitmapFactory.decodeFile(imagePath);
        } catch (Exception e) {
            Log.e(TAG, "Error loading image", e);
            return null;
        }
    }

    public boolean deleteImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return false;
        }
        try {
            File imageFile = new File(imagePath);
            return imageFile.delete();
        } catch (Exception e) {
            Log.e(TAG, "Error deleting image", e);
            return false;
        }
    }

    public File getImageDirectory() {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null) {
            storageDir = context.getFilesDir();
        }
        return new File(storageDir, IMAGE_DIR);
    }

    public Uri getImageUri(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return null;
        }
        return Uri.fromFile(imageFile);
    }
}


