/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.subsystems.DriveTrain;
import frc.robot.Robot;
import frc.robot.OI;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotMap;

/**
 * This command gives basic drive controls to the user through a two-stick gamepad.
 */
public class DriveGTA extends Command {


  public DriveGTA() {
  // Use requires() here to declare subsystem dependencies
  requires(Robot.m_drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
      //JT: This is where the driver code is actually going to go!
      
      //speedCap sets a maximum speed
      //JT: Should we move speedCap to a central location?
      double speedCap = 0.8;
      
    
      //JT: Adding a deadzone is a good idea, but 0.5 is huge! That's half the range! What if we want to go slow?
      
      //Right trigger to go forward...
      if (Robot.m_oi.gamepad.getRawAxis(RobotMap.rightTrigger) >= 0.5)
      {
      //JT: This code is going to have trouble turning. The max value that can go into leftDrive/rightDrive is 1. If the trigger is already at 1 you're already moving at 0.8, so your turn can only add another 0.2
      Robot.m_drivetrain.leftDrive((Robot.m_oi.gamepad.getRawAxis(RobotMap.rightTrigger) + Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisY)) * speedCap);
      Robot.m_drivetrain.rightDrive((Robot.m_oi.gamepad.getRawAxis(RobotMap.rightTrigger) + Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisY)) * speedCap);
      }
    
      //Left trigger to go backward      
      else if (Robot.m_oi.gamepad.getRawAxis(RobotMap.leftTrigger) >= 0.5)
      {
      Robot.m_drivetrain.leftDrive((Robot.m_oi.gamepad.getRawAxis(RobotMap.leftTrigger) + Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisY)) * -speedCap);
      Robot.m_drivetrain.rightDrive((Robot.m_oi.gamepad.getRawAxis(RobotMap.leftTrigger) + Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisY)) * -speedCap);
      }
      //Stopping
      else
      {
        Robot.m_drivetrain.leftDrive(0);
        Robot.m_drivetrain.rightDrive(0);
      }
      //JT: As a code clean-up, why not put your turning code into the else statement above? It'll give you smoother controls
      //turning
      
      //JT: I see what you're doing here, but you could combine this into a single condition by using an OR. Or even better, you could get the absolute value with abs()

      if (Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisX) >= 0.5)
      {
        Robot.m_drivetrain.leftDrive(Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisX));
        Robot.m_drivetrain.rightDrive(-Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisX));
      }
      else if(Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisX) <= -0.5)
      {
        Robot.m_drivetrain.leftDrive(Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisX));
        Robot.m_drivetrain.rightDrive(-Robot.m_oi.gamepad.getRawAxis(RobotMap.rightStickAxisX));
      }
    }
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
