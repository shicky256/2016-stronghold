package org.usfirst.frc.team2537.robot.drive;

import org.usfirst.frc.team2537.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTypeToggleCommand extends Command {
	@Override
	protected void initialize() {
		Robot.driveSys.setDriveType(Robot.driveSys.getDriveType().getNextDriveTypeInEnum());
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {

	}

}