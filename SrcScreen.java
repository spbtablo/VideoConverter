package com.spbtablo;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SrcScreen {
    public int CAMERA_UW_NUM = 0, CAMERA_AIR_NUM = 1, CAMERA_SPEED_NUM = 2;
    public int DEF_FRAME_HEIGHT = 576, DEF_FRAME_WIDTH = 960;
    private int frameCount = 0, framePos = -1, framesTotal = 0;
    public int frameHeight = DEF_FRAME_HEIGHT, frameWidth = DEF_FRAME_WIDTH, frameSplitPos = frameHeight / 2;
    Dimension dimensionsVideo[] = new Dimension[3];
    public float factorForAipImg = (float) 1.5; // выравниевание коэфф преломления воды и воздуха
    BufferedImage bufferedImageUW = null, bufferedImageAir = null, bufferedImageSpeed = null;
    boolean isGrabberStart = false, isGrabberUWStart = false, isGrabberAirStart = false, isGrabberSpeedStart = false;
    public int heightUW, widthUW, heightAir, widthAir, previewSplitPos;
    float factorFrameSlider = 1f, previewScale = 1f;
    Image imagePreview, imageUW, imageAir, imageSpeed;
    int pixelsUW[];
    int pixelsAir[];
    int pixelsDest[];

    public JPanel panelScreen;
    public JPanel panelUp;
    public JPanel panelDn;
    public JPanel panelCenter;
    public JPanel panelImgUW;
    public JPanel panelImgAir;
    public JPanel panelImgSpeed;
    public JLabel labelFileCam1;
    public JLabel labelFileCam2;
    public JLabel labelFileCam3;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelScreen = new JPanel();
        panelScreen.setLayout(new BorderLayout(0, 0));
        panelUp = new JPanel();
        panelUp.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1, true, true));
        panelScreen.add(panelUp, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        label1.setText("Подв. камера (CAM1)");
        panelUp.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Надв. камера(CAM 2)");
        panelUp.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label3 = new JLabel();
        label3.setText("Спидограмма (CAM 3)");
        panelUp.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelFileCam1 = new JLabel();
        labelFileCam1.setText("Файл :");
        panelUp.add(labelFileCam1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelFileCam2 = new JLabel();
        labelFileCam2.setText("Файл :");
        panelUp.add(labelFileCam2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelFileCam3 = new JLabel();
        labelFileCam3.setText("Файл :");
        panelUp.add(labelFileCam3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelDn = new JPanel();
        panelDn.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelScreen.add(panelDn, BorderLayout.SOUTH);
        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, true, true));
        panelCenter.setMaximumSize(new Dimension(978, 184));
        panelCenter.setMinimumSize(new Dimension(978, 184));
        panelCenter.setPreferredSize(new Dimension(978, 184));
        panelScreen.add(panelCenter, BorderLayout.CENTER);
        panelImgUW = new JPanel();
        panelImgUW.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelImgUW.setBackground(new Color(-1));
        panelCenter.add(panelImgUW, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(320, 180), new Dimension(320, 180), new Dimension(320, 180), 0, false));
        panelImgAir = new JPanel();
        panelImgAir.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelImgAir.setBackground(new Color(-1));
        panelCenter.add(panelImgAir, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(320, 180), new Dimension(320, 180), new Dimension(320, 180), 0, false));
        panelImgSpeed = new JPanel();
        panelImgSpeed.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelImgSpeed.setBackground(new Color(-1));
        panelCenter.add(panelImgSpeed, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(320, 180), new Dimension(320, 180), new Dimension(320, 180), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelScreen;
    }
}
