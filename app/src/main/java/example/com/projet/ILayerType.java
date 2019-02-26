package example.com.projet;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface ILayerType {
    abstract void setInflacter(final MainActivity main);

    abstract void generateLayer(final MainActivity main);

    abstract void applyLayer(final MainActivity main);
}
