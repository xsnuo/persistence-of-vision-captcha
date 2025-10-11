package com.xuesinuo.xtool.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.xuesinuo.xtool.gif.AnimatedGifEncoder;

/**
 * gif验证码，常见情况下，2行代码可以生成视觉暂留验证码
 * <p>
 * gif captcha, in common cases, 2 lines of code can generate visual retention captcha
 * 
 * <pre>
 * // example:
 * BufferedImage image = GifCaptcha.drawOnWhite("TEST", 240, 80, -1, -1, -1, null, 40);
 * GifCaptcha.buildByImage(image, -1, -1, null, new FileOutputStream("mygif.gif"));
 * </pre>
 * 
 * @author xuesinuo
 * @version 1.0.0
 * @since 1.0.0
 */
public class GifCaptcha {

    /**
     * 将白色背景的图片转为视觉暂留图片
     * <p>
     * Convert white background images to visual retention images
     * 
     * @param image  白色背景图片
     *               <p>
     *               white background image
     * @param frames 帧数：默认与宽或高相同，小于等于1时使用默认值
     *               <p>
     *               frames: default is the same as the width or height, less than or equal to 1 use the default value
     * @param pixel  单个像素尺寸：默认1，小于等于0时使用默认值
     *               <p>
     *               single pixel size: default is 1, less than or equal to 0 use the default value
     * @param colors 颜色组合，至少2种颜色：默认{ Color.BLACK, Color.WHITE }
     *               <p>
     *               colors: at least 2 colors: default { Color.BLACK, Color.WHITE }
     * @param out    输出流
     *               <p>
     *               output stream
     */
    public static void buildByImage(BufferedImage image, int frames, int pixel, Color[] colors, OutputStream out) {
        int height = image.getHeight();
        int width = image.getWidth();
        if (frames <= 1) {
            frames = Math.min(height, width);
        }
        if (pixel <= 0) {
            pixel = 1;
        }
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(out);
        encoder.setDelay(50);
        encoder.setRepeat(0);
        if (colors == null || colors.length < 2) {
            colors = new Color[] { Color.BLACK, Color.WHITE };
        }
        int[][] pixels = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = (int) (Math.random() * colors.length);
            }
        }
        int[][] moves = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                moves[i][j] = (int) (Math.random() * colors.length);
            }
        }
        for (int i = 0; i < frames; i++) {
            BufferedImage frame = new BufferedImage(width * pixel, height * pixel, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = frame.getGraphics();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    try {
                        if (image.getRGB(x, y) != -1) {
                            graphics.setColor(colors[moves[(x + i) % width][(y) % height]]);
                        } else {
                            graphics.setColor(colors[pixels[(x) % width][(y + i) % height]]);
                        }
                        graphics.fillRect(pixel * x, pixel * y, 10, 10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            encoder.addFrame(frame);
        }
        encoder.finish();
    }

    /**
     * 在白色背景上绘制字符
     * <p>
     * Draw characters on white background
     * 
     * @param letters     要绘制的字母
     *                    <p>
     *                    letters to be drawn
     * @param imageWidth  图片宽度
     *                    <p>
     *                    image width
     * @param imageHeight 图片高度
     *                    <p>
     *                    image height
     * @param gap         字母间隙：默认12，小于0则使用默认值
     *                    <p>
     *                    letter gap: default 12, less than 0 use the default value
     * @param drift       字符偏移范围：默认18，小于0则使用默认值
     *                    <p>
     *                    character drift range: default 18, less than 0 use the default value
     * @param fontSize    字体大小：默认适应图片宽高，小于等于0则使用默认值
     *                    <p>
     *                    font size: default is adapted to the width and height of the picture, less than or equal to 0 use the default value
     * @param font        字体：默认 new Font("Arial", Font.BOLD, fontSize)
     *                    <p>
     *                    font: default new Font("Arial", Font.BOLD, fontSize)
     * @param noise       噪点：默认0
     *                    <p>
     *                    noise: default 0
     * 
     * @return BufferedImage 静态图片
     *         <p>
     *         BufferedImage static image
     */
    public static BufferedImage drawOnWhite(String letters, int imageWidth, int imageHeight, int gap, int drift, int fontSize, Font font, int noise) {
        if (gap < 0) {
            gap = 12;
        }
        if (drift < 0) {
            drift = 18;
        }
        if (fontSize <= 0) {
            fontSize = Math.min(imageHeight, imageWidth / letters.length());
        }

        BufferedImage image = new BufferedImage(
                imageWidth,
                imageHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);
        g2d.setColor(Color.BLACK);
        if (font == null) {
            font = new Font("Arial", Font.BOLD, fontSize);
        }
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        for (int i = 0; i < letters.length(); i++) {
            char letter = letters.charAt(i);
            int x = i * fontSize + (int) (Math.random() * gap);
            int y = ((imageHeight - metrics.getHeight()) / 2 + metrics.getAscent()) + (int) ((Math.random() - 0.5) * drift);
            g2d.drawString(String.valueOf(letter), x, y);
        }
        for (int i = 0; i < noise; i++) {
            int x = (int) (Math.random() * imageWidth);
            int y = (int) (Math.random() * imageHeight);
            int r = (int) (Math.random() * 4) + 1;
            g2d.fillRect(x, y, r, r);
        }
        g2d.dispose();
        return image;
    }

    // public static void main(String[] args) throws FileNotFoundException {
    //     BufferedImage image = drawOnWhite("TEST", 240, 80, -1, -1, -1, null, 40);
    //     File outputFile = new File("test.png");
    //     try {
    //         ImageIO.write(image, "png", outputFile);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     buildByImage(image, -1, -1, new Color[] { Color.GREEN, Color.RED }, new FileOutputStream("mygif.gif"));
    // }
}
