package org.usfirst.frc.team2830.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Turn implements Step{
	
	Robot caller;
	
	double driveAmount;
	
	double power;
	
	public Turn(Robot caller, double driveAmount,double power){
		this.caller = caller;
		this.driveAmount = driveAmount;
		this.power = power;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		caller.robotDrive.mecanumDrive_Cartesian(0,0,0,0);
		caller.strafingGyro.reset();
		
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub
		
			caller.robotDrive.mecanumDrive_Cartesian(0,0,this.power,0);
		
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		
		if (Math.abs(caller.strafingGyro.getAngle()) > driveAmount){
			
			return true;

		}
		return false;
		}
	

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		caller.robotDrive.mecanumDrive_Cartesian(0,0,0,0);

	}

}
