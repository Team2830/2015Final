
package org.usfirst.frc.team2830.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public RobotDrive robotDrive;
	Joystick driverStick;
	Joystick operatorStick;
	
	final int ROBOT_LIFT_TOTE = 0;
	final int DO_NOTHING = 1;
	int mode = DO_NOTHING;
	Step currentStep;
	int stepNum = 0;
	int lastStep = -1; 
	
	
	
	public Gyro strafingGyro;
	
	Encoder frontLeftEncoder;
	Encoder frontRightEncoder;
	Encoder rearLeftEncoder;
	Encoder rearRightEncoder;
	
	public CANTalon elevatorTalon;
	
	// Channels for the wheels e e
	final int rearRightChannel	= 3;
	final int frontRightChannel	= 1;
	final int frontLeftChannel	= 0;
	final int rearLeftChannel	= 2;
	
	
	// channel for Gyro
	final int gyroChannel = 1;
	
	// The channel on the driver station that the joystick is connected to
	final int driverJoystickChannel	= 0;
	final int operatorJoystickChannel2  = 1;
	
	public DoubleSolenoid chuck;
	ChuckOperator oneToteChuckClose= new ChuckOperator(this, ChuckOperator.CLOSE);
	LiftingTote liftTote= new LiftingTote(this, 2, .2);
	Turn turn90 = new Turn(this, 90);
	DriveForward drive115 = new DriveForward(this, 115);
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
   
			robotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
			robotDrive.setExpiration(0.1);
			robotDrive.setInvertedMotor(MotorType.kFrontRight, true);	// invert the left side motors
			robotDrive.setInvertedMotor(MotorType.kRearRight, true);		// you may need to change or remove this to match your robot

			driverStick = new Joystick(driverJoystickChannel);
			operatorStick = new Joystick(operatorJoystickChannel2);
			
			strafingGyro = new Gyro(gyroChannel);
			
			new SmartDashboard();
			frontLeftEncoder = new Encoder(0,1);
			frontRightEncoder = new Encoder(2,3);
			rearLeftEncoder = new Encoder(4,5);
			rearRightEncoder = new Encoder(6,7);
			
			
			SmartDashboard.putNumber("Gyro Correction", 0.15);
			
			strafingGyro.setSensitivity(.007);
		
			elevatorTalon = new CANTalon(1);
			elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
			
			chuck = new DoubleSolenoid( 0, 1);
    }

    public void autonomousInit()
    {
   mode= (int) SmartDashboard.getNumber("Autonomous");
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    	switch(mode)
    	{
    		case ROBOT_LIFT_TOTE:
    			switch(stepNum)
    			{
    			case 0:
    				currentStep= oneToteChuckClose;
    				break;
    			case 1:
    				currentStep= liftTote;
    				break;
    			case 2:
    	
    				currentStep= turn90;
    				break;
    			case 3:
    				currentStep= drive115;
    				break;
    			}
    	}
    	if(lastStep!=stepNum)
    	{
    		currentStep.start();
    	}
    		lastStep= stepNum;
    		
    		currentStep.excecute();
    		
    		if(currentStep.isFinished())
    		{
    			currentStep.kill();
    			stepNum ++;
    		}
    }
    final boolean ELEVATOR_ANALOG_INVERTER = true;
    boolean robotCentric= true;
    boolean holdHeading= true;
    boolean lastIsTurning = true;
    final double CORRECTION_RATE = .1;
    final double DEADBAND = .2;
    double rotatingSpeed;
    final double GYRO_DEADBAND = 2;
    double setPoint = 0;
    double gyroCorrection = .15;
    boolean lastButton2=true;
    boolean lastButton3=true;
    /**
     * all the teleop functions are initialized 
     */
    public void teleopInit()
    {
    
    }
    /**
     *  everything that runs during teleop
     */
    public void teleopPeriodic()
    {
    	
    	drive();
        
    	double elevatorSpeed;
    	if (ELEVATOR_ANALOG_INVERTER)
    	{
    	elevatorSpeed = operatorStick.getAxis(Joystick.AxisType.kY)*-1;	
    	}
    	
    	else{
    		elevatorSpeed = operatorStick.getAxis(Joystick.AxisType.kY);
    	}
    	
    	elevatorTalon.set(elevatorSpeed);
    	
    	if (operatorStick.getRawButton(2))
    	{
    		chuck.set(DoubleSolenoid.Value.kForward);
    		
    	}else if(operatorStick.getRawButton(4))
    	{ 
    		chuck.set(DoubleSolenoid.Value.kReverse);}
    	
    	
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    public void drive () {
    	SmartDashboard.putNumber("frontLeftEncoder", frontLeftEncoder.get());
    	SmartDashboard.putNumber("frontRightEncoder", frontRightEncoder.get());
    	SmartDashboard.putNumber("rearLeftEncoder", rearLeftEncoder.get());
    	SmartDashboard.putNumber("rearRightEncoder", rearRightEncoder.get());
    	
        boolean isTurning = Math.abs(driverStick.getTwist()) > DEADBAND;
        gyroCorrection = SmartDashboard.getNumber("Gyro Correction");
        
        if (!isTurning && lastIsTurning){
        	setPoint = strafingGyro.getAngle();
        	rotatingSpeed=0;
        	System.out.println("A");
        	lastIsTurning = false;
        
        }else if(isTurning)
        {
        	rotatingSpeed=driverStick.getTwist();
        	System.out.println("B");
        	lastIsTurning = true;
        	
        }else
        {lastIsTurning = false;
        	if (Math.abs(strafingGyro.getAngle() - setPoint) < GYRO_DEADBAND)
        		
        	{
        		rotatingSpeed = 0;
        		System.out.println("C");
        		
        	}
        	else 
        	{
        	
        		if (strafingGyro.getAngle() - setPoint < 0)
        			{rotatingSpeed = gyroCorrection;
        			System.out.println("D");
        				
        		}
        		else
        			{rotatingSpeed = -gyroCorrection;
        			System.out.println("E");
        			
        		}
        		
        			if (driverStick.getRawButton(2) && !lastButton2)
        			{
        		        //robotDrive.mecanumDrive_Cartesian( driverStick.getAxis(Joystick.AxisType.kX),driverStick.getAxis(Joystick.AxisType.kY),driverStick.getAxis(Joystick.AxisType.kZ), 0);   
        				
        				robotCentric = !robotCentric;
        	
        			}
        			lastButton2=driverStick.getRawButton(2);
        			
        			if(driverStick.getRawButton(3) && lastButton3)
        			{
        				holdHeading=!holdHeading;
        				
        			}
        			lastButton3=driverStick.getRawButton(3);
        		
        	}
        	}
        
        robotDrive.mecanumDrive_Cartesian( driverStick.getAxis(Joystick.AxisType.kX),driverStick.getAxis(Joystick.AxisType.kY),rotatingSpeed, strafingGyro.getAngle());
    }
    
}
