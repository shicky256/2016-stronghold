package org.usfirst.frc.team2537.robot.input;

/**
 * List of ports that we will use on the robot. Change as necessary
 * 
 * @author Alex Taber
 *
 */
public final class Ports {
	public static final int 
	//Joystick
	JOYSTICK_ONE_PORT = 0, 
	JOYSTICK_TWO_PORT = 1,
	
	//XBOX Controller
	XBOX = 2,
	
	//Motor Talons
	FRONT_RIGHT_MOTOR_PORT = 1, 
	FRONT_LEFT_MOTOR_PORT = 4,
	BACK_RIGHT_MOTOR_PORT = 2, 
	BACK_LEFT_MOTOR_PORT = 3,
	
	//Other Talon
	ARM_TALON = 5, //Change when confirmed
	
	//Sensors
	DRIVE_ULTRASONIC_INPUT = 9,
	DRIVE_ULTRASONIC_ECHO = 8,
	ARM_IMU = 0;
}