package org.usfirst.frc.team2537.robot;

import org.usfirst.frc.team2537.robot.arm.ArmSubsystem;
import org.usfirst.frc.team2537.robot.auto.AutoChooser;
import org.usfirst.frc.team2537.robot.auto.AutoShootCommand;
import org.usfirst.frc.team2537.robot.camera.CameraFeeds;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;
import org.usfirst.frc.team2537.robot.input.Sensors;
import org.usfirst.frc.team2537.robot.shooter.actuator.ActuatorSubsystem;
import org.usfirst.frc.team2537.robot.shooter.angle.AngleSubsystemPID;
import org.usfirst.frc.team2537.robot.shooter.flywheel.FlywheelSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	AutoChooser autoChooser;
	Command autoCommand;
	CommandGroup autoCommandGroup; //Timmy Tommy is lazy
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	public static DriveSubsystem driveSys;
	public static CameraFeeds feeds;
	public static ArmSubsystem armSys;

	SendableChooser chooser;
	// My stuff
	public static Sensors sensorSys;
	public static FlywheelSubsystem shooterFlywheelSys;
	public static ActuatorSubsystem shooterActuatorSys;
	public static AngleSubsystemPID shooterAngleSys;

	@Override
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		// SaberMessage.printMessage();
		// Dashboard
		/*
		 * chooser = new SendableChooser(); chooser.addDefault("Default Auto",
		 * defaultAuto); chooser.addObject("My Auto", customAuto);
		 * SmartDashboard.putData("Auto choices", chooser);
		 */

		// Initalize Everything

		driveSys = new DriveSubsystem();
		driveSys.registerButtons();
		driveSys.initDefaultCommand();

		armSys = new ArmSubsystem();
		armSys.initDefaultCommand();
		armSys.registerButtons();
		//
		autoChooser = new AutoChooser();

		shooterFlywheelSys = new FlywheelSubsystem();
		shooterAngleSys = new AngleSubsystemPID();
		shooterActuatorSys = new ActuatorSubsystem();
		shooterFlywheelSys.initDefaultCommand();
		shooterFlywheelSys.registerButtons();
		// Shooter Angle
		shooterAngleSys.initDefaultCommand();
		shooterAngleSys.registerButtons();

		// Shooter Actuator
		shooterActuatorSys.initDefaultCommand();
		shooterActuatorSys.registerButtons();

		sensorSys = new Sensors();
		sensorSys.init();

		sensorSys.registerListener(armSys);
		sensorSys.registerListener(shooterAngleSys);
		sensorSys.registerListener(shooterFlywheelSys);

		feeds = new CameraFeeds();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomousInit() {
		autoCommand = autoChooser.getAutoChoice();
//		autoCommand = new AutoShootCommand();
		Scheduler.getInstance().add(autoCommand);
		Robot.armSys.init();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}


	/**
	 * 
	 */
	public void teleopInit(){
		System.out.println("Teleop init");
		if(autoCommand != null)
			autoCommand.cancel();
		feeds.init();
		sensorSys.handleEvents();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		sensorSys.handleEvents();
		feeds.run();
		Scheduler.getInstance().run();		
//		SmartDashboard.putNumber("Arm IMU", armSys.getIMUAngle());
//		Double shooterAngle = shooterAngleSys.getCurrentAngle();
//		SmartDashboard.putString("Shooter IMU", shooterAngle==null?"null":shooterAngle.toString());
//		SmartDashboard.putNumber("Arm Encoder", armSys.getAngle());
//		SmartDashboard.putBoolean("Is Fwd limit switch enabled", Robot.armSys.armMotor.isFwdLimitSwitchClosed());
//		SmartDashboard.putBoolean("Is Rev limit switch enabled", Robot.armSys.armMotor.isRevLimitSwitchClosed());
	}

	public void testInit() {
		sensorSys.handleEvents();
	}
	
	
	@Override
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		sensorSys.handleEvents();
//		sensorSys.updateSmartDashboardValues();
//		feeds.run();
		Scheduler.getInstance().run();
		
	}

	@Override
	public void disabledInit() {
	}

}
