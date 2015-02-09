package org.usfirst.frc.team2830.robot;

public class DriveForward implements Step {
	
	Robot caller;
	
	double driveAmount;
	
	public DriveForward(Robot caller,double driveAmount){
		this.caller = caller;
		this.driveAmount = driveAmount;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		caller.robotDrive.mecanumDrive_Cartesian(0,0,0,0);
		caller.frontLeftEncoder.reset();
		caller.frontRightEncoder.reset();
		caller.rearLeftEncoder.reset();
		caller.rearRightEncoder.reset();
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub
		caller.robotDrive.mecanumDrive_Cartesian(.6,0,0,0);
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		double distantDrive = caller.frontLeftEncoder.get() + caller.frontRightEncoder.get() + caller.rearLeftEncoder.get() + caller.rearRightEncoder.get();
		if (distantDrive > driveAmount){
			return true;
		}
		else 
			return false;
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		caller.robotDrive.mecanumDrive_Cartesian(0,0,0,0);
	}

}
