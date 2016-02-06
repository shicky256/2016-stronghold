package org.usfirst.frc.team2537.robot.shooter.flywheel;

import org.usfirst.frc.team2537.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class FlywheelCommand extends Command {
	// Speed
	private static final double SPEED_INCREMENT = .05;
	private static final double SPEED_PROXIMITY = 0.05;
	private double currentLeftFlywheelSpeed = 0.0;
	private double currentRightFlywheelSpeed = 0.0;
	private double targetSpeed;

	public FlywheelCommand(double speed) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		// super(FlywheelShootSpeed);
		// if (flywheelSpeed < 0) System.out.println("Negative speed of " +
		// speed + "given to flywheel spin up.");
		targetSpeed = speed;
	}

	@Override
	protected void initialize() {
		// Get the motor values to start with.
		currentLeftFlywheelSpeed = Robot.shooterFlywheelSys.getLeftFlywheelVelocity();
		currentRightFlywheelSpeed = Robot.shooterFlywheelSys.getRightFlywheelVelocity();

	}

	@Override
	public boolean isFinished() {

		return (isInRange(Robot.shooterFlywheelSys.getLeftFlywheelVelocity())
				&& isInRange(Robot.shooterFlywheelSys.getRightFlywheelVelocity()));

	}

	@Override
	protected void end() {
	}

	@Override
	protected void execute() {
		currentLeftFlywheelSpeed = Robot.shooterFlywheelSys.getLeftFlywheelVelocity();
		currentLeftFlywheelSpeed = incrementTowardsRange(currentLeftFlywheelSpeed);
		Robot.shooterFlywheelSys.setLeftFlywheelVelocity(currentLeftFlywheelSpeed);
		currentRightFlywheelSpeed = Robot.shooterFlywheelSys.getRightFlywheelVelocity();
		currentRightFlywheelSpeed = incrementTowardsRange(currentRightFlywheelSpeed);
		Robot.shooterFlywheelSys.setRightFlywheelVelocity(currentRightFlywheelSpeed);
		if(isFinished()) {
			//this.end();
			this.interrupted();
		}
	}

	@Override
	protected void interrupted() {
		//Robot.shooterFlywheelSys.setLeftFlywheelVelocity(0);
		//Robot.shooterFlywheelSys.setRightFlywheelVelocity(0);

	}

	private double incrementTowardsRange(double speed) {
		if (speed < targetSpeed - SPEED_PROXIMITY) {
			return speed + SPEED_INCREMENT;

		} else if (speed > targetSpeed + SPEED_PROXIMITY) {
			return speed - SPEED_INCREMENT;
		}
		return speed;
	}

	private boolean isInRange(double speed) {
		return Math.abs(speed - targetSpeed) <= SPEED_PROXIMITY;
	}
}
