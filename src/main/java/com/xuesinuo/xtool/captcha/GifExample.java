package com.xuesinuo.xtool.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xuesinuo.xtool.gif.AnimatedGifEncoder;

public class GifExample {
    public static void main(String[] args) throws IOException {
        int frames = 120;
        int pixel = 1;
        int height = 120;
        int width = 360;
        BufferedImage image = drawLetterOnWhiteImage("HELO", width, height);
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(new FileOutputStream("mygif.gif"));
        encoder.setDelay(50);
        encoder.setRepeat(0);
        Color[] colors = { Color.BLACK, Color.WHITE };
        int[][] pixels = new int[width][height];
        for (int i = 0; i < frames; i++) {
            for (int j = 0; j < frames; j++) {
                pixels[i][j] = Math.random() < 0.5 ? 0 : 1;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = pixels[i % frames][j % frames];
            }
        }
        int[][] moves = new int[width][height];
        for (int i = 0; i < frames; i++) {
            for (int j = 0; j < frames; j++) {
                moves[i][j] = Math.random() < 0.5 ? 0 : 1;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                moves[i][j] = moves[i % frames][j % frames];
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
     * 在白色图片上绘制字母
     * 
     * @param letters     要绘制的字母
     * @param imageWidth  图片宽度
     * @param imageHeight 图片高度
     */
    public static BufferedImage drawLetterOnWhiteImage(String letters, int imageWidth, int imageHeight) {
        BufferedImage image = new BufferedImage(
                imageWidth,
                imageHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);
        g2d.setColor(Color.BLACK);
        int fontSize = Math.min(imageHeight, imageWidth / letters.length());
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        for (int i = 0; i < letters.length(); i++) {
            char letter = letters.charAt(i);
            int x = i * fontSize + (int) (Math.random() * 12);
            int y = ((imageHeight - metrics.getHeight()) / 2 + metrics.getAscent()) + (int) ((Math.random() - 0.5) * 18);
            g2d.drawString(String.valueOf(letter), x, y);
        }
        g2d.dispose();
        File outputFile = new File("hello.png");
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return image;
    }
}
