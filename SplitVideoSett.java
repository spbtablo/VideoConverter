package com.spbtablo;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SplitVideoSett {
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

    public JPanel panelSplitSett;
    public JPanel panelImgPreview;
    private JPanel panelUp;
    private JPanel panelLf;
    private JPanel panelRt;
    private JPanel panelDn;
    public JSlider sliderDivLine;
    public JScrollBar scrollBarUp;
    public JScrollBar scrollBarDn;
    private JPanel panelLables;
    private JButton buttonChangeSett;
    private JPanel panelButtons;
    private JButton buttonSave;
    private JButton buttonCancel;
    public JLabel labelSplitPos;
    public JLabel labelScrollPos;

    public SplitVideoSett() {
        sliderDivLine.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                frameSplitPos = sliderDivLine.getValue();
                heightUW = frameSplitPos;
                heightAir = frameHeight - frameSplitPos;
                labelSplitPos.setText("Pos " + sliderDivLine.getValue());
            }
        });
        buttonChangeSett.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        buttonChangeSett.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        sliderDivLine.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });
    }

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
        panelSplitSett = new JPanel();
        panelSplitSett.setLayout(new BorderLayout(0, 0));
        panelSplitSett.setMaximumSize(new Dimension(923, 470));
        panelUp = new JPanel();
        panelUp.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelSplitSett.add(panelUp, BorderLayout.NORTH);
        panelLables = new JPanel();
        panelLables.setLayout(new GridLayoutManager(2, 2, new Insets(0, 10, 0, 10), -1, -1));
        panelUp.add(panelLables, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Сдвиг картинок");
        panelLables.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setAlignmentX(0.5f);
        label2.setHorizontalAlignment(11);
        label2.setHorizontalTextPosition(0);
        label2.setText("Линия раздела");
        panelLables.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelScrollPos = new JLabel();
        labelScrollPos.setText("50 , 50");
        panelLables.add(labelScrollPos, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelSplitPos = new JLabel();
        labelSplitPos.setText("50");
        panelLables.add(labelSplitPos, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelUp.add(panelButtons, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonChangeSett = new JButton();
        buttonChangeSett.setText("Изменить настройки");
        panelButtons.add(buttonChangeSett, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setIcon(new ImageIcon(getClass().getResource("/com/sun/deploy/resources/image/close_box_normal.png")));
        buttonCancel.setText("");
        buttonCancel.setToolTipText("Отменить");
        panelButtons.add(buttonCancel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(24, 24), new Dimension(24, 24), new Dimension(24, 24), 4, false));
        buttonSave = new JButton();
        buttonSave.setText("Сохранить");
        panelButtons.add(buttonSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelButtons.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(557, 11), null, 0, false));
        panelLf = new JPanel();
        panelLf.setLayout(new GridLayoutManager(2, 1, new Insets(0, 5, 0, 0), -1, -1));
        panelLf.setMinimumSize(new Dimension(50, 360));
        panelLf.setPreferredSize(new Dimension(60, 360));
        panelSplitSett.add(panelLf, BorderLayout.WEST);
        scrollBarUp = new JScrollBar();
        scrollBarUp.setValue(35);
        scrollBarUp.setValueIsAdjusting(false);
        scrollBarUp.setVisibleAmount(30);
        panelLf.add(scrollBarUp, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 2, false));
        scrollBarDn = new JScrollBar();
        scrollBarDn.setValue(35);
        scrollBarDn.setVisibleAmount(30);
        panelLf.add(scrollBarDn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 2, false));
        panelRt = new JPanel();
        panelRt.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 5), -1, -1));
        panelRt.setMinimumSize(new Dimension(30, 360));
        panelRt.setPreferredSize(new Dimension(60, 360));
        panelSplitSett.add(panelRt, BorderLayout.EAST);
        sliderDivLine = new JSlider();
        sliderDivLine.setEnabled(true);
        sliderDivLine.setMajorTickSpacing(50);
        sliderDivLine.setOrientation(1);
        sliderDivLine.setPaintLabels(true);
        sliderDivLine.setPaintTicks(true);
        panelRt.add(sliderDivLine, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 360), new Dimension(-1, 360), null, 0, false));
        panelDn = new JPanel();
        panelDn.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSplitSett.add(panelDn, BorderLayout.SOUTH);
        panelImgPreview = new JPanel();
        panelImgPreview.setLayout(new GridLayoutManager(1, 1, new Insets(5, 0, 5, 0), 1, 1));
        panelImgPreview.setBackground(new Color(-1));
        panelImgPreview.setEnabled(false);
        Font panelImgPreviewFont = this.$$$getFont$$$(null, Font.BOLD, -1, panelImgPreview.getFont());
        if (panelImgPreviewFont != null) panelImgPreview.setFont(panelImgPreviewFont);
        panelImgPreview.setMaximumSize(new Dimension(640, 360));
        panelImgPreview.setMinimumSize(new Dimension(640, 360));
        panelImgPreview.setPreferredSize(new Dimension(640, 360));
        panelSplitSett.add(panelImgPreview, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelSplitSett;
    }
}