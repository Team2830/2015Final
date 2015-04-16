
package org.usfirst.frc.team2830.robot;

import java.sql.Driver;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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

	final int BURGLAR = 5;
	final int DRIVE_BACK = 4;
	final int ROBOT_LIFT_TOTECONTAINER=3;
	final int ROBOT_LIFT_CONTAINER = 2;
	final int ROBOT_LIFT_TOTE = 1;
	final int DO_NOTHING = 0;
	
	
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
	
	public DoubleSolenoid burglar;
	

	
	
	ChuckOperator oneToteChuckClose;
	ElevatingChuck oneToteLiftTote;
	Turn oneToteTurn90;
	DriveForward oneToteDrive115;
	ChuckOperator oneToteChuckOpen;
	
	ChuckOperator oneContainerChuckClose;
	ElevatingChuck oneContainerLiftContainer;
	Turn oneContainerTurn90;
	DriveForward oneContainerDrive115;
	ChuckOperator oneContainerChuckOpen;
	
	
	ChuckOperator oneCTChuckClose1;
	ElevatingChuck oneCTChuckup1;
	Turn oneCTTurn55;
	StrafingClass oneCTStrafe3;
	DriveForward oneCTDrive4;
	ElevatingChuck oneCTChuckDown;
	ElevatingChuck oneCTChuckDown1;
	ChuckOperator oneCTChuckClose2;
	ElevatingChuck oneCTChuckup2;
	ChuckOperator oneCTChuckClose3;
	Turn oneCTTurn90;
	DriveForward oneCTDrive115;
	
	
	DriveForward justBackwards;
	Turn JBTurn90;

	DriveForward burglarDrive50;
	BurglarArm burglarExtendArm;
	Turn burglarTurn90;
	
	PowerDistributionPanel pdp; 
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
			strafingGyro.reset();
			
			new SmartDashboard();
			frontLeftEncoder = new Encoder(0,1);
			frontRightEncoder = new Encoder(2,3);
			rearLeftEncoder = new Encoder(4,5);
			rearRightEncoder = new Encoder(6,7);
			LiveWindow.addSensor("Drive Train", "Front Left Encoder", frontLeftEncoder);
			LiveWindow.addSensor("Drive Train", "Front Right Encoder", frontRightEncoder);
			LiveWindow.addSensor("Drive Train", "Rear Left Encoder", rearLeftEncoder);
			LiveWindow.addSensor("Drive Train", "Rear Right Encoder", rearRightEncoder);
			LiveWindow.addSensor("Drive Train", "Gyro", strafingGyro);
			pdp = new PowerDistributionPanel();
			//pdp.startLiveWindowMode();
			
			/*frontLeftEncoder.setDistancePerPulse((6*Math.PI*Math.sin(Math.PI/4))/360);
			frontRightEncoder.setDistancePerPulse((6*Math.PI*Math.sin(Math.PI/4))/360);
			rearLeftEncoder.setDistancePerPulse((6*Math.PI*Math.sin(Math.PI/4))/360);
			rearRightEncoder.setDistancePerPulse((6*Math.PI*Math.sin(Math.PI/4))/360);*/
			
			SmartDashboard.putNumber("Gyro Correction", -0.15);
			SmartDashboard.putNumber("Gyro Setting", strafingGyro.getAngle());
			
			strafingGyro.setSensitivity(.007);
		
			elevatorTalon = new CANTalon(1);
			elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
			chuck = new DoubleSolenoid( 0, 1);
			SmartDashboard.putNumber("Autonomous", this.DO_NOTHING);
			
			SmartDashboard.putString("Task", "Javion D. Mosley");
			
			elevatorTalon.ConfigFwdLimitSwitchNormallyOpen (true);
			elevatorTalon.ConfigRevLimitSwitchNormallyOpen (true);
			
			burglar = new DoubleSolenoid(2,3);
     }

    public void autonomousInit()
    
    {
    	SmartDashboard.putNumber("frontLeftEncoder", frontLeftEncoder.get());
    	SmartDashboard.putNumber("frontRightEncoder", frontRightEncoder.get());
    	SmartDashboard.putNumber("rearLeftEncoder", rearLeftEncoder.get());
    	SmartDashboard.putNumber("rearRightEncoder", rearRightEncoder.get());    	
    	
    	robotDrive.setSafetyEnabled(false);
    	
    	oneToteChuckClose= new ChuckOperator(this, ChuckOperator.CLOSE);
    	oneToteLiftTote= new ElevatingChuck(this, 2, .2);
    	oneToteTurn90 = new Turn(this, 90., .2);
    	oneToteDrive115 = new DriveForward(this, 115, .6);
    	oneToteChuckOpen = new ChuckOperator (this, ChuckOperator.OPEN);
    
    	oneContainerChuckClose = new ChuckOperator(this, ChuckOperator.CLOSE);
    	oneContainerLiftContainer = new ElevatingChuck(this, .5, .1);
    	oneContainerDrive115 = new DriveForward(this, 120, -.8);
    	
    	
    	oneCTChuckClose1= new ChuckOperator (this, ChuckOperator.CLOSE);
    	oneCTChuckup1= new ElevatingChuck(this, .8, 1);
    	oneCTTurn55= new Turn(this, 40, -.5);
    	oneCTStrafe3= new StrafingClass (this, 10, .6);
    	oneCTDrive4= new DriveForward (this, 4, .8);
    	oneCTChuckDown= new ElevatingChuck(this, .1, -1);
    	oneCTChuckClose3 = new ChuckOperator (this, ChuckOperator.OPEN);
    	oneCTChuckDown1= new ElevatingChuck(this, .3, -1);
    	oneCTChuckClose2= new ChuckOperator (this, ChuckOperator.CLOSE);
     	oneCTChuckup2= new ElevatingChuck(this, .4, 1);
   	    
    	oneCTTurn90 = new Turn(this, 90,-.4);
   	    oneCTDrive115 = new DriveForward (this, 115,.7);
    	
    	justBackwards = new DriveForward (this, 55, -.7);
    	JBTurn90 = new Turn(this, 90,.6);
    	
    	burglarDrive50 = new DriveForward (this, 50, -.5);
    	burglarExtendArm = new BurglarArm (this, BurglarArm.OPEN);
    	burglarTurn90= new Turn (this, 90, .6);
    	
    	
   mode= (int) SmartDashboard.getNumber("Autonomous");
   SmartDashboard.putNumber("Gyro", strafingGyro.getAngle());
   
   stepNum = 0;
   lastStep = -1;	   
   
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	SmartDashboard.putNumber("Gyro", strafingGyro.getAngle());
    	
    	SmartDashboard.putNumber("frontLeftEncoder", frontLeftEncoder.get());
    	SmartDashboard.putNumber("frontRightEncoder", frontRightEncoder.get());
    	SmartDashboard.putNumber("rearLeftEncoder", rearLeftEncoder.get());
    	SmartDashboard.putNumber("rearRightEncoder", rearRightEncoder.get());
    	SmartDashboard.putNumber("step", stepNum);
    	
    	switch(mode)
    	{
    	case DRIVE_BACK:
    		switch (stepNum)
    		{
    		case 0: currentStep= justBackwards;
    		System.out.println("Driving Back");
    			break;
    		case 1:currentStep= JBTurn90;
    			break;
    		
    		default: currentStep= null;
    		}
    		break;
    	
    		case ROBOT_LIFT_TOTE:
    			switch(stepNum)
    			{
    			case 0:
    				currentStep= oneToteChuckClose;
    				break;
    			case 1:
    				currentStep= oneToteLiftTote;
    				break;
    			case 2:
    				currentStep= oneToteTurn90;
    				break;
    			case 3:
    				currentStep= oneToteDrive115;
    				break;
    			default:
    				currentStep = null;
    			
    			}
    			break;
    		case ROBOT_LIFT_CONTAINER:
    			switch(stepNum)
    			{
    			case 0:
    				currentStep= oneContainerChuckClose;
    				break;
    			case 1:
    				currentStep= oneContainerLiftContainer;
    				break;
    			
    			case 2:
    				currentStep= oneContainerDrive115;
    				break;
    			default:
    				currentStep = null;
    				
    			}
    			break;
   		case ROBOT_LIFT_TOTECONTAINER:
    			switch(stepNum)
    			{
    			case 0:
    				currentStep= oneCTChuckClose1;
    				break;
    			case 1:
    				currentStep= oneCTChuckup1 ;
    				break;
    			case 2:
    				currentStep= oneCTTurn55;;
    				break;
    			case 3:
    				currentStep= oneCTStrafe3;
    				break;
    			case 4:
    				currentStep= oneCTDrive4;
    				break;
    			case 5:
    				currentStep= oneCTChuckClose3;
    				break;
    			case 6:
    				currentStep= oneCTChuckDown;
    				break;
    			case 7:
    				currentStep= oneCTChuckDown;
    				break;
    			case 8: 
    				currentStep= oneCTChuckClose2;
    				break;
    			case 9:
    				currentStep= oneCTChuckClose2;
    				break;
    			case 10:
    				currentStep= oneCTChuckup2;
    				break;
    			case 11:
    				currentStep= oneCTTurn90;
    				break;
    			case 12:
    				currentStep= oneCTDrive115;
    				break;
    			default:
    				currentStep = null;
    			}
    			break;
   		case BURGLAR:
			switch(stepNum)
			{
			case 0:
				currentStep= burglarDrive50;
				break;
			case 1:
				currentStep= burglarExtendArm;
				break;
			case 2:
				currentStep= burglarTurn90;
				break;

			default:
				currentStep = null;
			
			}    	}
    	
    	System.out.println(currentStep);
    	
    	if(currentStep != null)
    	{
    		if(lastStep!=stepNum)
    		{
    			currentStep.start();
    		}
    		lastStep= stepNum;

    		currentStep.excecute();

    		if(currentStep.isFinished())
    		{
    			System.out.println("step;"+stepNum + "time;"+Timer.getFPGATimestamp());
    	    	
    			currentStep.kill();
    			stepNum ++;
    		}
    	}
    	 
        }
    final boolean ELEVATOR_ANALOG_INVERTER = false;
    boolean robotCentric= true;
    boolean holdHeading= false;
    boolean lastIsTurning = true;
    final double CORRECTION_RATE = .1;
    final double DEADBAND = .4;
    double rotatingSpeed;
    final double GYRO_DEADBAND = 2;
    double setPoint = 0;
    double gyroCorrection = -.15;
    boolean lastButton2=true;
    boolean lastButton3=true;
    double angleToFeed;
	

    /**
     * all the teleop functions are initialized 
     */
    public void teleopInit()
    {
    	robotDrive.setSafetyEnabled(true);
    }
    /**```````````````````````````
     *  everything that runs during teleop
     */
    public void teleopPeriodic()
    {
    	SmartDashboard.putNumber("Current", pdp.getCurrent(12));
    	
    
    	if (driverStick.getRawButton(4))
    	{
    		strafingGyro.reset();
    	}
    	else 
    		
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
    	
    	if (operatorStick.getRawButton(5))
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
    	LiveWindow.run();
    
    }
    public void drive () {
    	
    
    	SmartDashboard.putNumber("frontLeftEncoder", frontLeftEncoder.get());
    	SmartDashboard.putNumber("frontRightEncoder", frontRightEncoder.get());
    	SmartDashboard.putNumber("rearLeftEncoder", rearLeftEncoder.get());
    	SmartDashboard.putNumber("rearRightEncoder", rearRightEncoder.get());

    	boolean isTurning = Math.abs(driverStick.getTwist()) > DEADBAND;
    	gyroCorrection = SmartDashboard.getNumber("Gyro Correction");

    	
		if (driverStick.getRawButton(2) && !lastButton2)
		{
			//robotDrive.mecanumDrive_Cartesian( driverStick.getAxis(Joystick.AxisType.kX),driverStick.getAxis(Joystick.AxisType.kY),driverStick.getAxis(Joystick.AxisType.kZ), 0);   

			robotCentric = !robotCentric;

		}
		lastButton2=driverStick.getRawButton(2);

		if(driverStick.getRawButton(3) && !lastButton3)
		{
			holdHeading=!holdHeading;

		}
		lastButton3=driverStick.getRawButton(3);
		
    	
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
    		{
    			rotatingSpeed = -gyroCorrection;
    			System.out.println("E");

    		}


    	}

    	}
    	SmartDashboard.putNumber("angle", strafingGyro.getAngle());
    	SmartDashboard.putBoolean("robot", robotCentric);
    	SmartDashboard.putBoolean("hold", holdHeading);
    	
    	if(robotCentric)
    	{
    		angleToFeed = 0;
    	}
    	else{
    		angleToFeed= strafingGyro.getAngle();
    		
    	}
    	
    	if(!holdHeading)
    	{
    		rotatingSpeed= squareMaintainSign (driverStick.getAxis(Joystick.AxisType.kTwist));
    	}	
    	
    	//robotDrive.mecanumDrive_Cartesian( driverStick.getAxis(Joystick.AxisType.kX),driverStick.getAxis(Joystick.AxisType.kY),rotatingSpeed, angleToFeed);
    	SmartDashboard.putNumber("Angle To Feed", angleToFeed);
    	SmartDashboard.putNumber("rotatingSpeed",rotatingSpeed);
   	if (driverStick.getRawButton(1))
    	{
        	robotDrive.mecanumDrive_Cartesian( squareMaintainSign (driverStick.getAxis(Joystick.AxisType.kX))*.3,squareMaintainSign (driverStick.getAxis(Joystick.AxisType.kY))*.3,rotatingSpeed*.3, angleToFeed);
    	}
    	else
    	{  
    		robotDrive.mecanumDrive_Cartesian ( squareMaintainSign (driverStick.getAxis(Joystick.AxisType.kX)),squareMaintainSign (driverStick.getAxis(Joystick.AxisType.kY)),rotatingSpeed, angleToFeed);
    	} 
   	}

    public double squareMaintainSign (double joystick){
    	if (joystick < 0)
    	{
    		
    		return joystick*joystick*-1;
    	}
    	else{
    		return joystick*joystick;
    	}
    }
}
