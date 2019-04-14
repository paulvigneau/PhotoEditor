package example.com.projet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import example.com.projet.filter.Filter;

public class MainActivity extends AppCompatActivity {
    private PhotoView photoView;
    private ImageLoad imageLoad;

    public boolean inRenderScript;
    public LayerType layerType;
    public Filter layerFilter;
    public View layerView;
    public Runnable renderThread;
    public ProgressBar progress;

    public Image image;
    public Image applyImage;

    public HistogramView histogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.photoView = findViewById(R.id.imageID);
        this.image = getImage(R.drawable.image);
        this.applyImage = new Image(this.image);
        this.inRenderScript = false;

        this.photoView.setImageBitmap(this.image.getBitmap());
        this.imageLoad = new ImageLoad(this);
        this.histogram = new HistogramView((ImageView)findViewById(R.id.histogram));
        this.progress = findViewById(R.id.progressBar);

        this.renderThread = new Runnable() {
            @Override
            public void run() {
                layerType.getType().applyLayer(MainActivity.this, inRenderScript);
                photoView.setImageBitmap(layerFilter.getImageOut().getBitmap());
                progress.setVisibility(View.INVISIBLE);
            }
        };

        final Button applyButton = (Button) findViewById(R.id.applyID);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                new Thread(renderThread).start();
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

        ImageButton histogramButton = (ImageButton) findViewById(R.id.histogram_button);
        histogramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                histogram.applyFilter(applyImage);

                ConstraintLayout histogramView = (ConstraintLayout)findViewById(R.id.histogram_view);
                histogramView.setVisibility(View.VISIBLE);
            }
        });

        ImageButton closeHistogram = (ImageButton)findViewById(R.id.close_histogram);
        closeHistogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout histogramView = (ConstraintLayout)findViewById(R.id.histogram_view);
                histogramView.setVisibility(View.INVISIBLE);
            }
        });

        for(LayerType type : LayerType.values()){
            type.generateSelectButton(this);
        }
        layerType = LayerType.values()[0];
        layerType.getType().setInflacter(MainActivity.this);
        layerType.getType().generateLayer(MainActivity.this);

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

    public void setNewImage(Bitmap map){
        this.photoView.setImageBitmap(map);
        this.image = new Image(map);
        this.applyImage = new Image(map);

        this.layerType.getType().generateLayer(this);
    }

    public void setLayerType(LayerType type){
        this.layerType = type;
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
                this.imageLoad.takePictureFromGallery();
                break;
            case R.id.load_camera:
                this.imageLoad.takePictureFromCamera();
                break;
            case R.id.save_image:
                if(this.photoView.getDrawable() != null){
                    this.imageLoad.savePictureToGallery(((BitmapDrawable)this.photoView.getDrawable()).getBitmap());
                }
                break;
            case R.id.reset_image:
                photoView.setImageBitmap(this.image.getBitmap());
                this.applyImage = new Image(this.image);
                this.layerFilter.setImageSrc(this.applyImage);
                break;
            case R.id.rs_checkbox:
                this.inRenderScript = !item.isChecked();
                item.setChecked(this.inRenderScript);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.imageLoad.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
            this.imageLoad.takePictureFromCamera();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
        }
    }

    public void showMessage(final String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
