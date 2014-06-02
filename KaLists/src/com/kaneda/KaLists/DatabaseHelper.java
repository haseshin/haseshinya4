package com.kaneda.KaLists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 野球選手名鑑
 * データベースのヘルパークラス
 *
 * @author kaneda
 */
public class DatabaseHelper extends SQLiteOpenHelper{
	/**
	 * データベースファイル名の定数フィールド
	 */
	private static final String DATABASE_NAME = "baseball.db";
	/**
	 * バージョン情報の定数フィールド
	 */
	private static final int DATABASE_VERSION =1;
	private static Context mContext = null;

	/**
	 * コンストラクタ
	 * @param context コンテキスト
	 */
	public DatabaseHelper(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){

		//トランザクションの開始
		db.beginTransaction();

		//クリエイト文の作成
		StringBuffer sb = new StringBuffer();

		//create文/insert文の取得
		InputStream is;
	    BufferedReader bfReader;
        try {
            is = mContext.getAssets().open("player.sql");
            bfReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while(( line = bfReader.readLine() ) != null){
                if(! line.equals("") ){
                    sb.append(line);
                    sb.append("\n");
                }
            }
            sb.deleteCharAt(sb.length()-1);
            for(String sql: sb.toString().split(";")){
                db.execSQL(sql);
            }
        } catch (IOException e) {
             e.printStackTrace();
    }
		//成功したとき
		db.setTransactionSuccessful();
		//トランザクションの終了
		db.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
	}
}