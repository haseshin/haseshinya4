package com.kaneda.KaLists;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * SA41 Androidサンプル　Chapter04 Ex01 データベース
 *
 * 第一画面表示用アクティビティクラス
 * 球団リストを表示する *
 * @author ohs05032
*/

public class TempleListActivity extends Activity implements OnClickListener {
	 private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temple_list);
    	Button speakButton = (Button) findViewById(R.id.button1);
		ListView lvTemple = (ListView) findViewById(R.id.listView1);
		   speakButton.setOnClickListener(this);
		List<String> templeList = createPrefectureList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,templeList);

		lvTemple.setAdapter(adapter);
		lvTemple.setOnItemClickListener(new  ListItemClickListener());
	}



	 public void onClick(View v) {
	        	setContentView(R.layout.activity_temple_list);


		          PackageManager pm = getPackageManager();
		          List<ResolveInfo> activities = pm.queryIntentActivities(
		          new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	            startVoiceRecognitionActivity();
	 }
	    private void startVoiceRecognitionActivity() {
	        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.JAPAN.toString());
	        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
	        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	    }


	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
	    	String resultsString="";
	        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	        resultsString=results.get(0);

			Intent intent = new Intent(TempleListActivity.this,ThirdListActivity.class);
			intent.putExtra("resultString", resultsString);
			startActivity(intent);

	        }

	        super.onActivityResult(requestCode, resultCode, data);
	        }







	/**
	 *球団を生成するメソッド
	 * @return 球団リストオブジェクト
	 */
	private  List<String> createPrefectureList(){
				List<String> groupList = new ArrayList<String>();
				groupList.add("阪神タイガース");
				groupList.add("読売ジャイアンツ");
				return  groupList;
	}
	/**
	 * リストが選択されたときの処理が
	 * 第2画面へ処理を移管する
	 *
	 * @author kaneda
	 */

	private class ListItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?>  parent, View view, int position,long id){
			ListView listView = (ListView) parent;
			String group = (String) listView.getItemAtPosition(position);

			Intent intent = new Intent(TempleListActivity.this,SecondListActivity.class);
			intent.putExtra("selectedgroup", group);
			startActivity(intent);
		}
	}
}