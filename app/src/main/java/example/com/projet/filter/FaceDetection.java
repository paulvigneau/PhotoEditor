package example.com.projet.filter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import example.com.projet.Image;
import example.com.projet.MainActivity;

import example.com.projet.R;

/**
 * Represent the FaceDetector filter
 */
public class FaceDetection extends Filter {

    /**
     * The face detector constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The image source
     */
    public FaceDetection(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    protected void applyJava() {
        Bitmap btm = super.imageSrc.getBitmap();
        Bitmap tempBitmap = Bitmap.createBitmap(btm.getWidth(), btm.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(btm, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector.Builder(super.main.getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();
        if(!faceDetector.isOperational()){
            main.showMessage("Impossible d'utiliser la reconnaissance de visage");
            //Toast.makeText(super.main, "Impossible d'utiliser la reconnaissance de visage", Toast.LENGTH_SHORT).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(btm).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        for(int i = 0; i < faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            // Placement du monocle centré sur l'oeil droit
            PointF pointF = thisFace.getLandmarks().get(Landmark.BOTTOM_MOUTH).getPosition(); // BUG: Corresponds to right eye
            float ratioX = thisFace.getWidth() / 5;
            float ratioY = thisFace.getHeight() / 5;

            Bitmap b1 = BitmapFactory.decodeResource(super.main.getResources(), R.drawable.right_monocle);
            tempCanvas.drawBitmap(b1,null,  new RectF(pointF.x - ratioX / 2, pointF.y - ratioY / 4,
                    pointF.x + ratioX / 2, pointF.y + ratioY + ratioY / 4),null);

            // Placement du nez rouge
            pointF = thisFace.getLandmarks().get(Landmark.LEFT_EAR_TIP).getPosition(); // BUG: Corresponds to nose
            ratioX = thisFace.getWidth() / 5;

            Bitmap b2 = BitmapFactory.decodeResource(super.main.getResources(), R.drawable.red_nose);
            tempCanvas.drawBitmap(b2,null,  new RectF(pointF.x - ratioX / 2, pointF.y - ratioX / 2,
                    pointF.x + ratioX / 2, pointF.y + ratioX / 2),null);
        }
        super.imageOut.setBitmap(tempBitmap);

    }

    @Override
    protected void applyRenderScript() {
        main.showMessage("Not available in RenderScript");
        // Execution de la version Java
        this.applyJava();
    }
}
