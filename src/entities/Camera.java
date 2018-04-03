package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	private float speed;
	
	public Camera() {
		
		this.speed = 0.5f;
	
	}
	
	public void move() {
		
		yaw =  - (Display.getWidth() - Mouse.getX() / 2);
		pitch =  (Display.getHeight() / 2) - Mouse.getY();
		
		if (pitch >= 90)
		{
			
			pitch = 90;
			
		}
		else if (pitch <= -90)
		{
			
			pitch = -90;
			
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{

			position.z += -(float)Math.cos(Math.toRadians(yaw)) * speed;
			position.x += (float)Math.sin(Math.toRadians(yaw)) * speed;
			
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.z -= -(float)Math.cos(Math.toRadians(yaw)) * speed;
			position.x -= (float)Math.sin(Math.toRadians(yaw)) * speed;


		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			
			position.z += (float)Math.sin(Math.toRadians(yaw)) * speed;
			position.x += (float)Math.cos(Math.toRadians(yaw)) * speed;

		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			
			position.z -= (float)Math.sin(Math.toRadians(yaw)) * speed;
			position.x -= (float)Math.cos(Math.toRadians(yaw)) * speed;

		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			position.y += speed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y -= speed;
		}
		/*
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z-=0.1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z+=0.1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x+=0.1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x-=0.1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			position.y+=0.1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y-=0.1f;
		}
		*/
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
