package com.company;

import org.opencv.aruco.DetectorParameters;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.aruco.Aruco;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Hello, OpenCV");

        var video0 = new VideoCapture();
        var video1 = new VideoCapture();

        video0.open(0);
        video1.open(1);

        var videos = new ArrayList<VideoCapture>();

        videos.add(video0);
        videos.add(video1);

        var frames = new ArrayList<Mat>(){
            {
                add(new Mat());
                add(new Mat());
            }
        };
//        var window = new ImageWindow("capture0", ImageWindow.WINDOW_AUTOSIZE);
        var window = new HighGui();

//        BufferedImage temp;

        var dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_50);
        var parameters = DetectorParameters.create();
        var corners = new ArrayList<Mat>();
        var ids = new Mat();
        var rejectedImgPoints = new ArrayList<Mat>();

        var wasMarkerFoundList = new ArrayList<Boolean>(){
            {
                add(false);
                add(false);
            }
        };
        var wasMarkerLostList = new ArrayList<Boolean>(){
            {
                add(false);
                add(false);
            }
        };
        var wasTouched = false;

        while(true){
            for (int i = 0; i < 2; i++){
                videos.get(i).read(frames.get(i));

                Aruco.detectMarkers(frames.get(i), dictionary, corners, ids, parameters, rejectedImgPoints);

                wasMarkerLostList.set(i, wasMarkerFoundList.get(i) && !(ids.empty()));

                if(ids.empty()){
                    wasMarkerFoundList.set(i, false);
                    wasMarkerLostList.set(i, true);
                }
                else{
                    wasMarkerFoundList.set(i, true);
                    Aruco.drawDetectedMarkers(frames.get(i), corners, ids);
                    System.out.println("Marker was Found!");
                }
            }

            if(!wasMarkerLostList.contains(false)){
                if(!wasTouched){
                    System.out.println("Touch!");

                    for(int i = 0; i < wasMarkerLostList.size(); i++){
                        wasMarkerLostList.set(i, false);
                    }
                }

                wasTouched = true;
            }
            else{
                wasTouched = false;
            }
        }
    }
}
