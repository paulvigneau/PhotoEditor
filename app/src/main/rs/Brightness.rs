#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)

//static  const  float4  weight = {0.299f, 0.587f, 0.114f, 0.0f};

float brightness;

float c;
float x;
float m;

static float3 RGBToHSL(float4 color){
    float3 hsl;

    float r = color.r;
    float g = color.g;
    float b = color.b;

    float Cmax = fmax(r, fmax(g, b));
    float Cmin = fmin(r, fmin(g, b));

    float delta = Cmax - Cmin;

    //Define H
    if(delta == 0)
        hsl.x = 0;
    else if(Cmax == r)
        hsl.x = 60 * fmod((g-b)/delta, 6);
    else if(Cmax == g)
        hsl.x = 60 * ((b-r)/delta + 2);
    else if(Cmax == b)
        hsl.x = 60 * ((r-g)/delta + 4);

    //Define L
    hsl.z = (Cmax + Cmin)/2;

    //Define S
    if(Cmax == 0)
        hsl.y = 0;
    else
        hsl.y = delta / (1 - fabs(2 * hsl.z - 1));

    return hsl;
}

static float4 HSLToRGB(float3 hsl){
    float4 color;


    c = (1 - fabs(2 * hsl.z - 1)) * hsl.y;
    x = c * (1 - fabs(fmod(hsl.x / 60.0f, 2) - 1));
    m = hsl.z - c/2;

    int t = fmod((hsl.x / 60), 60);

    switch(t){
        case 0:
            color.r = c + m;
            color.g = x + m;
            color.b = m;
            break;
        case 1:
            color.r = x + m;
            color.g = c + m;
            color.b = m;
            break;
        case 2:
            color.r = m;
            color.g = c + m;
            color.b = x + m;
            break;
        case 3:
            color.r = m;
            color.g = x + m;
            color.b = c + m;
            break;
        case 4:
            color.r = x + m;
            color.g = m;
            color.b = c + m;
            break;
        case 5:
            color.r = c + m;
            color.g = m;
            color.b = x + m;
            break;
        default:
            break;
    }

    color.a = 1;
    return color;
}


uchar4  RS_KERNEL  Brightness(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);
    float3 hsl = RGBToHSL(pixelf);
    //hsl.z += brightness;

    pixelf = HSLToRGB(hsl);
    return  rsPackColorTo8888(pixelf.r, pixelf.g, pixelf.b, pixelf.a);
}