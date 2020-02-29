package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.Range;
import java.lang.reflect.Array;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
//import org.firstinspires.ftc.teamcode.PathPoint;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cAddr;
//import org.firstinspires.ftc.teamcode.Passbot2020;
import java.util.*;
import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;

@Disabled
@Autonomous(name="Skystones Red", group="!")
public class SkyStoneRed extends LinearOpMode{
    int colorSide = 1;
    private VuforiaLocalizer vuforia;
    boolean farTest, centerTest, closeTest;
    int testxStart = 351, testyStart = 187, testwidth = 722, testheight = 120;

    int pos;

    private DcMotor lf;
    private DcMotor lb;
    private DcMotor rf;
    private DcMotor rb;

    private DcMotor inl;
    private DcMotor inr;

    private DcMotor armExtend;
    private DcMotor armLift;

    private Servo hand;
    private Servo foundation;

    //Telemetry outputs the average red value, average green value, average blue value,
    //number of yellow pixels, and number of black pixels.
    //we found the number of yellow pixels to be more accurate so that is what this
    //file does.
    //in order to run you need to define the pixel locations of the blocks... the pixels
    //must be measured from the bottom right pixel, though phone orientation may affect this
    //the test width is for the entire 3 block row. We found the best way to find these
    //measurements by opening up the built in Concept: VuMark Id and using a ruler to measure
    //the distance from the bottom right corner. Then use the entire distance across the view
    //to get the total x distance and using the fact that the camera is 100x720 to get
    //the partial x distance. This is repeated to get every number and then fine tuned.
    public void runOpMode() throws InterruptedException{
        lf = hardwareMap.get(DcMotor.class, "lf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rf = hardwareMap.get(DcMotor.class, "rf");
        rb = hardwareMap.get(DcMotor.class, "rb");

        inl = hardwareMap.get(DcMotor.class, "inl");
        inr = hardwareMap.get(DcMotor.class, "inr");

        armExtend = hardwareMap.get(DcMotor.class, "ae");
        armLift = hardwareMap.get(DcMotor.class, "al");

        hand = hardwareMap.get(Servo.class, "h");
        foundation = hardwareMap.get(Servo.class, "f");

        rf.setDirection(DcMotor.Direction.REVERSE);
        rb.setDirection(DcMotor.Direction.REVERSE);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        initVuforia();

        while(true) {
            findSkystone();
            telemetry.update();
            if (opModeIsActive()) {
                break;
            }
        }

        if (closeTest) {
            posA();
        } else if (centerTest) {
            posB();
        } else {
            posC();
        }
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = "AeQcHp7/////AAABmSRlbZOksUhznhWTdwL8Iphf3imOF5R+JRPW2JTcDK2Anq55CZNsX5B7rvYpp/JTBjNSeVS5Es2VW54M7hELDYTgaTFsxepV9rr2xVAojTQxmfvBbFXmjpQjNVUI+4J6dPAZY5IiaVozfahFwYaBSuHbRfuTERPHwmKh8DBKTke+EKjH7bXkzzjcDA9XhobUydgawa5gQ/f6aeF+EgvQAUGGDHiam147y6xuNRrqaRBhKvXM3tzrrxaNni9DmHuo3B3adjAQTdHy7oMcz+TZC0isE2a8y33r0hU//JgE0OrBbWoPumLmXg8sRtwoS+MXwn8vZ0ll8qHgp2mME+WszqFG29e4P3u2vFKeXOwPVl3B";
        parameters.cameraDirection = CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.setFrameQueueCapacity(1);
        vuforia.enableConvertFrameToBitmap();
    }

    public void findSkystone() throws InterruptedException{

        farTest=false;
        centerTest=false;
        closeTest=false;

        Bitmap bitmap = vuforia.convertFrameToBitmap(vuforia.getFrameQueue().take());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] rawColorArray = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(rawColorArray,0,width,0,0,width,height);
        double[][][] pixels = new double[width][height][3];
        for(int j = 0; j<height; j++){
            for(int i = 0; i<width; i++){
                //telemetry.addLine("width: " + i + " height: " + j + " value: "+((rawColorArray[j*width+i])&0xFF));
                pixels[i][j][0] = ((rawColorArray[j*width+i]>>16)&0xFF);
                pixels[i][j][1] = ((rawColorArray[j*width+i]>>8)&0xFF);
                pixels[i][j][2] = ((rawColorArray[j*width+i])&0xFF);
            }
        }
        int increment = testwidth/3;
        double[] block1Test = averageValues(pixels, testxStart+10, increment-20, testyStart, testheight);
        double[] block2Test = averageValues(pixels, testxStart+increment+10, increment-20, testyStart, testheight);
        double[] block3Test = averageValues(pixels, testxStart+increment*2+10, increment-20, testyStart, testheight);
        //r,g,b,yellows,blacks
        telemetry.addLine(Arrays.toString(block1Test));
        telemetry.addLine(Arrays.toString(block2Test));
        telemetry.addLine(Arrays.toString(block3Test));

        if(block1Test[3]<block2Test[3]&&block1Test[3]<block3Test[3]){
            if(colorSide==1){
                closeTest=true;
            }else{
                farTest=true;
            }
        }else if(block2Test[3]<block1Test[3]&&block2Test[3]<block3Test[3]){
            if(colorSide==1){
                centerTest=true;
            }else{
                centerTest=true;
            }
        }else if(block3Test[3]<block2Test[3]&&block3Test[3]<block1Test[3]){
            if(colorSide==1){
                farTest=true;
            }else{
                closeTest=true;
            }
        }
        telemetry.addLine(("close "+closeTest)+("   center "+centerTest)+("    far "+farTest));
        telemetry.update();

        if (closeTest) {
            pos = 1;
        } else if (centerTest) {
            pos = 2;
        } else if (farTest) {
            pos = 3;
        } else {
            pos = 1;
        }
    }
    public double[] averageValues(double[][][] pixels, int xStart, int xLength, int yStart, int yLength){
        double[] temp = new double[5];
        int yellows = 0;
        int blacks = 0;


        double numPixels = xLength * yLength;
        for(int j = yStart; j<=(yStart+yLength); j++){
            for(int i = xStart; i<=(xStart+xLength); i++){
                //telemetry.addLine("j: " + j + " i: " + i + "Values: " + pixels[i][j][0] +","+ pixels[i][j][1]+","+ pixels[i][j][2]);
                temp[0]+=pixels[i][j][0];
                temp[1]+=pixels[i][j][1];
                temp[2]+=pixels[i][j][2];
            }
        }
        temp[0]/=numPixels;
        temp[1]/=numPixels;
        temp[2]/=numPixels;
        temp[0] = Math.round(temp[0]);
        temp[1] = Math.round(temp[1]);
        temp[2] = Math.round(temp[2]);

        for(int j = yStart; j<=(yStart+yLength); j++){
            for(int i = xStart; i<=(xStart+xLength); i++){
                //telemetry.addLine("j: " + j + " i: " + i + "Values: " + pixels[i][j][0] +","+ pixels[i][j][1]+","+ pixels[i][j][2]);
                if(pixels[i][j][0]>90&&pixels[i][j][1]>90&&pixels[i][j][2]<120&&pixels[i][j][0]+pixels[i][j][1]>pixels[i][j][2]*2.7){
                    yellows++;
                }
                if(pixels[i][j][0]<100&&pixels[i][j][1]<100&&pixels[i][j][2]<100&&(pixels[i][j][0]+pixels[i][j][1]+pixels[i][j][2])/3<75){
                    blacks++;
                }
            }
        }
        temp[3] = yellows;
        temp[4] = blacks;
        return temp;
    }

    private void posA() {
        lf.setPower(-0.5); //strafe left
        lb.setPower(-0.5);
        rf.setPower(-0.5);
        rb.setPower(-0.5);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {

        }

        lf.setPower(0); //stop
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        lf.setPower(-0.5); //strafe left
        lb.setPower(0.5);
        rf.setPower(0.5);
        rb.setPower(-0.5);

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {

        }

        lf.setPower(0); //stop
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {

        }
        inl.setPower(-1);
        inr.setPower(1);

        lf.setPower(0.5); //move forward
        lb.setPower(0.5);
        rf.setPower(0.5);
        rb.setPower(0.5);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }

        lf.setPower(0); //stop
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
        inl.setPower(0);
        inr.setPower(0);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {

        }

        lf.setPower(0.5); //strafe right
        lb.setPower(-0.5);
        rf.setPower(-0.5);
        rb.setPower(0.5);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {

        }

        lf.setPower(0); //stop
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {

        }

        lf.setPower(0.5); //move forward
        lb.setPower(0.5);
        rf.setPower(0.5);
        rb.setPower(0.5);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {

        }

        lf.setPower(0); //stop
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {

        }

        lf.setPower(-0.5); //move backward
        lb.setPower(-0.5);
        rf.setPower(-0.5);
        rb.setPower(-0.5);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {

        }

        lf.setPower(0); //stop
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
    }

    private void posB() {

    }

    private void posC() {

    }
}

