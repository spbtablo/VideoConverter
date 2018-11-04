package com.spbtablo;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainScreen extends JFrame {
    boolean IS_DEBUG = true;
    public SplitVideoSett splitSettScr;
    public SrcScreen srcScreen;
    public PreviewScreen prevScreen;
    public JPanel panel1;
    public JButton buttonFramePrev;
    public JButton buttonFrameNext;
    private JButton buttonOpen;
    private JSlider sliderDivLine;
    public JPanel panelImgUW;
    public JPanel panelImgSpeed;
    public JPanel panelImgAir;
    public JSlider sliderFrames;
    public JPanel panelSel;
    public JPanel panelImges;
    public JPanel panelNav;
    public JPanel panelDn;
    private JLabel labelFile;
    private JLabel labelSelTotal;
    private JLabel labelPath;
    private JButton buttonViewSplitSett;
    private JLabel labelFileCam1;
    private JLabel labelFileCam2;
    private JLabel labelFileCam3;
    private JLabel labelSplitPos;

    JFileChooser fileChooserAVI;
    File[] listAVIFiles;
    FileFilter extAVIFilter;
    private File currDir;
    private File selDir;
    public static final int CAMERA_UW_NUM = 0, CAMERA_AIR_NUM = 1, CAMERA_SPEED_NUM = 2;
    public int DEF_FRAME_HEIGHT = 576, DEF_FRAME_WIDTH = 960;
    private int frameCount = 0, framePos = -1, framesTotal = 0;
    public int frameHeight = DEF_FRAME_HEIGHT, frameWidth = DEF_FRAME_WIDTH, frameSplitPos = frameHeight / 2;
    Dimension dimensionsVideo[] = new Dimension[3];
    public float factorForAipImg = (float) 1.5; // выравниевание коэфф преломления воды и воздуха
    public File srcDir;
    public File tmpImgDir = null;
    File[] listFiles;
    File fileCam1, fileCam2, filCam3;
    VideoFileInfo videoFileInfo1, videoFileInfo2, videoFileInfo3;
    ArrayList<VideoFileInfo> listSelectedFileInfo = new ArrayList<>();
    ArrayList<VideoFileInfo> listCamUWVideoFileInfo = new ArrayList<>();
    ArrayList<VideoFileInfo> listCamAirVideoFileInfo = new ArrayList<>();
    ArrayList<VideoFileInfo> listSpeedFileInfo = new ArrayList<>();
    FFmpegFrameGrabber frameGrabberUW = null, frameGrabberAir = null, frameGrabberSpeed = null;
    Java2DFrameConverter frameConverter = new Java2DFrameConverter();
    BufferedImage bufferedImageUW = null, bufferedImageAir = null, bufferedImageSpeed = null;
    boolean isGrabberStart = false, isGrabberUWStart = false, isGrabberAirStart = false, isGrabberSpeedStart = false;
    public int heightUW, widthUW, heightAir, widthAir, previewSplitPos;
    float factorFrameSlider = 1f, previewScale = 1f;
    Image imagePreview, imageUW, imageAir, imageSpeed;
    int pixelsUW[];
    int pixelsAir[];
    int pixelsDest[];
    public JPanel panelImgPreview;


    public MainScreen() {
        currDir = new File(System.getProperty("user.dir"));
        extAVIFilter = new ExtFilter("avi");
        fileChooserAVI = new JFileChooser();
        fileChooserAVI.setCurrentDirectory(currDir);
        FileNameExtensionFilter filterAVI = new FileNameExtensionFilter("AVI file", "avi", "AVI");
        fileChooserAVI.setFileFilter(filterAVI);
        for (int i = 0; i < 3; i++) {
            dimensionsVideo[i] = new Dimension(DEF_FRAME_WIDTH, DEF_FRAME_HEIGHT);
        }
        splitSettScr = new SplitVideoSett();
        splitSettScr.$$$getRootComponent$$$().setVisible(true);
        srcScreen = new SrcScreen();
        prevScreen = new PreviewScreen();
        setNamesFromScreens();
        $$$setupUI$$$();
        panelImges.add(srcScreen.panelScreen, BorderLayout.NORTH);
        panelImges.add(splitSettScr.panelSplitSett, BorderLayout.CENTER);
        setContentPane(panel1);


        sliderDivLine.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                frameSplitPos = sliderDivLine.getValue();
                heightUW = frameSplitPos;
                heightAir = frameHeight - frameSplitPos;
                labelSplitPos.setText("Pos " + sliderDivLine.getValue());
            }
        });

        buttonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooserAVI.setDialogTitle("Выбор папки с AVI");
                fileChooserAVI.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooserAVI.setMultiSelectionEnabled(true);
                int result = fileChooserAVI.showOpenDialog(null);
                // Если файл выбран, то представим его в сообщении
                if (result == JFileChooser.APPROVE_OPTION) {
                    listAVIFiles = fileChooserAVI.getSelectedFiles();
                    fileChooserAVI.setCurrentDirectory(fileChooserAVI.getSelectedFile().getParentFile());
                    selDir = listAVIFiles[0].getParentFile();
                    loadingVideoFiles();
                }

            }
        });
        buttonFramePrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (framePos > 0) {
                    framePos--;
                    showSrcFrames();
                    sliderFrames.setValue((int) ((float) framePos / factorFrameSlider));
                }
            }
        });
        buttonFrameNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (framePos < frameCount - 1) {
                    framePos++;
                    showSrcFrames();
                }
            }
        });
        buttonViewSplitSett.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void setNamesFromScreens() {
        sliderDivLine = splitSettScr.sliderDivLine;
        panelImgUW = srcScreen.panelImgUW;
        panelImgSpeed = srcScreen.panelImgSpeed;
        panelImgAir = srcScreen.panelImgAir;
        panelImgPreview = prevScreen.panelImgPreview;
        panelImgPreview = splitSettScr.panelImgPreview;
        labelFileCam1 = srcScreen.labelFileCam1;
        labelFileCam2 = srcScreen.labelFileCam2;
        labelFileCam3 = srcScreen.labelFileCam3;

    }

    /* Загружаем выбранные файлы*/
    public void loadingVideoFiles() {
        labelPath.setText(listAVIFiles[0].getParent());
        String info = "Выбрано файлов : " + listAVIFiles.length + "\n";
        labelSelTotal.setText(info);
        if (listAVIFiles.length < 2) { // Один файл, ищем пару
            File file = listAVIFiles[0];
            String camMame = file.getName();
        }
        labelFile.setText(listAVIFiles[0].getName());
        tmpImgDir = createDir(currDir, "tmpImg");
        if (!tmpImgDir.exists()) {
            clearDir(tmpImgDir);
        }
        listFiles = listAVIFiles;
        getsFileInfoFromList(listFiles);
        labelFileCam1.setText(getFileNameFromList(listCamUWVideoFileInfo));
        labelFileCam2.setText(getFileNameFromList(listCamAirVideoFileInfo));
        labelFileCam3.setText(getFileNameFromList(listSpeedFileInfo));
        initVideoProperties();
        startFrameGrabbers();
        setNavButtonEnabled(true);
        showSrcFrames();
    }

    /* Готовим окна к выводу новых видеофайлов*/
    private void initVideoProperties() {
        framePos = 0;
        getFrameSizeFromList();
        /*TO DO  Потом уточнить, надо ли выбирать размер кадра или оставить по подводному видео*/
        frameHeight = dimensionsVideo[CAMERA_UW_NUM].height;
        frameWidth = dimensionsVideo[CAMERA_UW_NUM].width;
        frameSplitPos = frameHeight / 2;
        heightUW = frameSplitPos;
        heightAir = frameHeight - frameSplitPos;
        widthAir = dimensionsVideo[CAMERA_AIR_NUM].width;
        widthUW = frameWidth;
        previewScale = (float) panelImges.getHeight() / (float) frameHeight;
        previewSplitPos = (int) (previewScale * (float) frameSplitPos);
        pixelsDest = new int[frameWidth * frameHeight];
        pixelsUW = new int[dimensionsVideo[CAMERA_AIR_NUM].width * dimensionsVideo[CAMERA_AIR_NUM].height];
        pixelsAir = new int[dimensionsVideo[CAMERA_SPEED_NUM].height * dimensionsVideo[CAMERA_SPEED_NUM].height];
        sliderDivLine.setMaximum(frameHeight);
        sliderDivLine.setValue(frameSplitPos);
        factorFrameSlider = (float) framesTotal / (float) sliderFrames.getMaximum();
/*
        sliderFrames.setMinimum(0);
        sliderFrames.setMaximum(framesTotal / 100);
*/
        sliderFrames.setValue(0);
        imagePreview = createImage(frameWidth, frameHeight);
        imageUW = createImage(dimensionsVideo[CAMERA_UW_NUM].width, dimensionsVideo[CAMERA_UW_NUM].height);
        imageAir = createImage(dimensionsVideo[CAMERA_AIR_NUM].width, dimensionsVideo[CAMERA_AIR_NUM].height);
        imageSpeed = createImage(dimensionsVideo[CAMERA_SPEED_NUM].width, dimensionsVideo[CAMERA_SPEED_NUM].height);
    }

    /* Получаем размеры кадров у истчников видео*/
    private void getFrameSizeFromList() {
        getFrameSizeFromFile(listCamUWVideoFileInfo, dimensionsVideo[CAMERA_UW_NUM]);
    }

    private void getFrameSizeFromFile(ArrayList<VideoFileInfo> listFileInfo, Dimension dimension) {
        if (listFileInfo.size() > 0) {
            dimension.height = listFileInfo.get(0).height;
            dimension.width = listFileInfo.get(0).width;
        } else {
            dimension.height = DEF_FRAME_HEIGHT;
            dimension.width = DEF_FRAME_WIDTH;
        }
    }

    /* Показываем кадры во всех четырех окнах*/
    private void showSrcFrames() {
        imageUW = showNewFrame(frameGrabberUW, panelImgUW);
        imageAir = showNewFrame(frameGrabberAir, panelImgAir);
        imageSpeed = showNewFrame(frameGrabberSpeed, panelImgSpeed);
        showDestFrame();
    }

    /* Выводим итоговую картинку*/
    private void showDestFrame() {
        Graphics graphicsPrev = panelImges.getGraphics();
        graphicsPrev.clearRect(0, 0, frameWidth, frameHeight);
//        PixelGrabber pixelGrabberUW = new PixelGrabber(bufferedImageUW.getSource(), 0, 0, dimensionsVideo[CAMERA_UW_NUM].width, dimensionsVideo[CAMERA_UW_NUM].height, 0, dimensionsVideo[CAMERA_UW_NUM].width);
        CropImageFilter cropImageFilterUW = new CropImageFilter(0, 0, frameWidth, heightUW);
        CropImageFilter cropImageFilterAir = new CropImageFilter(0, frameSplitPos, frameWidth, heightAir);
        FilteredImageSource filteredImageSourceUW = new FilteredImageSource(imageUW.getSource(), cropImageFilterUW);
        FilteredImageSource filteredImageSourceAir = new FilteredImageSource(imageAir.getSource(), cropImageFilterAir);
        BufferedImage bufferedImageUp = grabFrameFromFile(frameGrabberAir, framePos).getSubimage(0, 0, widthAir, heightAir); // TO DO потом увеличть картинку
        BufferedImage bufferedImageDn = grabFrameFromFile(frameGrabberUW, framePos).getSubimage(0, frameSplitPos, widthUW, heightUW);
//        bufferedImageDn = imageUW.getGraphics().g
//        graphicsPrev.drawImage(bufferedImageUp, 0, 0, frameWidth, heightAir, panelImges);
/*
        graphicsPrev.drawImage(bufferedImageUp, 0, 0,  panelImges.getWidth(), previewSplitPos, panelImges);
        graphicsPrev.drawImage(bufferedImageDn, 0, previewSplitPos, panelImges.getWidth(), (panelImges.getHeight() - previewSplitPos), panelImges);
*/
        graphicsPrev.drawImage(createImage(filteredImageSourceAir), 0, 0, panelImges.getWidth(), previewSplitPos, panelImges);
        graphicsPrev.drawImage(createImage(filteredImageSourceUW), 0, previewSplitPos, panelImges.getWidth(), (panelImges.getHeight() - previewSplitPos), panelImges);
        panelImgSpeed.getGraphics().drawImage(createImage(filteredImageSourceAir), 0, 0, panelImgSpeed.getWidth(), panelImgSpeed.getHeight(), panelImgSpeed);
    }

    private Image showNewFrame(FFmpegFrameGrabber frameGrabber, JPanel panelImg) {
        Image image;
        if (frameGrabber == null) {
            return null;
        }
        BufferedImage bufferedImage = grabFrameFromFile(frameGrabber, framePos);
        if (bufferedImage != null) {
            image = createImage(bufferedImage.getSource());
//            panelImges.getGraphics().drawImage(bufferedImage, 0, 0, panelImges.getWidth(), panelImges.getHeight(), panelImges);
            panelImg.getGraphics().drawImage(image, 0, 0, panelImg.getWidth(), panelImg.getHeight(), panelImg);
        } else {
            panelImg.getGraphics().clearRect(0, 0, panelImg.getWidth(), panelImg.getHeight());
            image = null;
        }
        return image;
    }

    private BufferedImage grabFrameFromFile(FFmpegFrameGrabber frameGrabber, int framePos) {
        if (framePos < frameGrabber.getLengthInFrames()) {
            try {
                frameGrabber.setFrameNumber(framePos);
                return frameConverter.convert(frameGrabber.grabImage());
            } catch (FrameGrabber.Exception e) {
            }
        }
        return null;
    }

    /* Запуск грабберов для конвертации картинок*/
    private void startFrameGrabbers() {
        if (listCamUWVideoFileInfo.size() > 0) {
            frameGrabberUW = startFrameGrabber(listCamUWVideoFileInfo.get(0));
        }
        if (listCamAirVideoFileInfo.size() > 0) {
            frameGrabberAir = startFrameGrabber(listCamAirVideoFileInfo.get(0));
        }
        if (listSpeedFileInfo.size() > 0) {
            frameGrabberSpeed = startFrameGrabber(listSpeedFileInfo.get(0));
        }
    }

    private FFmpegFrameGrabber startFrameGrabber(VideoFileInfo listVideoFileInfo) {
        FFmpegFrameGrabber frameGrabber = listVideoFileInfo.frameGrabber;
        if (frameGrabber != null) {
            try {
                frameGrabber.start();
                frameGrabber.setFrameNumber(framePos);
                return frameGrabber;
            } catch (FrameGrabber.Exception e) {
            }
        }
        return null;
    }

    private void releaseFrameGrabber(FFmpegFrameGrabber frameGrabber) {
        if (frameGrabber != null) {
            try {
                frameGrabber.stop();
                frameGrabber.release();
                frameGrabber = null;
            } catch (FrameGrabber.Exception e) {
            }
        }
    }

    private String getFileNameFromList(ArrayList<VideoFileInfo> videoFileInfos) {
        if (videoFileInfos.size() > 0) {
            return ("Файл : " + videoFileInfos.get(0).name.getName());
        } else {
            return ("Файл не найден");
        }
    }

    private void setNavButtonEnabled(boolean b) {
        buttonFrameNext.setEnabled(b);
        buttonFramePrev.setEnabled(b);
        sliderFrames.setEnabled(b);
    }

    private boolean checkFrameGrabberStatus(FFmpegFrameGrabber frameGrabber) {
        if (frameGrabber == null) {
            return false;
        } else {
            return isGrabberStart;
        }
    }

    private void stopFrameGrabber(FFmpegFrameGrabber frameGrabber) {
        try {
            frameGrabber.stop();
            isGrabberStart = false;
        } catch (FrameGrabber.Exception e) {
        }

    }

    // Препарируем имя файла на части - время и т п
    private void getsFileInfoFromList(File[] listFiles) {
        //WND00_20181026105548_20181026110405_PART0.avi
        listSelectedFileInfo.clear();
        listCamUWVideoFileInfo.clear();
        listCamAirVideoFileInfo.clear();
        listSpeedFileInfo.clear();
        for (File file : listFiles) {
            listSelectedFileInfo.add(new VideoFileInfo(file));
        }
        for (VideoFileInfo vfi : listSelectedFileInfo) {
            switch (vfi.camNum) {
                case CAMERA_UW_NUM:
                    listCamUWVideoFileInfo.add(vfi);
                    break;
                case CAMERA_AIR_NUM:
                    listCamAirVideoFileInfo.add(vfi);
                    break;
                case CAMERA_SPEED_NUM:
                    listSpeedFileInfo.add(vfi);
                    break;
                default:
                    break;
            }
            if (vfi.frames > 0) {
                frameCount = max(framesTotal, vfi.frames);
            }
        }
    }

    public File createDir(File currDir, String dir) {
        File newDir = new File(currDir.getPath() + "//" + dir);
        if (!newDir.exists()) {
            newDir.mkdir();
        }
        return newDir;
    }

    public void clearDir(File srcDir) {
        int n = 0;
        if (srcDir == null) {
            return;
        }
        if (!srcDir.isDirectory()) {
            return;
        }
        if (srcDir.listFiles() == null) {
            return;
        }
        for (File file : Objects.requireNonNull(srcDir.listFiles())) {
            n++;
            if (file.exists()) {
                file.delete();
            }
        }
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        sliderDivLine.setValue(frameSplitPos);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setMinimumSize(new Dimension(1150, 700));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), ""));
        panelImges = new JPanel();
        panelImges.setLayout(new BorderLayout(0, 0));
        panelImges.setBackground(new Color(-1));
        panelImges.setMinimumSize(new Dimension(990, 670));
        panelImges.setPreferredSize(new Dimension(990, 670));
        panel1.add(panelImges, BorderLayout.CENTER);
        panelDn = new JPanel();
        panelDn.setLayout(new GridLayoutManager(1, 4, new Insets(3, 3, 3, 3), -1, -1));
        panel1.add(panelDn, BorderLayout.SOUTH);
        panelNav = new JPanel();
        panelNav.setLayout(new GridBagLayout());
        panelDn.add(panelNav, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        sliderFrames = new JSlider();
        sliderFrames.setEnabled(true);
        sliderFrames.setInverted(false);
        sliderFrames.setMajorTickSpacing(10);
        sliderFrames.setMinorTickSpacing(1);
        sliderFrames.setValue(0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelNav.add(sliderFrames, gbc);
        buttonFramePrev = new JButton();
        buttonFramePrev.setEnabled(false);
        buttonFramePrev.setText("<");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelNav.add(buttonFramePrev, gbc);
        buttonFrameNext = new JButton();
        buttonFrameNext.setEnabled(false);
        buttonFrameNext.setText(">");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        panelNav.add(buttonFrameNext, gbc);
        buttonViewSplitSett = new JButton();
        buttonViewSplitSett.setText("Настроить картинку");
        buttonViewSplitSett.setToolTipText("Натроть раслоложение видео с камер экране");
        panelDn.add(buttonViewSplitSett, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelDn.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelDn.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        panelSel = new JPanel();
        panelSel.setLayout(new GridLayoutManager(1, 4, new Insets(2, 2, 2, 2), -1, -1));
        panelSel.setMinimumSize(new Dimension(500, 47));
        panel1.add(panelSel, BorderLayout.NORTH);
        panelSel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Выбор папки/файла"));
        buttonOpen = new JButton();
        buttonOpen.setText("Выбрать");
        panelSel.add(buttonOpen, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelFile = new JLabel();
        labelFile.setMaximumSize(new Dimension(-1, -1));
        labelFile.setMinimumSize(new Dimension(100, 16));
        labelFile.setText("Папка / Файл ");
        panelSel.add(labelFile, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelSelTotal = new JLabel();
        labelSelTotal.setText("Label");
        panelSel.add(labelSelTotal, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelPath = new JLabel();
        labelPath.setText("Label");
        panelSel.add(labelPath, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
