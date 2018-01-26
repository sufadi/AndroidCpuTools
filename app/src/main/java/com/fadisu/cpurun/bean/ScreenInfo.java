package com.fadisu.cpurun.bean;


public class ScreenInfo {

    // 英寸
    public double size;

    public String sizeStr;

    // 高
    public int heightPixels;

    public String screenRealMetrics;

    // 宽
    public int widthPixels;

    // 显示器的逻辑密度
    public float density;

    // 屏幕密度为点每英寸
    public int densityDpi;

    public String densityDpiStr;

    // 显示在显示器的字体的定标因子
    public float scaledDensity;

    // 每在 X 维屏幕英寸的确切物理像素
    public float xdpi;

    // 每在Y维屏幕英寸的确切物理像素
    public float ydpi;

    //在屏幕中显示的参考密度
    public int density_default;

    @Override
    public String toString() {
        return "ScreenInfo{" +
                "size=" + size +
                ", sizeStr='" + sizeStr + '\'' +
                ", heightPixels=" + heightPixels +
                ", screenRealMetrics='" + screenRealMetrics + '\'' +
                ", widthPixels=" + widthPixels +
                ", density=" + density +
                ", densityDpi=" + densityDpi +
                ", densityDpiStr='" + densityDpiStr + '\'' +
                ", scaledDensity=" + scaledDensity +
                ", xdpi=" + xdpi +
                ", ydpi=" + ydpi +
                ", density_default=" + density_default +
                '}';
    }
}
