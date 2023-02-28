package io.github.dingdangdog.utils;

import io.github.dingdangdog.entity.FileInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    /**
     * 设置文字水印
     *
     * @param fileInfo  原有文件信息
     * @param waterMark 水印内容
     * @throws IOException 文件操作异常
     */
    public static void addWatermark(FileInfo fileInfo, String waterMark) throws IOException {
        // 拷贝原有文件
        MultipartFileUtils.backupFile(fileInfo);
        // 开始
        File file = new File(fileInfo.getFilePath(), fileInfo.getFileName());
        Image imageFile = ImageIO.read(file);
        //获取图片的宽
        int imageFileWidth = imageFile.getWidth(null);
        //获取图片的高
        int imageFileHeight = imageFile.getHeight(null);

        BufferedImage bufImg = new BufferedImage(imageFileWidth, imageFileHeight, BufferedImage.TYPE_INT_RGB);
        // 加水印
        // 创建画笔
        Graphics2D g = bufImg.createGraphics();
        // 绘制原始图片
        g.drawImage(imageFile, 0, 0, imageFileWidth, imageFileHeight, null);
        //-------------------------文字水印 start----------------------------
        // 根据图片的背景设置水印颜色
        g.setColor(new Color(255, 255, 255, 160));
        // 如果需要，请获取系统字体，防止无字体导致报错
//        Font font = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0];
        // 设置字体 加粗，文字大小为60pt
        g.setFont(new Font("微软雅黑", Font.BOLD, 12));
        // 水印内容
        String waterMarkContent = "@" + waterMark;
        // 设置水印的坐标(为原图片中间位置)
        int x = (imageFileWidth - getWatermarkLength(waterMarkContent, g)) / 2;
        int y = imageFileHeight / 2;
        // 画出水印 第一个参数是水印内容，第二个参数是x轴坐标，第三个参数是y轴坐标
        g.drawString(waterMarkContent, x, y);
        g.dispose();
        //-------------------------文字水印 end----------------------------
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ImageIO.write(bufImg, fileInfo.getFileType(), fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * 获取水印文字的长度
     *
     * @param waterMarkContent 水印内容
     * @param g                画笔
     */
    private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

}
