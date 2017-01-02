package com.talengu.wordwarrior.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqlHelper {

	public static String WORDLIST_TABLE_TMPWORD = "TmpWord";
	public static String WORDLIST_TABLE_MYWORD = "MyWord";
	public static final String DB_PATH="data/data/com.talengu.wordwarrior/databases";
	public static final String DB_NAME = DB_PATH+"/wordroid.db";

	public void Insert(Context context, String table, ContentValues values) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		try {
			db.insert(table, null, values);
			Log.i("wordroid=", "Insert");
		} catch (Exception e) {
			e.getStackTrace();
		}
		db.close();
	}

	public void CreateTable(Context context, String table) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		String sql = "CREATE TABLE " + table
				+ " ( ID text not null, SPELLING text not null , MEANNING text not null, PHONETIC_ALPHABET text, LIST text not null" + ");";
		try {
			db.execSQL(sql);
			Log.i("wordroid=", sql);
		} catch (Exception e) {
			e.getStackTrace();
		}
		db.close();
	}

	public void Update(Context context, String table, ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		try {
			db.update(table, values, whereClause, whereArgs);
			Log.i("wordroid=", "Update");
		} catch (Exception e) {
			e.getStackTrace();
		}
		db.close();
	}

	/**
	 *
	 * @param context
	 * @param table The table name to compile the query against.
	 * @param columns A list of which columns to return. Passing null will
	 *            return all columns, which is discouraged to prevent reading
	 *            data from storage that isn't going to be used.
	 * @param selection A filter declaring which rows to return, formatted as an
	 *            SQL WHERE clause (excluding the WHERE itself). Passing null
	 *            will return all rows for the given table.
	 * @param selectionArgs You may include ?s in selection, which will be
	 *         replaced by the values from selectionArgs, in order that they
	 *         appear in the selection. The values will be bound as Strings.
	 * @param groupBy A filter declaring how to group rows, formatted as an SQL
	 *            GROUP BY clause (excluding the GROUP BY itself). Passing null
	 *            will cause the rows to not be grouped.
	 * @param having A filter declare which row groups to include in the cursor,
	 *            if row grouping is being used, formatted as an SQL HAVING
	 *            clause (excluding the HAVING itself). Passing null will cause
	 *            all row groups to be included, and is required when row
	 *            grouping is not being used.
	 * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.

	 */
	public Cursor Query(Context context, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
						String orderBy) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		Cursor cursor = null;
		try {
			cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
			Log.i("wordroid=", "query");
			Log.i("countofcursor=", "" + cursor.getCount());
		} catch (Exception e) {
			e.getStackTrace();
		}
		db.close();
		return cursor;

	}

	public void Delete(Context context, String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		try {
			db.delete(table, whereClause, whereArgs);
			Log.i("wordroid=", "delete");
		} catch (Exception e) {
			e.getStackTrace();
		}
		db.close();
	}

	public void DeleteTable(Context context, String table) {

		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		String sql = "drop table " + table;
		try {
			db.execSQL(sql);
			Log.i("wordroid=", sql);
		} catch (Exception e) {
			e.getStackTrace();
		}
		db.close();
	}
}
