package org.usfirst.frc.team2537.robot.shooter.flywheel;

import java.util.HashMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2537.robot.input.Ports;
import org.usfirst.frc.team2537.robot.input.Sensor;
import org.usfirst.frc.team2537.robot.input.HumanInput;
import org.usfirst.frc.team2537.robot.input.SensorListener;
import org.usfirst.frc.team2537.robot.shooter.ShootCommandGroup;
import org.usfirst.frc.team2537.robot.shooter.HarvestCommandGroup;

public class FlywheelSubsystem extends Subsystem implements SensorListener {	
	//Constants
	private static final CANTalon leftFlywheelMotor 	= new CANTalon(Ports.TALON_LEFT_FLYWHEEL_PORT);
	private static final CANTalon rightFlywheelMotor 	= new CANTalon(Ports.TALON_RIGHT_FLYWHEEL_PORT);
	//Make the talon's go to the right control mode.
	static {
		leftFlywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		rightFlywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);	
	}
	private static final Solenoid ballPistonPusher 		= new Solenoid(Ports.SOLENOID_PORT);
	private static final boolean  CHECK_TEMPERATURE 	= true;
	private static final double   MAX_TEMPERATURE 		= 100;//celsuis
	//Vars
	private boolean proximityValue = false;
	
	public FlywheelSubsystem() {
		//Starting motors.
		//Make sure the the mode to velocity so we can modify it.
		//Everything will be in "position change / 10ms"
	}
	
	public void actuateSolenoid() {
		ballPistonPusher.set(true);
	}
	public void retractSolenoid() {
		ballPistonPusher.set(false);
	}
	
	public void setSolenoid(boolean extended){
		ballPistonPusher.set(extended);
	}
	@Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	//setDefaultCommand(new ShotBallCommand());
    	//There is not a defualt command.
    }
	
	public void registerButtons() {
		HumanInput.registerPressedCommand(HumanInput.ballShootTrigger, new ShootCommandGroup());
		HumanInput.registerPressedCommand(HumanInput.harvestBallTrigger, new HarvestCommandGroup());
		
	}
	//Shooter Left Flywheel controls.
	public double getLeftFlywheelVelocity() {
		return leftFlywheelMotor.getEncVelocity();
	}
	
	/**
	 * Set the velocity of the left flywheel.
	 * 
	 * @param velocity The voltage amount the wheel will be set to. 
	 * 				This should be [-1, 1].
	 */

	public void setLeftFlywheelVelocity(double velocity) {
		leftFlywheelMotor.set(velocity);
	}
	
	//Shooter Right Flywheel controls.
	public double getRightFlywheelVelocity() {
		return rightFlywheelMotor.getEncVelocity();
	}
	
	/**
	 * Set the velocity of the right flywheel.
	 * 
	 * @param velocity TODO find speed unit.
	 */
	public void setRightFlywheelVelocity(double velocity) {
		rightFlywheelMotor.set(velocity);
	}
	
	//Let the commands have assess to temperature readings.
	public boolean isTemperatureFault(){
		//Check to make sure I'm not on fire!!
		return CHECK_TEMPERATURE && (leftFlywheelMotor.getTemperature() >= MAX_TEMPERATURE || 
				rightFlywheelMotor.getTemperature() >= MAX_TEMPERATURE);
	}
	
	@Override
	//Proximity
	public void receivedValue(HashMap<String, Double> sensorMap) {
		//BEWARE OF NullPointer
		try {
			proximityValue = sensorMap.get(Sensor.SHOOTER_BALL) == 1;
		} catch (NullPointerException e){
			//We have an error.
			//We don't actually care.
		}
	}
	
	public boolean proximityValue(){
		return proximityValue;
	}
}
