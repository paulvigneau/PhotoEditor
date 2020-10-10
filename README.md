# PhotoEditor

Android application that embeds several algorithms to apply filters on your pictures.

## Description

This simple Android application propose to choose a photo from the  smartphone's image gallery and to modify it with a lot of filters.

This app was a University project in order to discover the Android development (in Java) and the image processing.

The main goal was to develop powerful algorithms. For that reason, some of the filters are not only in Java but in RenderScript too. The application let you choose the mode for each filter in order to compare time processing.

## Features

Here is the list of all the filters the application has :

- Brightness
- Saturation
- Contrast
- Equalize
- Blur (Averaging, Gaussian, Sharpen)
- Contour - with convolution matrices (Sobel, Prewitt, Laplacien, Emboss)
- Colorize
- One color and the rest in grayscale
- Replace a color
- Grayscale
- Sepia
- Invert colors
- Sketch
- Cartoon
- Pixelated
- Mirror (horizontal/vertical)
- Face detector (with google library)

Each filter can be adapted with parameters. For example, the size of a pixel for the pixel filter or the threshold for the color replacement.
