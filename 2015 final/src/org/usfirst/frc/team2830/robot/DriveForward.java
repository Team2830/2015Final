package org.usfirst.frc.team2830.robot;

public class DriveForward implements Step {
	
	Robot caller;
	
	double driveAmount;
	
	double drivePower;
	
	double setPoint;
	
	double rotatingSpeed;
	
	final double gyroCorrection = .15;
	
	final double GYRO_DEADBAND = 4;
	
	public DriveForward(Robot caller,double driveAmount, double drivePower)
	{
		this.caller = caller;
		this.driveAmount = driveAmount /(6*Math.PI*Math.sin(Math.PI/4)/360);
		this.drivePower = drivePower;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		caller.robotDrive.mecanumDrive_Cartesian(0,0,0,0);
		caller.frontLeftEncoder.reset();
		caller.frontRightEncoder.reset();
		caller.rearLeftEncoder.reset();
		caller.rearRightEncoder.reset();
		
		setPoint = caller.strafingGyro.getAngle();
		
	
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub
		
		
		if (Math.abs(caller.strafingGyro.getAngle() - setPoint) < GYRO_DEADBAND)

    	{
    		rotatingSpeed = 0;
    		System.out.println("C");
    	}
		
		else if (caller.strafingGyro.getAngle() - setPoint < 0)
		{rotatingSpeed = gyroCorrection;
		System.out.println("D");

		}
		else
		{
			rotatingSpeed = -gyroCorrection;
			System.out.println("E");

		}
		
		System.out.print("excute");
	
	caller.robotDrive.mecanumDrive_Cartesian(0,this.drivePower,rotatingSpeed,0);
	}
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		double distantDrive = caller.frontLeftEncoder.get() + caller.frontRightEncoder.get() + caller.rearLeftEncoder.get() + caller.rearRightEncoder.get();
		if (Math.abs(distantDrive / 4) > Math.abs(driveAmount)){
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
	
	public String toString()
	{
		return "Drive at " + this.drivePower + " for " + this.driveAmount;
	}

}
