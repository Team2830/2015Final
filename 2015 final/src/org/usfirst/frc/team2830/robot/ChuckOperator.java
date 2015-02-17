package org.usfirst.frc.team2830.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class ChuckOperator implements Step
	{
	
	Robot caller;
	Timer clock= new Timer();
	static final int CLOSE = 0;
	static final int OPEN = 1;
	
	public ChuckOperator(Robot caller, int openOrClose)
	{
		this.caller=caller;
	}

	@Override
	public void start() {
		caller.chuck.set(DoubleSolenoid.Value.kForward);
		clock.reset();
		clock.start();
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFinished() {
		if (clock.get() > 1)
			
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