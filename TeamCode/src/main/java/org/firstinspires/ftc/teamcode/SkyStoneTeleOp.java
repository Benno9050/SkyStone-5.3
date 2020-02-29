package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="SkyStone TeleOp", group="!")
public class SkyStoneTeleOp extends OpMode {
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

    private double speed = 1.00;


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

        armExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void loop() {
        //GAMEPAD 1---------------------------------------------------------------------------------
        if (Math.abs(gamepad1.left_stick_y) >= 0.1 || Math.abs(gamepad1.right_stick_y) >= 0.1) { //left and right sticks control motors
            if (Math.abs(gamepad1.left_stick_y) >= 0.1) {
                lf.setPower(-gamepad1.left_stick_y * speed);
                lb.setPower(gamepad1.left_stick_y * speed);
            }
            if (Math.abs(gamepad1.right_stick_y) >= 0.1) {
                rf.setPower(-gamepad1.right_stick_y * speed);
                rb.setPower(gamepad1.right_stick_y * speed);
            }
        } else if (gamepad1.dpad_up) { //move forward
            lf.setPower(speed);
            lb.setPower(speed);
            rf.setPower(speed);
            rb.setPower(speed);
        } else if (gamepad1.dpad_down) { //move backward
            lf.setPower(-speed);
            lb.setPower(-speed);
            rf.setPower(-speed);
            rb.setPower(-speed);
        } else if (gamepad1.dpad_left) { //turn left
            lf.setPower(-speed);
            lb.setPower(-speed);
            rf.setPower(speed);
            rb.setPower(speed);
        } else if (gamepad1.dpad_right) { //turn right
            lf.setPower(speed);
            lb.setPower(speed);
            rf.setPower(-speed);
            rb.setPower(-speed);
        } else if (gamepad1.left_trigger >= 0.1) { //strafe left
            lf.setPower(-gamepad1.left_trigger * speed);
            lb.setPower(gamepad1.left_trigger * speed);
            rf.setPower(gamepad1.left_trigger * speed);
            rb.setPower(-gamepad1.left_trigger * speed);
        } else if (gamepad1.right_trigger >= 0.1) { //strafe right
            lf.setPower(gamepad1.right_trigger * speed);
            lb.setPower(-gamepad1.right_trigger * speed);
            rf.setPower(-gamepad1.right_trigger * speed);
            rb.setPower(gamepad1.right_trigger * speed);
        } else if (gamepad1.left_bumper) { //strafe left
            lf.setPower(-speed);
            lb.setPower(speed);
            rf.setPower(speed);
            rb.setPower(-speed);
        } else if (gamepad1.right_bumper) { //strafe right
            lf.setPower(speed);
            lb.setPower(-speed);
            rf.setPower(-speed);
            rb.setPower(speed);
        } else { //turn motors off
            lf.setPower(0);
            lb.setPower(0);
            rf.setPower(0);
            rb.setPower(0);
        }

        if (gamepad1.a) { //change speed to slow/fast mode
            speed = 0.25;
        } else {
            speed = 1.00;
        }

        telemetry.addData("Speed", speed);

        //GAMEPAD 2---------------------------------------------------------------------------------
        if (gamepad2.dpad_up) { //extend arm out
            armExtend.setPower(0.5);
        } else if (gamepad2.dpad_down) { //retract arm
            armExtend.setPower(-0.5);
        } else {
            armExtend.setPower(0);
        }

        if (gamepad2.a) { //lift arm
            armLift.setPower(0.75);
        } else if (gamepad2.y) { //lower arm
            armLift.setPower(-0.75);
        } else {
            armLift.setPower(0);
        }

        if (gamepad2.right_bumper) { //intake in
            inl.setPower(1);
            inr.setPower(-1);
        } else if (gamepad2.left_bumper) { //intake out
            inl.setPower(-1);
            inr.setPower(1);
        } else {
            inl.setPower(0);
            inr.setPower(0);
        }

        if (gamepad2.x) { //close hand
            hand.setPosition(0.7);
        } else if (gamepad2.b) { //open hand
            hand.setPosition(0.5);
        }

        if (gamepad2.dpad_right) {
            foundation.setPosition(0.5);
        } else if (gamepad2.dpad_left) {
            foundation.setPosition(1.0);
        }
        
    }
}
