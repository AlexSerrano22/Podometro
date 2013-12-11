package com.camina;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener{
	float d ;	
	//private long last_update = 0, last_movement = 0;
//	private float prevX = 0, prevY = 0, prevZ = 0;
	private float X = 0, Y = 0, Z = 0 ,X1 = 0, Y1 = 0, Z1 = 0 ;
	private static final double nbElements = 46;
	public int p;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		d = getIntent().getFloatExtra("ds",dsx);
		
		Log.d("valor", ""+d);
		
		
		
	}
	
	protected void onResume() {
	    super.onResume();
	    SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
	    List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);        
	    if (sensors.size() > 0) {
	        sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
	    }
	}
	
		
	protected void onStop() {
	    SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);        
	    sm.unregisterListener(this);
	    super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
public  boolean boll  = false;

final float alpha = (float) 0.8;
float[] gravity = new float[3];
float k = (float) 0.35;
float tx = 0,ty = 0,tz = 0;


	public float mediaMovilx(float V){
		
		return  tx=(float) (k * tx + (1.0 - k) * V);
		
	}
	
	
public float mediaMovily(float V){
		
		return  ty=(float) (k * ty + (1.0 - k) * V);
		
	}

public float mediaMovilz(float V){
	
	return  tz=(float) (k * tz + (1.0 - k) * V);
	
}

	//Sensor sensor = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	int w= 0,u=0;
	float[] fx = new float[2401];
	float[] fy = new float[2401];
	//float[] fz = new float[2401];
	
	
	long now = 0;
	long time = 0;
	int temp = 0;

	float mediax=0;
	float mediay=0;
	
	float dsx=0,dsy=0;
	
	public float[] suma(float []A ,float []B,int in ,int f){
		float[] Aux = new float[1200];
		for(int i=in; i<f; i++){
			if(in>1199){
			Aux[i-1200]= A[i]+B[i];
			}else{
				Aux[i]= A[i]+B[i];
			}
		}
		
		return Aux;
	}
	
	public float media(float []A ,float []B,int in ,int f){
		float media=0;
			for(int i=in; i<f; i++){
				media+= A[i];
				media+= B[i];
			}
			
		return media/1200;
	}
	
	public float DS(float []A ,float []B,int in ,int f,float media){
		float ds=0;
			for(int i=in; i<f; i++){
				ds += Math.pow(((A[i]+B[i])-media),2);
				
			}
			
			ds= ds/1199;
			
		return (float) Math.sqrt(ds);
	}
	
	public int flatp(float []A,float []B, int in, int f, float ds){
		boolean ban= false;
		boolean ban1= false;

		int cont=0;
		float aux =0;
		if(in<1199){
		for(int i=in; i<f; i++){	
			aux=A[i]+B[i];
			if(aux>= ds){
				ban=true;
			}
			if(ban == true && aux<= -ds ){
				cont++;
				ban=false;
			}
		}
		}else{
			for(int i=in; i<f; i++){	
				aux=A[i-1199]+B[i-1199];
				if(aux>= ds){
					ban=true;
				}
				if(ban == true && aux<= -ds ){
					cont++;
					ban=false;
				}
		}
		}
/*else{
			for(int i=in; i<f; i++){	
				aux=A[i-1200];
				if(ban == false ){
					if(ban1==true){
						
						if(aux>ds){
							ban1=false;
							ban = true;
							cont++;
						}
						
					}
					else{
						if(aux>ds){
							ban = true;
							}
					}
				}else{
					if(aux<-ds){
						cont++;
						ban=false;
						ban1=true;
						}
					}
				}
			
		}
		
		*/
		return cont;
	}
	
	
	
	float []xy = new float[1200];
	int pasos =0;
	@Override
	public void onSensorChanged(SensorEvent event) {
		long tS;
        synchronized (this) {
        	//long current_time = event.timestamp;
        	
        	
			tS = event.timestamp;

		///	((TextView) this.findViewById(R.id.axex)).setText("X : " + x);
			//((TextView) this.findViewById(R.id.axey)).setText("Y : " + y);
			//((TextView) this.findViewById(R.id.axez)).setText("Z : " + z);

			// Get the mean frequency for "nbElements" (=30) elements
			/*if (now != 0) {
				temp++;
				if (temp == nbElements) {
							    	
		        		*/
		    			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		            	gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		            	gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
		               
		                X = event.values[0] - gravity[0];
		                Y = event.values[1] - gravity[1];
		                Z = event.values[2] - gravity[2];
		                
		                X = mediaMovilx(X);  
		                Y = mediaMovily(Y);
		                Z = mediaMovilz(Z);
		                
		                fx[u]= X;
	                	fy[u]= Y;
	                	u++;
		                
		                if(u==1200 ){
		                	
		                	pasos+= flatp(fx,fy,0,1199,d);
		                	
		                }
		                if(u == 2400){
		                	//procesa 1200 a 2399
		                	u=0;
		                	
		                	
		                
		                	
		                	pasos+= flatp(fx,fy,1200,2399,d);
		                	
		                }
		                
				/*	time = tS - now;
					temp = 0;
					
					fx[u] = X;
					fy[u] = Y;
					//fz[u] = Z;
					
					u++;
					if(u > 24 && u<26){
						//prosesa 0 - 24
					}
					if(u > 48){
							u=0;
							//procesa 25 - 48
						
					}
				}
			}
			// To set up now on the first event and do not change it while we do not have "nbElements" events
			if (temp == 0) {
				now = tS;
			}
			*/
			

        	
        	
        	
           ((TextView) findViewById(R.id.txtAccX)).setText("X: " + X);
           ((TextView) findViewById(R.id.txtAccY)).setText("Y: " + Y);
           ((TextView) findViewById(R.id.txtAccZ)).setText("Z: " + Z);
           ((TextView) findViewById(R.id.txtAcc)).setText("time: " + u/50);
           ((TextView) findViewById(R.id.txtAc)).setText("DS: " + dsx);
           ((TextView) findViewById(R.id.valor2)).setText("pasos: " + pasos);


           
        }
		
	}  
	

}
