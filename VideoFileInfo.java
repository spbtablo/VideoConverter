package com.spbtablo;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;

import java.io.File;
import java.util.StringTokenizer;

public class VideoFileInfo {
    //WND00_20181026105548_20181026110405_PART0.avi
    static final String CAM_PREF = "WND";
    static final String PART_PREF = "PART";
    public File name = null;
    public String type = "";
    public int camNum = 0;
    public int partNum = 0;
    public int year = 0, month = 0, day = 0;
    public int hourS = 0, minS = 0, secS = 0;
    public int hourE = 0, minE = 0, secE = 0;
    public String partStr = "";
    public String timeStart = "", timeEnd = "";
    public int width = 0;
    public int height = 0;
    public int frames= 0;
    public long lengthInTime = 0;
    public FFmpegFrameGrabber frameGrabber = null;

    VideoFileInfo(File file){
        this.name = file;
        String nameStr = name.getName();
        String camStr;
        StringTokenizer st = new StringTokenizer(nameStr, ".");
        int iE = nameStr.lastIndexOf('.');
        if ((iE > 0) && (iE < nameStr.length()-1)) {
            type = nameStr.substring(iE + 1).toLowerCase();
        }
        st = new StringTokenizer(st.nextToken(), "_");
        if (st.countTokens() < 1){
            return;
        }
        try {
            if (st.hasMoreTokens()){
                camStr = st.nextToken();
                if (camStr.length() > 4 && nameStr.contains(CAM_PREF)){
                    camNum = Integer.parseInt(camStr.substring(CAM_PREF.length()));
                }
            }
            if (st.hasMoreTokens()){
                //WND00_20181026105548_20181026110405_PART0.avi
                timeStart = st.nextToken();
                year = Integer.parseInt(timeStart.substring(0, 4));
                month = Integer.parseInt(timeStart.substring(4, 6));
                day = Integer.parseInt(timeStart.substring(6, 8));
                hourS = Integer.parseInt(timeStart.substring(8, 10));
                minS = Integer.parseInt(timeStart.substring(10, 12));
                secS = Integer.parseInt(timeStart.substring(12));
            }
            if (st.hasMoreTokens()){
                timeEnd = st.nextToken();
                hourE = Integer.parseInt(timeEnd.substring(8, 10));
                minE = Integer.parseInt(timeEnd.substring(10, 12));
                secE = Integer.parseInt(timeEnd.substring(12));
            }
            if (st.hasMoreTokens() && nameStr.contains(PART_PREF)){
                String partStr = st.nextToken();
                partNum = Integer.parseInt(partStr.substring(PART_PREF.length()));
            }
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
            frameGrabber.start();
            width = frameGrabber.getImageWidth();
            height = frameGrabber.getImageHeight();
            frames = frameGrabber.getLengthInFrames();
            lengthInTime = frameGrabber.getLengthInTime();
            this.frameGrabber = frameGrabber;
            frameGrabber.stop();
        } catch (FrameGrabber.Exception e) {
        } catch (IndexOutOfBoundsException e){
        } catch (NumberFormatException e){
        }

//        String ffmpegComand = "ffmpeg -i " + file.getAbsolutePath() + "  -vframes " + frameCount;// + " thumb%04d.jpg";
//        ffmpegComand += " " + tmpImgDir.getAbsolutePath() + "\\" + "thumb%04d.jpg";
//        try {
//            Runtime.getRuntime().exec(ffmpegComand);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
