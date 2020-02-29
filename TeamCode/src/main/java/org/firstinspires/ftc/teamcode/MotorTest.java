package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Motor Test", group="Test")
public class MotorTest extends OpMode {

    DcMotor m;
    DcMotor m2;

    public void init() {
        m = hardwareMap.get(DcMotor.class, "m");
        m2 = hardwareMap.get(DcMotor.class, "m2");
    }

    public void loop() {
        if (gamepad1.x) {
            m.setPower(0.25);
        } else if (gamepad1.a) {
            m.setPower(-0.25);
        } else {
            m.setPower(0);
        }

        if (gamepad1.y) {
            m2.setPower(0.25);
        } else if (gamepad1.b) {
            m2.setPower(-0.25);
        } else {
            m2.setPower(0);
        }
    }
}