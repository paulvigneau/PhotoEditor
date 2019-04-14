package example.com.projet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.widget.Toast;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageLoad {
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private static final int CAMERA_PERMISSION_CODE = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 1001;

    private String currentPhotoPath;
    private MainActivity main;

    public ImageLoad(MainActivity main){
        this.main = main;
    }

    public void takePictureFromCamera() {
        if (this.main.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.main.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE); // REQUEST CAMERA PERMISSION
        } else if (this.main.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.main.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);// REQUEST WRITE STORAGE PERMISSION
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(this.main.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    System.err.println("An error occurred while creating the File : " + ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    this.main.startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        }
    }

    public void takePictureFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        this.main.startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST);
    }

    public void savePictureToGallery(Bitmap bitmap) {
        if (this.main.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.main.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
        } else {
            OutputStream fOut = null;
            Uri outputFileUri;
            try {
                File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "MySuperApp" + File.separator);
                root.mkdirs();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
                String imageFileName = "PNG_" + timeStamp + ".png";
                File sdImageMainDirectory = new File(root, imageFileName);
                outputFileUri = Uri.fromFile(sdImageMainDirectory);
                System.out.println(outputFileUri);
                fOut = new FileOutputStream(sdImageMainDirectory);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();

                this.main.showMessage("Saved");
            } catch (Exception e) {
                this.main.showMessage("Error occured. Please try again later");
            }
        }
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,          // prefix
                ".jpg",                 // suffix
                storageDir              // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        this.currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.main.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        }
        return res;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

                int scaleFactor = Math.min(Math.round(bmOptions.outWidth / 1500.0f), Math.round(bmOptions.outHeight / 1500.0f));

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                this.main.setNewImage(bitmap);
            } else if (requestCode == GALLERY_REQUEST) {
                try {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.main.getContentResolver(), selectedImageUri);
                    float scaleFactor = Math.max(2000.0f / bitmap.getWidth(), 2000.0f / bitmap.getHeight());

                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleFactor, scaleFactor);

                    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                    bitmap.recycle();

                    this.main.setNewImage(resizedBitmap);
                }catch (Exception e) {
                    System.out.println("Error : " + e.getMessage());
                }
            }
        }
    }

}
