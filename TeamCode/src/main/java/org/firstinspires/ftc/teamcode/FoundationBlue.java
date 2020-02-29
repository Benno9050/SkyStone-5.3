package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Foundation Blue", group="!")
public class FoundationBlue extends OpMode {
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

    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
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

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        rf.setDirection(DcMotor.Direction.REVERSE);
        rb.setDirection(DcMotor.Direction.REVERSE);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void start() {

        lf.setPower(-0.5); //reverse
        lb.setPower(-0.5);
        rf.setPower(-0.5);
        rb.setPower(-0.5);

        try {
            Thread.sleep(2000);
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

        foundation.setPosition(0.5); //grab foundation

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        lf.setPower(0.5); //move forward
        lb.setPower(0.5);
        rf.setPower(0.5);
        rb.setPower(0.5);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }

        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        foundation.setPosition(1.0); //release foundation

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        lf.setPower(-0.5); //strafe right
        lb.setPower(0.5);
        rf.setPower(0.5);
        rb.setPower(-0.5);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }

        lf.setPower(0); //stop
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);

    }

    public void loop() {

    }

    public void move(double in, boolean frontLeft, boolean rearLeft, boolean frontRight, boolean rearRight) {
        double mm = in * 25.4;

        int frontMotors = (int) ((1440 * mm) / (2 * Math.PI * 49));
        int rearMotors = (int) ((288 * mm) / (2 * Math.PI * 49));

        if (frontLeft) {
            lf.setTargetPosition(frontMotors);
        } else {
            lf.setTargetPosition(-frontMotors);
        }

        if (rearLeft) {
            lb.setTargetPosition(rearMotors);
        } else {
            lb.setTargetPosition(-rearMotors);
        }

        if (frontRight) {
            rf.setTargetPosition(frontMotors);
        } else {
            rf.setTargetPosition(-frontMotors);
        }

        if (rearRight) {
            rb.setTargetPosition(rearMotors);
        } else {
            rb.setTargetPosition(-rearMotors);
        }

//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

}
