package org.usfirst.frc.team2537.robot.shooter.actuator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2537.robot.Robot;

/**
 *
 */
public class ActuatorCommand extends Command {
	//Varibles
	private boolean extended;
	private static double TIMEOUT = 1.0;
	
    public ActuatorCommand(boolean extended) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	super(TIMEOUT);
    	requires(Robot.shooterActuatorSys);
    	this.extended = extended;
    }
    
    @Override
    // Called just before this Command runs the first time
    protected void initialize() {
    	//Fire the solenoid
    	Robot.shooterActuatorSys.setPosition(extended);
    }
    
    @Override
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//What for the time.
    }
    
    @Override
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (extended){
    		return Robot.shooterActuatorSys.isExtended();
    	}
    	//else
    	return Robot.shooterActuatorSys.isRetracted();
    }
    
    @Override
    // Called once after isFinished returns true
    protected void end() {
    }
    
    @Override
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
