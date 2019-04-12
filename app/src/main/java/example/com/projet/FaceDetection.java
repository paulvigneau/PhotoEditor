package example.com.projet;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class FaceDetection extends Filter {

    public FaceDetection(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    protected void applyJava() {
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);

        Bitmap btm = super.imageSrc.getBitmap();
        Bitmap tempBitmap = Bitmap.createBitmap(btm.getWidth(), btm.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(btm, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector.Builder(super.main.getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();
        if(!faceDetector.isOperational()){
            Toast.makeText(super.main, "Impossible d'utiliser la reconnaissance de visage", Toast.LENGTH_SHORT).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(btm).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        for(int i = 0; i < faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();

//                    tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);

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
//        photoView.setImageDrawable(new BitmapDrawable(super.main.getResources(), tempBitmap));

    }

    @Override
    protected void applyRenderScript() {
        Toast.makeText(super.main, "Version RenderScript non disponible. Exécution de la version Java", Toast.LENGTH_LONG).show();

        // Execution de la version Java
        this.applyJava();
    }
}
