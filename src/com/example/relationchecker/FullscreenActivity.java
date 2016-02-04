

package com.example.relationchecker;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import com.example.relationchecker.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = false;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
		.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(
								android.R.integer.config_shortAnimTime);
					}
					controlsView
					.animate()
					.translationY(visible ? 0 : mControlsHeight)
					.setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE
							: View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.agree_button).setOnTouchListener(
				mDelayHideTouchListener);


		Button agreeButton=(Button) findViewById(R.id.agree_button);
		Button disagreeButton= (Button) findViewById(R.id.disagree_button);




		final TextView t= (TextView) contentView;

		final RelationsDatabase database = new RelationsDatabase();

		final LinkedList<Relation> queue= generateQuestionsList(database);

		//String textToShow=generateNewQuestion(database);
		String textToShow=generateQuestionFromQueue(queue,database);


		t.setText(textToShow);


		agreeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//String textToShow=generateNewQuestion(database);
				String textToShow=generateQuestionFromQueue(queue,database);

				t.setText(textToShow);

			}
		});
	}
	//MINE
	private String generateNewQuestion(RelationsDatabase database) {
		// TODO Auto-generated method stub
		Relation rel=database.getRandomRelation();

		String retVal = rel.getFirstTerm()+" "+rel.getSecondTerm()+" "+rel.getThirdTerm();
		saveToFile(database.getRelations());
		
		
		return retVal;
	}
	//MINE
	private LinkedList<Relation> generateQuestionsList(RelationsDatabase database){


		LinkedList<Relation> queue = database.getRelationsBelowConfidence(4);


		return queue;

	}
	//MINE
	private String generateQuestionFromQueue(LinkedList<Relation> queue , RelationsDatabase database){

		Relation rel=queue.poll();
		String retVal = "No more questions.";
		if(rel!=null){
			retVal = rel.getFirstTerm()+" "+rel.getSecondTerm()+" "+rel.getThirdTerm();

		}
		saveToFile(database.getRelations());
		
		openFile();
		Log.v("TEST", "Opening");
		return retVal;
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {

		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	//MINE
	
	private void saveToDB(){
		
	}
	
	
	private void saveToFile(ArrayList<Relation> arrayList){

		String filename = "relations.xml";

		FileOutputStream fos;       

		try {
			fos = openFileOutput(filename,Context.MODE_APPEND);



			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(fos, "UTF-8");
			serializer.startDocument(null, Boolean.valueOf(true));
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

			serializer.startTag(null, "root");

			int nbOfRelations = arrayList.size(); 
			for(int j = 0 ; j < nbOfRelations ; j++)
			{

				List<String> tagsList = Arrays.asList("firstTerm", "secondTerm", "thirdTerm", "weight", "confidence");
				Relation currentRelation=arrayList.get(j);

				serializer.startTag(null, "record");

				serializer.startTag(null, "firstTerm");
				String data=currentRelation.getFirstTerm();


				serializer.text(data);

				serializer.endTag(null, "firstTerm");


				serializer.startTag(null, "secondTerm");
				data=currentRelation.getSecondTerm();


				serializer.text(data);

				serializer.endTag(null, "secondTerm");


				serializer.startTag(null, "thirdTerm");
				data=currentRelation.getThirdTerm();


				serializer.text(data);

				serializer.endTag(null, "thirdTerm");


				serializer.startTag(null, "weight");
				data= String.valueOf(currentRelation.getWeight());


				serializer.text(data);

				serializer.endTag(null, "weight");

				serializer.startTag(null, "confidence");
				data= String.valueOf(currentRelation.getConfidence());


				serializer.text(data);

				serializer.endTag(null, "confidence");

				serializer.endTag(null, "record");
			}
			serializer.endTag(null, "root");

			serializer.endDocument();

			serializer.flush();

			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openFile(){
		String filename = "relations.xml";

		FileInputStream fis = null;
		InputStreamReader isr = null;

		try {
			fis = openFileInput(filename);

			isr = new InputStreamReader(fis);
			char[] inputBuffer = new char[fis.available()];
			isr.read(inputBuffer);
			String data = new String(inputBuffer);
			isr.close();
			fis.close();

			
			//Log.v("TEST", data);
			
			/*
			 * converting the String data to XML format
			 * so that the DOM parser understand it as an XML input.
			 */
			InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));


			DocumentBuilderFactory dbf;
			DocumentBuilder db;
			NodeList items = null;
			Document dom;

			
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			dom = db.parse(is);
			// normalize the document
			dom.getDocumentElement().normalize();

			
			ArrayList<String> list = new ArrayList<String>();
			items = dom.getElementsByTagName("record");
			/*
			for(int i=0;i<items.getLength();i++){
				Node currentItem=items.item(i);
				
				Node currentChild=currentItem.getFirstChild();
				Log.v("DEBUGA",currentChild.getNodeValue());
				while (currentChild!=null){
					
					Log.v("DEBUGB",currentChild.getNodeValue());
					currentChild=currentChild.getNextSibling();
				}
				
			}
			
			*/
			
			
			ArrayList<String> arr = new ArrayList<String>();

			for (int i=0;i<items.getLength();i++){

				Node item = items.item(i);

				arr.add(item.getNodeValue());

			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}