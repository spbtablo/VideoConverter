package com.spbtablo;

import javax.swing.*;
import java.io.File;

public class Main {
    private static final boolean IS_DEBUG = true;
    public static MainScreen mainScreen;
    public static SplitVideoSett splitSettScr;
    public static SrcScreen srcScreen;
    public static PreviewScreen prevScreen;

    public static void main(String[] args) {
        File currDir = new File(System.getProperty("user.dir"));
        mainScreen = new MainScreen();
        splitSettScr = new SplitVideoSett();
        srcScreen = new SrcScreen();
        prevScreen = new PreviewScreen();
        mainScreen.IS_DEBUG = IS_DEBUG;
        mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/*
        mainScreen.splitSettScr = splitSettScr;
        mainScreen.srcScreen = srcScreen;
        mainScreen.prevScreen = prevScreen;
//        mainScreen.panelImges.add(splitSettScr.$$$getRootComponent$$$());
        mainScreen.panelImges.add(splitSettScr.panelSplitSett);
*/

        mainScreen.setContentPane(mainScreen.$$$getRootComponent$$$());
        mainScreen.pack();
        mainScreen.setSize(1200 , 720);
        mainScreen.setLocation(20, 5);
        mainScreen.setVisible(true);
        mainScreen.repaint();
        if (IS_DEBUG) {
            // D:\Плавание\Soft\Video\Converter\WND00_20181026105548_20181026110405_PART0.avi
            /*D:\Плавание\Soft\Video\Converter\WND01_20181026105548_20181026110405_PART0.avi
             *D:\Плавание\Soft\Video\Converter\WND00_20181026105548_20181026110405_PART0.avi */
            mainScreen.listAVIFiles = new File[2];
            mainScreen.listAVIFiles[0] = new File("D:\\Плавание\\Soft\\Video\\Converter\\WND00_20181026105548_20181026110405_PART0.avi");
            mainScreen.listAVIFiles[1] = new File(currDir, "WND01_20181026105548_20181026110405_PART0.avi");
            mainScreen.loadingVideoFiles();
        }
    }

}
