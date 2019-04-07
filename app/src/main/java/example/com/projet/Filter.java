package example.com.projet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public abstract class Filter {
    protected MainActivity main;
    protected Image imageSrc;
    protected Image imageOut;

    public Filter(MainActivity main, Image image) {
        this.main = main;

        this.imageSrc = image;
        this.imageOut = new Image(image);
    }

    public void apply(boolean inRS){
        if(inRS){
            applyRenderScript();
        }else{
            applyJava();
        }
    }

    protected abstract void applyJava();
    protected abstract void applyRenderScript();

    public Image getImageSrc(){
        return this.imageSrc;
    }

    public Image getImageOut(){
        return this.imageOut;
    }

    public void setImageSrc(Image source){
        this.imageSrc = source;
    }

    protected void showAlert(){
        new AlertDialog.Builder(this.main)
                .setTitle("Erreur de rendu")
                .setMessage("Le mode de rendu sélectionné n'est pas disponible pour se filtre")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }
}
