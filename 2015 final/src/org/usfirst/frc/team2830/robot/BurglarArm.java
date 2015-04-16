package org.usfirst.frc.team2830.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class BurglarArm implements Step
	{
	
	Robot caller;
	Timer clock= new Timer();
	static final int CLOSE = 2;
	static final int OPEN = 3;
	int openOrClose;
	
	public BurglarArm(Robot caller, int openOrClose)
	{
		this.caller=caller;
		this.openOrClose = openOrClose;
	}

	@Override
	public void start() {
		DoubleSolenoid.Value valueToSet = (openOrClose == CLOSE) ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse;
		
		caller.burglar.set(valueToSet);
		clock.reset();
		clock.start();
	}

	@Override
	public void excecute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFinished() {
		if (clock.get() > .5)
			
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