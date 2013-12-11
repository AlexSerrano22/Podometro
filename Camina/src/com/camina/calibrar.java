package com.camina;

import java.util.List;
import java.util.Locale;


import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.TextView;
import android.widget.Toast;

public class calibrar extends Activity implements OnClickListener, SensorEventListener,OnInitListener {
	boolean inicio=false;
	datos d = new datos();
	Intent pods;
	private TextToSpeech tts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);
		
		final View Button = findViewById(R.id.boton1);
		final View Button2 = findViewById(R.id.detener);
		tts = new TextToSpeech(this, this);
		Button2.setEnabled(false);
		pods = new Intent(this,MainActivity.class);   
		
		Button.setOnClickListener(new View.OnClickListener() {
        

			
            public void onClick(View arg0){
            	
            	
            	Button.setEnabled(false);
            	CountDownTimer timer = new CountDownTimer(4000, 1000) {
        			public void onTick(long millisUntilFinished) {
        				
        				((TextView) findViewById(R.id.te)).setText("segundos restantes:" + millisUntilFinished / 1000);
        				if (tts!=null) {
        					String text = ""+millisUntilFinished / 1000;
        					if (text!=null) {
        						if (!tts.isSpeaking()) {
        							tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        						}
        					}
        				}
        					
        				
        			}
        		 
        			@Override
        			public void onFinish() {
        				inicia = true;
        				inicio=true;
        				if (tts!=null) {
        					String text = "inicia ".toString();
        					if (text!=null) {
        						if (!tts.isSpeaking()) {
        							tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        						}
        					}
        				}
        				Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

        	            

                    	v.vibrate(300);
                    	Button2.setEnabled(true);
        		             
        			}
        		}.start();
			
            }
        });
		
		Button2.setOnClickListener(new View.OnClickListener() {
            

			
            public void onClick(View arg0){
            	Button2.setEnabled(false);
            	inicia=false;
            	
            }
            
		});
			
		
		
		
	}
	int seg=10;
	boolean calibrado = false;
	
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
	
	boolean inicia=false;
	
		
		
		
	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	
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
	
			return cont;
		}
		
		private float X = 0, Y = 0, Z = 0;
		float []xy = new float[1200];
		int pasos =0;
		boolean b2=false;
		float dsG;
		Bundle bundle = new Bundle();
		
		@Override
		public void onSensorChanged(SensorEvent event) {
		
	        synchronized (this) {
	        	if(inicia==true){
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
			                
			            
			                	
		        					
			                				
	        	}else{
	        		if(inicio==true){
	        			
	        			mediax = (float) media(fx,fy,0,u-115); 
	                	
	                	dsG = (float) DS(fx,fy,0,u-115,mediax);
	                	
	                	dsx= dsG/10;
	                	
	                	pasos= flatp(fx,fy,0,u-115,dsx);
	                	((TextView) findViewById(R.id.te)).setText("pasos:" + pasos);
	                	inicio=false;
	                	if(pasos>8 && pasos <=10){
	                		d.actualizar(dsx);
	                		
	                		pods.putExtra("ds", dsx);
	                		if (tts!=null) {
	        					String text = "diste ss"+pasos+"con Sensibilidad de "+p;
	        					if (text!=null) {
	        						if (!tts.isSpeaking()) {
	        							tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	        						}
	        					}
	        				}
	                		
	                		startActivity(pods);
	                	}else{
	                		
	                		b2=true;	
	                		
	                		
	                	}
	                	
	        		}else{
	        			if(b2==true){
	        			
	        			if(p<100){
	        				p++;
                			dsx+=dsG/10;
                			pasos= flatp(fx,fy,0,u-115,dsx);
                			
                		
                		if(pasos >10){
                			Toast.makeText(this, ""+pasos, 1);
	                		

                		}else{
                			d.actualizar(dsx);
                			pods.putExtra("ds", dsx);
                			b2=false;
                			//pasos= flatp(fx,fy,0,u-115,dsx);
                			if (tts!=null) {
	        					String text = "distesss"+pasos+"con Sensibilidad de "+p;
	    	                	((TextView) findViewById(R.id.te)).setText("pasos:" + pasos);

	        					if (text!=null) {
	        						if (!tts.isSpeaking()) {
	        							tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	        						}
	        					}
	        				}
                			startActivity(pods);
                		}
	        			}else{
	        				b2=false;
	        				if (tts!=null) {
	        					String text = "la cagas wey vuelvelo a hacer ";
	        					if (text!=null) {
	        						if (!tts.isSpeaking()) {
	        							tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	        						}
	        					}
	        				}

                		}
	        				
	        				
	        			}
	        			
	        			
	        		}
	        		
	        	}
	        	
	        	
	        }
		}
		int p=0;
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onInit(int code) {
			if (code==TextToSpeech.SUCCESS) {
				Locale locSpanish = new Locale("es", "MX");
				tts.setLanguage(locSpanish);
				//tts.setLanguage(Locale.getDefault());
			} else {
				tts = null;
				Toast.makeText(this, "Failed to initialize TTS engine.", Toast.LENGTH_SHORT).show();
			}
			
		}

	

}
