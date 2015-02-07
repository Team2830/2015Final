package org.usfirst.frc.team2830.robot;

public interface Step {
	
	public void Start();
	
	public void execute();
	
	public boolean isFinished();
	
	public void kill();
}
