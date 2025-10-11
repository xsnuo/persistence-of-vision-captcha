# persistence-of-vision-captcha

使用视觉暂留原理，生成动图验证码。图片任何一帧都是乱码，只有连续播放人眼才能看出到验证码。以此方式可以比传统图片验证码更抗OCR识别，同时相比行文验证成本更低。

注意，图片放大到一定程度，字符可能才会清晰可见。使用验证码时，使用较大的验证码图片，或提供放大功能，可以有效提高使用体验。

## Maven引入

```xml
<dependency>
    <groupId>com.xuesinuo</groupId>
    <artifactId>persistence-of-vision-captcha</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 效果图

带宽原因，效果图可能加载较慢。请等待一下，动图流畅后，方可看到效果，倍率放大后，字符更清晰可见。

![captcha](https://www.xuesinuo.com/img/persistence-of-vision-captcha.gif)

github在线预览效果图可能被压缩，原图请看：<https://www.xuesinuo.com/img/persistence-of-vision-captcha.gif>

## 使用示例

最基本的使用案例，很简单：

```java
// 创建一张白底的静态验证码图：设置文字内容、图片尺寸、字体、噪点等信息
BufferedImage image = GifCaptcha.drawOnWhite("TEST", 240, 80, -1, -1, -1, null, 40);
// 将白底静态验证码图转为视觉暂留动图：设置帧数、颜色、像素合并等信息，输出到OutputStream
GifCaptcha.buildByImage(image, -1, -1, null, new FileOutputStream("mygif.gif"));
```
