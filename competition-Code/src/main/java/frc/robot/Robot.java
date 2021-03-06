/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.PixyCamera;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PneumaticSubsystem;
import frc.robot.subsystems.*;
import frc.robot.OI;
import frc.robot.subsystems.PixyCamera;
import edu.wpi.first.wpilibj.CameraServer;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Command driverControls;
  public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
  public static DriveTrain m_drivetrain = new DriveTrain();
  public static PixyCamera m_pixycam = new PixyCamera();
  public static IntakeSubsystem m_intake = new IntakeSubsystem();
  public static PneumaticSubsystem m_pneumatics = new PneumaticSubsystem();
  public static OI m_oi;


  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  SendableChooser<Command> m_driveselect = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_oi = new OI();
    m_oi.init();
    m_drivetrain.init();
    m_pneumatics.init();

    //Our drive select chooser
    m_driveselect.setDefaultOption("Tank Controls", new DriveWithController());
    m_driveselect.addOption("GTA Controls", new DriveGTA());
    m_driveselect.addOption("COD Controls", new DriveCOD());
    m_driveselect.addOption("TahirGTA Controls", new DriveGTAInverted());
    SmartDashboard.putData("Driver mode", m_driveselect);
    SmartDashboard.putNumber("Throttle", RobotMap.masterThrottle);
    SmartDashboard.putNumber("Krab Speed", RobotMap.krabSpeed);
		SmartDashboard.putNumber("Pixy x1", -1);
		SmartDashboard.putNumber("Pixy x2", -1);
    SmartDashboard.putNumber("Pixy mid", -1);
    SmartDashboard.putNumber("Krabcoder", m_intake.intakeLR.getSensorCollection().getQuadraturePosition()); //I *think* this will output the encoder value to SmartDashboard. Can't test.
    SmartDashboard.putData(m_drivetrain);

    CameraServer.getInstance().startAutomaticCapture();


  }

  public static void wait1MSec(long time){
		// wait1MSec exists only to mimic a function that's familiar to anyone with Vex/RobotC experience.
		// This will pause execution of code for a set duration (in milliseconds), allowing for simple drive-for-time behaviours
		long Time0 = System.currentTimeMillis();
	    long Time1;
	    long runTime = 0;
	    while(runTime<time){
	        Time1 = System.currentTimeMillis();
	        runTime = Time1 - Time0;
	        //System.out.println("Our runtime: " + Long.toString(runTime)); // TODO: remove me please
	    }
	}


  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous init does the exact same thing as TeleOp init.
   */
  @Override
  public void autonomousInit() {
    
    Command driverControls = m_driveselect.getSelected();
    Command operatorControls = new OperatorControl();
    RobotMap.masterThrottle = SmartDashboard.getNumber("Throttle", 0.9); //Get value for the throttle. Take 0.9 as a default.
    RobotMap.krabSpeed = SmartDashboard.getNumber("Krab Speed", 0.5); //Default speed for the krab is 0.3
    m_intake.intakeLR.getSensorCollection().setQuadraturePosition(0, 20);
    driverControls.start();
    operatorControls.start();
  
  }

  /**
   * This function is called periodically during autonomous. It's the exact same code from TeleOp, because of Sandstorm
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();

    m_pixycam.pixyBroadcast(); //Update SmartDashboard with pixy data.
    SmartDashboard.putNumber("Krabcoder", m_intake.intakeLR.getSensorCollection().getQuadraturePosition());

    //JT: Just in case, if for some reason we lose control this restarts the control command
    if (Robot.m_drivetrain.getCurrentCommand() == null) {
      Scheduler.getInstance().add(new DriveWithController());
    }
  
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when teleop starts running.
    // If you want the autonomous to continue until interrupted by another command, remove
    // this line or comment it out.
    
    Command driverControls = m_driveselect.getSelected();
    Command operatorControls = new OperatorControl();
    RobotMap.masterThrottle = SmartDashboard.getNumber("Throttle", 0.9); //Get value for the throttle. Take 0.9 as a default.
    RobotMap.krabSpeed = SmartDashboard.getNumber("Krab Speed", 0.5); //Default speed for the krab is 0.3
    m_intake.intakeLR.getSensorCollection().setQuadraturePosition(0, 20);
    driverControls.start();
    operatorControls.start();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    Scheduler.getInstance().run();

    //m_pixycam.cameraLEDRing.set(true); //Turn on LED ring. This should be tied to a button later.
    m_pixycam.pixyBroadcast(); //Update SmartDashboard with pixy data.
    SmartDashboard.putNumber("Krabcoder", m_intake.intakeLR.getSensorCollection().getQuadraturePosition());

    //JT: Just in case, if for some reason we lose control this restarts the control command
    if (Robot.m_drivetrain.getCurrentCommand() == null) {
      Scheduler.getInstance().add(new DriveWithController());
    }

  
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
