package org.usfirst.frc.team2830.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Turn180 implements Step{
	
	Robot caller;
	
	public Turn180(Robot caller){
		this.caller = caller;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		caller.robotDrive.mecanumDrive_Cartesian(0,0,0,0);
		caller.strafingGyro;
    
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		caller.strafingGyro.reset();
		if (caller.strafingGyro = 0)
		{
			robotDrive.mecanumDrive_Cartesian(0.7,0,0,0);
		}
		else robotDrive.mecanumDrive_Cartesian(0,0,0,0);
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
		if (strafingGyro = 180){
			robotDrive.mecanumDrive_Cartesian(0,0,0,180);
		}
		else
			robotDrive.mecanumDrive_Cartesian(0.7,0,0,0);
		}
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}

}
