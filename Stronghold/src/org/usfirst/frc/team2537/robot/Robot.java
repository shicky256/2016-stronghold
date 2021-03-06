package org.usfirst.frc.team2537.robot;

import org.usfirst.frc.team2537.robot.arm.ArmSubsystem;
import org.usfirst.frc.team2537.robot.auto.ArmAndDriveAuto;
import org.usfirst.frc.team2537.robot.camera.CameraFeeds;
import org.usfirst.frc.team2537.robot.climber.Climber;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;
import org.usfirst.frc.team2537.robot.input.Sensors;
import org.usfirst.frc.team2537.robot.shooter.actuator.ActuatorSubsystem;
import org.usfirst.frc.team2537.robot.shooter.flywheel.FlywheelSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static DriveSubsystem driveSys;
	public static CameraFeeds feeds;
	public static ArmSubsystem armSys;
	public static Climber climberSys;
	public static Sensors sensorSys;
	public static FlywheelSubsystem shooterFlywheelSys;
	public static ActuatorSubsystem shooterActuatorSys;

	@Override
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		// Dashboard
		/*
		 * chooser = new SendableChooser(); chooser.addDefault("Default Auto",
		 * defaultAuto); chooser.addObject("My Auto", customAuto);
		 * SmartDashboard.putData("Auto choices", chooser);
		 */

		sensorSys = new Sensors();
		sensorSys.init();

		driveSys = new DriveSubsystem();
		driveSys.registerButtons();
		driveSys.initDefaultCommand();
		
		climberSys = new Climber();
		climberSys.initDefaultCommand();
		climberSys.registerButtons();
		climberSys.enable();
		climberSys.positionMode();

		armSys = new ArmSubsystem();
		armSys.initDefaultCommand();
		armSys.registerButtons();

		shooterFlywheelSys = new FlywheelSubsystem();

		shooterActuatorSys = new ActuatorSubsystem();
		shooterFlywheelSys.initDefaultCommand();
		shooterFlywheelSys.registerButtons();

		// Shooter Actuator
		shooterActuatorSys.initDefaultCommand();
		shooterActuatorSys.registerButtons();

		sensorSys.registerListener(armSys);
		sensorSys.registerListener(shooterFlywheelSys);

		feeds = new CameraFeeds();
		feeds.init();
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
		feeds.init();
		Scheduler.getInstance().add(new ArmAndDriveAuto());
		System.out.println("Autonomous start");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		feeds.run();
	}


	/**
	 * 
	 */
	public void teleopInit(){
		System.out.println("Teleop init");
		feeds.init();
		sensorSys.handleEvents();
		climberSys.electromagnetOn();

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		sensorSys.handleEvents();
		feeds.run();
		Scheduler.getInstance().run();		
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
		Scheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		feeds.init();
	}
	
	@Override
	public void disabledPeriodic() {
		feeds.run();
	}

}
