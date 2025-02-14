package com.deepscience.rpa;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.deepscience.rpa.util.ImageUtils;
import com.deepscience.rpa.util.OCRUtils;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.sikuli.basics.Debug;
import org.sikuli.basics.Settings;
import org.sikuli.script.OCR;
import org.sikuli.script.SikuliXception;
import org.sikuli.script.support.RunTime;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class OCRTest {

    static {
        RunTime.loadLibrary(RunTime.libOpenCV);
    }

    @Test
    public void ocrTest() {
        System.out.println(Paths.get(System.getProperty("user.dir")));
        BufferedImage image = ImgUtil.read("C:\\Users\\yang\\autoLive\\2025-02-12\\1739337598919.png");
        BufferedImage bufferedImage = ImageUtils.resizeImage(image, 1.5f);
        String liveId = OCRUtils.readNumberText(bufferedImage);
        System.out.println(liveId);
    }

    @Test
    public void liveIdTest() throws TesseractException {
        OCR.Options options = OCR.globalOptions();
        // options.language("chi_sim");
        options.dataPath(FileUtil.getAbsolutePath("tessdata"));
        // options.variable("tessedit_char_whitelist", "0123456789");
        ITesseract tesseractAPI = getTesseractAPI(options);

        // String s = OCR.readLine("D:\\zksz\\autoLive\\logs\\test.png");
        // String s = tesseractAPI.doOCR(new File(FileUtil.getAbsolutePath("images/test/closeButton_02.png")));
        String s = tesseractAPI.doOCR(new File("D:\\zksz\\autoLive\\logs\\test.png"));
        System.out.println(s);
        // s = OCR.readLine(new File(FileUtil.getAbsolutePath("images/test/closeButton_02.png")));
        s = OCR.readLine("D:\\zksz\\autoLive\\logs\\test.png");
        System.out.println(s);
    }

    private ITesseract getTesseractAPI(OCR.Options options) {
        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setOcrEngineMode(options.oem());
            tesseract.setPageSegMode(options.psm());
            tesseract.setLanguage(options.language());
            tesseract.setDatapath(defaultDataPath());
            for (Map.Entry<String, String> entry : options.variables().entrySet()) {
                tesseract.setTessVariable(entry.getKey(), entry.getValue());
            }
            if (!options.configs().isEmpty()) {
                tesseract.setConfigs(new ArrayList<>(options.configs()));
            }
            return tesseract;
        } catch (UnsatisfiedLinkError e) {
            String helpURL;
            if (RunTime.get().runningWindows) {
                helpURL = "https://github.com/RaiMan/SikuliX1/wiki/Windows:-Problems-with-libraries-OpenCV-or-Tesseract";
            } else {
                helpURL = "https://github.com/RaiMan/SikuliX1/wiki/macOS-Linux:-Support-libraries-for-Tess4J-Tesseract-4-OCR";
            }
            Debug.error("see: " + helpURL);
            if (RunTime.isIDE()) {
                Debug.error("Save your work, correct the problem and restart the IDE!");
                try {
                    Desktop.getDesktop().browse(new URI(helpURL));
                } catch (IOException ex) {
                } catch (URISyntaxException ex) {
                }
            }
            throw new SikuliXception(String.format("OCR: start: Tesseract library problems: %s", e.getMessage()));
        }
    }

    private static String defaultDataPath() {
        // export SikuliX eng.traineddata, if libs are exported as well
        File fTessDataPath = new File(RunTime.get().fSikulixAppPath, "SikulixTesseract/tessdata");
        boolean shouldExport = RunTime.get().shouldExport();
        boolean fExists = fTessDataPath.exists();
        if (!fExists || shouldExport) {
            if (0 == RunTime.get().extractResourcesToFolder("/tessdataSX", fTessDataPath, null).size()) {
                throw new SikuliXception(String.format("OCR: start: export tessdata did not work: %s", fTessDataPath));
            }
        }
        // if set, try with provided tessdata parent folder
        String defaultDataPath;
        if (Settings.OcrDataPath != null) {
            defaultDataPath = new File(Settings.OcrDataPath, "tessdata").getAbsolutePath();
        } else {
            defaultDataPath = fTessDataPath.getAbsolutePath();
        }
        return defaultDataPath;
    }

    // public static BufferedImage convertTo24Bit(BufferedImage src) {
    //     Imgcodecs.imread
    //     // 如果原图是32位色（包含透明度通道），去掉透明度通道，转换为RGB 24位色
    //     if (src.channels() == 4) {
    //         // 将 32 位图像转换为 24 位图像
    //         Mat rgb = new Mat();
    //         Imgproc.cvtColor(src, rgb, Imgproc.COLOR_BGRA2BGR);
    //         return rgb;
    //     }
    //     return src;
    // }

    public static Mat bufferedImageToMat(BufferedImage bufferedImage) {
        // 获取图像的宽度和高度
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // 创建一个与 BufferedImage 同样大小的 Mat 对象
        Mat mat = new Mat(height, width, CvType.CV_8UC3); // 8-bit, 3 channels (RGB)

        // 获取 BufferedImage 的像素数据
        byte[] data = ((java.awt.image.DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

        // 将数据拷贝到 Mat 中
        mat.put(0, 0, data);

        // 由于 OpenCV 使用的是 BGR 格式，而 BufferedImage 使用的是 RGB 格式，需要转换通道顺序
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR);

        return mat;
    }
}
