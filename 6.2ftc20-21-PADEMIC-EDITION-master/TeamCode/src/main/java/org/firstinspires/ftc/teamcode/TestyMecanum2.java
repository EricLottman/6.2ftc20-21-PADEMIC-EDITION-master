package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Mecanum Drive Example", group="Iterative Opmode")
public class TestyMecanum2 extends OpMode {

    /*
     * The mecanum drivetrain involves four separate motors that spin in
     * different directions and different speeds to produce the desired
     * movement at the desired speed.
     */

    // declare and initialize four DcMotors.
    private DcMotor leftFront  = null;
    private DcMotor rightFront = null;
    private DcMotor leftRear   = null;
    private DcMotor rightRear  = null;

    double wheelSpeedAdadpter=0;

    @Override
    public void init() {

        // Name strings must match up with the config on the Robot Controller
        // app.
        leftFront   = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront  = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear    = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear   = hardwareMap.get(DcMotor.class, "rightRear");
        
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD); //changed some of the directions.
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop() {

        // Mecanum drive is controlled with three axes: drive (front-and-back),
        // strafe (left-and-right), and twist (rotating the whole chassis).
        double drive  = -gamepad1.left_stick_y; //was positive.
        double strafe = gamepad1.left_stick_x * 1.5; //Makes angles more accurate becasue the wheels have to turn more to go sideways, so it allows the bot to adapt better when it comes to going at obscure angles on the plane
        double twist  = gamepad1.right_stick_x;

        /*
         * If we had a gyro and wanted to do field-oriented control, here
         * is where we would implement it.
         *
         * The idea is fairly simple; we have a robot-oriented Cartesian (x,y)
         * coordinate (strafe, drive), and we just rotate it by the gyro
         * reading minus the offset that we read in the init() method.
         * Some rough pseudocode demonstrating:
         *
         * if Field Oriented Control:
         *     get gyro heading
         *     subtract initial offset from heading
         *     convert heading to radians (if necessary)
         *     new strafe = strafe * cos(heading) - drive * sin(heading)
         *     new drive  = strafe * sin(heading) + drive * cos(heading)
         *
         * If you want more understanding on where these rotation formulas come
         * from, refer to
         * https://en.wikipedia.org/wiki/Rotation_(mathematics)#Two_dimensions
         */

        // You may need to multiply some of these by -1 to invert direction of
        // the motor.  This is not an issue with the calculations themselves.
        if (gamepad2.b) && {
            wheelSpeedAdadpter=.5
        } else {
            wheelSpeedAdadpter=0
        }

        double[] speeds = {
            (drive + strafe + twist),
            (drive - strafe - twist),
            (drive - strafe + twist),
            (drive + strafe - twist)
        };

        // Because we are adding vectors and motors only take values between
        // [-1,1] we may need to normalize them.

        // Loop through all values in the speeds[] array and find the greatest
        // *magnitude*.  Not the greatest velocity.
        double max = Math.abs(speeds[0]);
        for(int i = 0; i < speeds.length; i++) {
            if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
        }

        // If and only if the maximum is outside of the range we want it to be,
        // normalize all the other speeds based on the given speed value.
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
        }

        // apply the calculated values to the motors.
        leftFront.setPower(speeds[0]-wheelSpeedAdadpter);
        rightFront.setPower(speeds[1]-wheelSpeedAdadpter);
        leftRear.setPower(speeds[2]-wheelSpeedAdadpter);
        rightRear.setPower(speeds[3]-wheelSpeedAdadpter);
    }
}
