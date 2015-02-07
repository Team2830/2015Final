package org.usfirst.frc.team2830.robot;

public interface Step {
	
	public void start();
	
	public void excecute();
	
	public boolean isFinished();
	
	public void kill();
}
