package org.usfirst.frc.team2537.robot.shooter.flywheel;

import java.util.HashMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2537.robot.input.Ports;
import org.usfirst.frc.team2537.robot.input.Sensor;
import org.usfirst.frc.team2537.robot.input.HumanInput;
import org.usfirst.frc.team2537.robot.input.SensorListener;
import org.usfirst.frc.team2537.robot.shooter.ShootCommandGroup;
import org.usfirst.frc.team2537.robot.shooter.HarvestCommandGroup;
import static edu.wpi.first.wpilibj.CANTalon.FeedbackDevice.QuadEncoder;

/*
 * Important notice! If we use getSoftLimit() it will be in native units.
 */
public class FlywheelSubsystem extends Subsystem implements SensorListener {

	// Constants
	public static final boolean DEBUG = false;
	private static final int ENCODER_TICKS_PER_REV = 20;
	private static final double UNITS_PER_100MS_TO_RPM = 100.0 / 4096 * 1000 * 60;
	private static final double SPEED_TOLERANCE = 5;
	// Max voltage that can be output from the flywheel talons.
	private static final float MAX_VOLTAGE = 12.0f;
	// 5 volts per second ramp rate for the flywheels
	private static final double VOLTAGE_RAMP_RATE = 12;
	// yet
	private static final double P = 2.5, I = 0.01, D = 0.0;
	// Vars
	private boolean proximityValue = false;
	public CANTalon rightFlywheelMotor;
	public CANTalon leftFlywheelMotor;

	public FlywheelSubsystem() {
		// Make sure the the mode to velocity so we can modify it.
		rightFlywheelMotor = new CANTalon(Ports.RIGHT_FLYWHEEL_PORT);
		leftFlywheelMotor = new CANTalon(Ports.LEFT_FLYWHEEL_PORT);

		// Set encoder.
		rightFlywheelMotor.setFeedbackDevice(QuadEncoder);
		rightFlywheelMotor.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
		leftFlywheelMotor.setFeedbackDevice(QuadEncoder);
		leftFlywheelMotor.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);

		// Make sure the soft limits are disabled.
		rightFlywheelMotor.enableForwardSoftLimit(false);
		rightFlywheelMotor.enableReverseSoftLimit(false);
		leftFlywheelMotor.enableForwardSoftLimit(false);
		leftFlywheelMotor.enableReverseSoftLimit(false);

		// Disable the limit switchs, as they do not exist.
		rightFlywheelMotor.enableLimitSwitch(false, false);
		leftFlywheelMotor.enableLimitSwitch(false, false);

		// Nominal voltages, not sure if this is needed
		// Something found indicated that if the PID is bad,
		// voltage will ramp to the nominal Output, we should try
		// turning these off.
		rightFlywheelMotor.configNominalOutputVoltage(0.0f, 0.0f);
		rightFlywheelMotor.configPeakOutputVoltage(12.0f, -12.0f);
		rightFlywheelMotor.configMaxOutputVoltage(MAX_VOLTAGE);
		leftFlywheelMotor.configNominalOutputVoltage(0.0f, 0.0f);
		leftFlywheelMotor.configPeakOutputVoltage(12.0f, -12.0f);
		leftFlywheelMotor.configMaxOutputVoltage(MAX_VOLTAGE);

		// Set rightFlywheelMotor to be reversed of everything else.
		leftFlywheelMotor.reverseOutput(false);
		leftFlywheelMotor.reverseSensor(false);
		rightFlywheelMotor.reverseOutput(false);
		rightFlywheelMotor.reverseSensor(true);

		// Set PID's
		rightFlywheelMotor.setPID(P, I, D);
		rightFlywheelMotor.setF(0);
		leftFlywheelMotor.setPID(P, I, D);
		leftFlywheelMotor.setF(0);
		leftFlywheelMotor.setVoltageRampRate(VOLTAGE_RAMP_RATE);
		rightFlywheelMotor.setVoltageRampRate(VOLTAGE_RAMP_RATE);

		// Make the talons go into the speed control mode.
		rightFlywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		leftFlywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
	}

	@Override
	public void initDefaultCommand() {
	}

	public void registerButtons() {
		HumanInput.registerPressedCommand(HumanInput.ballShootTrigger, new ShootCommandGroup());
		HumanInput.registerWhileHeldCommand(HumanInput.harvestBallTrigger, new HarvestCommandGroup());
	}

	// Set and get speed
	/**
	 * Set the speed of the motors.
	 *
	 * @param speed
	 *            in RPM.
	 */
	public void setSpeed(double speed) {
		System.out.println("Set Motor Speed");
		if (DEBUG) {
			System.out.println("Left Flywheel Speed: " + rightFlywheelMotor.getSpeed());
		}
		rightFlywheelMotor.set(-speed);
		leftFlywheelMotor.set(speed);
	}

	// LEFT
	/**
	 * Get the current speed of the left flywheel.
	 * 
	 * @return flywheel speed in RPM
	 */
	public double getLeftSpeed() {
		if (DEBUG) {
			System.out.println("Left Flywheel Speed: " + rightFlywheelMotor.getSpeed());
			System.out.println("Right Flywheel Speed: " + leftFlywheelMotor.getSpeed());
		}
		return rightFlywheelMotor.getSpeed();
	}

	/**
	 * Get the error of the left flywheel. This is the distance from the wanted
	 * speed and the current speed.
	 * 
	 * @return Difference between current speed and wanted speed. In RPM
	 */
	public double getLeftError() {
		// We want to change this from native units (units per 100ms) to RPM.
		// return leftFlywheelMotor.getError()/100/4096*1000*60;
		/*
		 * Here is how I came to this conversion factor.
		 * 
		 */
		// Simplified for speed.
		return rightFlywheelMotor.getError() * UNITS_PER_100MS_TO_RPM;
	}

	// RIGHT
	/**
	 * Get the current speed of the left flywheel.
	 * 
	 * @return flywheel speed in RPM
	 */
	public double getRightSpeed() {
		if (DEBUG) {
			System.out.println("Right Flywheel Speed: " + leftFlywheelMotor.getSpeed());
		}
		return leftFlywheelMotor.getSpeed();
	}

	/**
	 * Get the error of the left flywheel. This is the distance from the wanted
	 * speed and the current speed.
	 * 
	 * @return Difference between current speed and wanted speed in RPM.
	 */
	public double getRightError() {
		return leftFlywheelMotor.getError() * UNITS_PER_100MS_TO_RPM;
	}

	/**
	 * Check if a ball is in the shooter.
	 * 
	 * @return if the proximity sensor is returning true to seeing something.
	 */
	public boolean isBallPresent() {
		return proximityValue;
	}

	// Counter is only used for is at speed to determine how many consecutive
	// measurments are at the speed that we want to be at.
	int counter = 0;
	public boolean isAtSpeed(double speed) {
		System.out.println(counter);
		if (Math.abs(getRightSpeed() - speed) <= SPEED_TOLERANCE
				&& Math.abs(-getLeftSpeed() - speed) <= SPEED_TOLERANCE) {
			counter++;
		} else {
			counter = 0;
		}
		if (counter == 10) {
			counter = 0;
			return true;
		}
		return false;
	}

	@Override
	// Proximity
	public void receivedValue(HashMap<Sensor, Double> sensorMap) {
		Double value = sensorMap.get(Sensor.SHOOTER_BALL);
		if (value != null)
			proximityValue = (value == 1);

		// This is routenly called regardless of motor activation, should use
		// this to
		// put debug code.
		if (DEBUG) {
			System.out.print("Flywheel ");
			System.out.print("L Speed: " + getLeftSpeed());
			System.out.print("L Error: " + getLeftError());
			System.out.print("R Speed: " + getRightSpeed());
			System.out.println("R Error: " + getRightError());
		}
	}
}