package example.com.projet;

public interface ILayerType {
    /**
     * Create the layer view
     *
     * @param main
     *      The main activity
     */
    void setInflacter(final MainActivity main);

    /**
     * Create the layer filter
     *
     * @param main
     *      The main activity
     */
    void generateLayer(final MainActivity main);

    /**
     * Apply the filter
     *
     * @param main
     *      The main activity
     * @param inRenderScript
     *      TRUE if apply in RenderScript
     */
    void applyLayer(final MainActivity main, boolean inRenderScript);
}
