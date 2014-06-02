package com.kaneda.KaLists;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * SA41 Androidサンプル　Chapter04 Ex01 データベース
 *
 * 第一画面表示用アクティビティクラス
 * 球団リストを表示する *
 * @author ohs05032
*/

public class TempleListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temple_list);

		ListView lvTemple = (ListView) findViewById(R.id.lv_temple);

		List<String> templeList = createPrefectureList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,templeList);

		lvTemple.setAdapter(adapter);
		lvTemple.setOnItemClickListener(new  ListItemClickListener());
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