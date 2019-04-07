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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private static final int CAMERA_PERMISSION_CODE = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 1001;

    private String currentPhotoPath;

    private PhotoView photoView;

    public LayerType layerType;
    public Filter layerFilter;
    public View layerView;

    public Image image;
    public Image applyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoView = findViewById(R.id.imageID);
        this.image = getImage(R.drawable.image);
        this.applyImage = new Image(this.image);

        photoView.setImageBitmap(this.image.getBitmap());

        final Button applyButton = (Button) findViewById(R.id.applyID);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layerType.getType().applyLayer(MainActivity.this);
                photoView.setImageBitmap(layerFilter.getImageOut().getBitmap());
            }
        });

        Button undoButton = (Button) findViewById(R.id.undoID);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoView.setImageBitmap(layerFilter.getImageSrc().getBitmap());
                applyImage = new Image(layerFilter.getImageSrc());
            }
        });

        Spinner layersSpinner = (Spinner) findViewById(R.id.layersID);
        layersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                layerType = LayerType.values()[i];
                layerType.getType().setInflacter(MainActivity.this);
                layerType.getType().generateLayer(MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        photoView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                System.out.println("X : " + dragEvent.getX());
                System.out.println("Y : " + dragEvent.getY());
                return true;
            }
        });

    }

    private Image getImage(int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap map = BitmapFactory.decodeResource(getResources(), id, options);

        return new Image(map);
    }

    public void setImage(int id, int imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap map = BitmapFactory.decodeResource(getResources(), id, options);

        ImageView image = (ImageView) findViewById(imageView);
        image.setImageBitmap(map);
    }

    public void setApplyImage(){
        this.applyImage = this.layerFilter.getImageOut();
    }

    public void InflateLayer(int id, int parent) {
        if (this.layerView != null && this.layerView.getParent() != null) {
            ((ViewGroup) this.layerView).removeAllViews();
        }

        LayoutInflater inflater = getLayoutInflater();
        if (id == -1)
            return;
        this.layerView = inflater.inflate(id, (ViewGroup) findViewById(parent));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_layout, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.load_galery:
                takePictureFromGallery();
                return true;
            case R.id.load_camera:
                takePictureFromCamera();
                return true;
            case R.id.save_image:
                savePictureToGallery();
                return true;
            case R.id.reset_image:
                photoView.setImageBitmap(this.image.getBitmap());
                this.applyImage = new Image(this.image);
        }
        return false;
    }

    private void takePictureFromCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE); // REQUEST CAMERA PERMISSION
        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);// REQUEST WRITE STORAGE PERMISSION
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
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

                    System.out.println(photoFile);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        }
    }

    private void takePictureFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST);
    }

    private void savePictureToGallery() {
        if (photoView.getDrawable() != null) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
            } else {
                // Get the Bitmap from the Image
                Bitmap bitmap = this.layerFilter.getImageOut().getBitmap();

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

                    Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        } else
            Toast.makeText(this, "Load an image before saving it.", Toast.LENGTH_SHORT).show();
    }


    private File createImageFile() throws IOException {
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
        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        }
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(currentPhotoPath));
                    this.photoView.setImageBitmap(bitmap);
                    this.image = new Image(bitmap);
                    this.applyImage = new Image(this.image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                // Get the path from the Uri
                final String path = getPathFromURI(selectedImageUri);
                if (path != null) {
                    File f = new File(path);
                    selectedImageUri = Uri.fromFile(f);
                }

                // Set the image in ImageView
                this.photoView.setImageURI(selectedImageUri);
                this.image = new Image(((BitmapDrawable) this.photoView.getDrawable()).getBitmap());
                this.applyImage = new Image(this.image);
            }
            this.layerType.getType().generateLayer(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
            takePictureFromCamera();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
        }
    }
}
