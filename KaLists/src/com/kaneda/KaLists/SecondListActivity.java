package com.kaneda.KaLists;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * SA41 Androidサンプル　Chapter04 Ex01 データベース
 *
 * 第一画面表示用アクティビティクラス
 * お寺リストを表示する
 * @author kaneda
*/

public class SecondListActivity extends Activity {

	/*
	 * 球団リスト画面で選択された球団名
	 */
	private String _selectedgroup = "";
	private Context _context = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);

		ListView lvTemple = (ListView) findViewById(R.id.lv_temple);

		List<String> templeList = createPrefectureList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,templeList);

		lvTemple.setAdapter(adapter);
		lvTemple.setOnItemClickListener(new  ListItemClickListener());
	}


	/**
	 * 選手名を生成するメソッド
	 * @return 選手名リストオブジェクト
	 */
	private  List<String> createPrefectureList(){

				Intent intent = getIntent();
				Bundle extras = intent.getExtras();
				List<String> playerList = new ArrayList<String>();
				String pnumber  ="";
				String name ="";


				if(extras != null){
					_selectedgroup = extras.getString("selectedgroup");
				}

				//DBに接続
				DatabaseHelper helper = new DatabaseHelper(this);
				SQLiteDatabase db = helper.getWritableDatabase();
				//カーソルの初期化
				Cursor cursor = null;

				String sql = "SELECT * FROM player WHERE pgroup = '" + _selectedgroup + "'";

				db.beginTransaction();
				try{
					cursor = db.rawQuery(sql, null);
					db.setTransactionSuccessful();

					//ぐるぐるまわるーデータはいるー

					while(cursor != null && cursor.moveToNext()){
						/*
						 * 所属選手名のリストを作成する
						 * pnumber(背番号)
						 * name（選手名）
						 */


						//背番号の生成
						int idexContent = cursor.getColumnIndex("pnumber");
						pnumber = cursor.getString(idexContent);

						//選手名の生成
						idexContent = cursor.getColumnIndex("name");
						name = cursor.getString(idexContent);
						//リストに入力する
						playerList.add(pnumber+"　"+name);

					}
				}	catch(Exception ex){
					Log.e("ERROR",ex.toString());
				}finally{
					db.endTransaction();
					db.close();
				}
				return  playerList;
	}


	/**
	 * リストが選択されたときの処理が
	 * web画面へ遷移する
	 *
	 * @author kaneda
	 */

	private class ListItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?>  parent, View view, int position,long id){

			//選手名を取得
	        String player = "筒香嘉智";

	        //エンコード初期値
	        String enc ="";
	        //文字コードに変換する
	        try {
				 enc = URLEncoder.encode(player,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

	        //エンコードした選手名に検索ワード（スペース+野球選手）をつける
	        enc = enc + "%e3%80%80%e9%87%8e%e7%90%83%e9%81%b8%e6%89%8b";

	        //yahooサーチの検索結合する
	        String yahoourl ="http://search.yahoo.co.jp/search?p="+enc;


	    	//メインスレッドからのインターネット接続を可能にするための１文
			//ただし、暫定的に使用する。本来は別スレッドを作成して、別スレッドよりインターネット接続すること
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

			//webページに接続
			Intent newIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(httpGet(yahoourl)));
			startActivity(newIntent);

	    }
	}

    public static String httpGet(String strURL){

    	//抽出条件
    	String wiki = "<span class=\"u\">ja.wikipedia.org/wiki/";
    	//wikiURL
    	 int index = 0;

      //(1)トライキャッチによるエラー処理
     try{
        //(2)URLクラスを使って通信
        URL url=new URL(strURL);
        URLConnection connection=url.openConnection();
       //(3)動作を入力に設定
        connection.setDoInput(true);    //データを入力することの宣言
        InputStream stream=connection.getInputStream();   //(4)入力ストリームを取得
       //(5)得られた入力ストリームをバッファリーダ(input）を使って読み出していく（ための設定）
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
       //(6)データの取得処理
        String data="";
        String tmp="";
        while((tmp=input.readLine()) !=null){     //入力ストリームから1行ずつ読み込み
        	if(tmp.indexOf(wiki) != -1){
        		data=tmp+"\r\n";     //dataに追加（バッファが空になるまで）
        		/**********アンカー部分の切り抜き処理**********/

        		//1. 指定した文字より前の部分を削除
        	    index = data.indexOf(wiki);
        	    data = data.substring(index);

        	    //2.指定した文字より後ろ部分を削除
        	     index = data.indexOf("</span>");
        	     data = data.substring(0,index);
        	    //3.余分な部分の切り抜き（<b>）と（<span>）とを削除
        	     data = data.replace("<b>", "");
        	     data = data.replace("</b>", "");
        	     data = data.replace("<span class=\"u\">", "");
        	     data = "http://" + data;
        	}
        }
       //(7)終了処理
         stream.close();
         input.close();

         return data;         //dataを返却

     }catch (Exception  e){
         //(8)エラー処理
         return e.toString();
        }
   }
	}
