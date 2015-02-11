package org.usfirst.frc.team2830.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class ElevatingChuck implements Step {

	Robot caller;
	Timer clock= new Timer();
	
	double liftTime;
	double liftPower;

	
	public ElevatingChuck(Robot caller, double liftTime, double liftPower)
	{
		this.caller=caller;
		this.liftTime = liftTime;
		this.liftPower = liftPower;
	}

	@Override
	public void start() {
	
		caller.elevatorTalon.set(liftPower);
		clock.reset();

		
	}

	@Override
	public void excecute() {
		
		caller.elevatorTalon.set(liftPower);
		
		
	}

	@Override
	public boolean isFinished() {
		if (clock.get() > liftTime)
			
		{
			return true;
			
		}
		else
		{
			return false;
		}
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}

}
