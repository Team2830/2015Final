package org.usfirst.frc.team2830.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class LiftingTote implements Step {

	Robot caller;
	Timer clock= new Timer();
	
	public LiftingTote(Robot caller)
	{
		this.caller=caller;
	}

	@Override
	public void start() {
	
		caller.elevatorTalon.set(.2);
		clock.reset();

		
	}

	@Override
	public void excecute() {
		
		caller.elevatorTalon.set(.2);
		
		
	}

	@Override
	public boolean isFinished() {
		if (clock.get() > 2)
			
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
