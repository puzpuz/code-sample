package com.puz.salestakingorder.datasources;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.puz.salestakingorder.models.AttributesItem;
import com.puz.salestakingorder.utils.MySQLiteHelper;

public class AttributeDataSource {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] column={
			MySQLiteHelper.ATTRIBUTE_ID,	
			MySQLiteHelper.ATTRIBUTE_NAME,
			MySQLiteHelper.ATTRIBUTE_STATUS,
			MySQLiteHelper.ATTRIBUTE_TYPE,
			MySQLiteHelper.ATTRIBUTE_DESCRIPTION};
	public AttributeDataSource(Context context){
		dbHelper=new MySQLiteHelper(context);
	}
	public void open() throws SQLException{
		database=dbHelper.getWritableDatabase();
	}
	public void close(){
		dbHelper.close();
	}
	public void createAttribute(
			long id,
			String name,
			String type,
			String status,
			String description
			){
		ContentValues values=new ContentValues();
		values.put(MySQLiteHelper.ATTRIBUTE_ID,id);
		values.put(MySQLiteHelper.ATTRIBUTE_NAME,name);
		values.put(MySQLiteHelper.ATTRIBUTE_TYPE,type);
		values.put(MySQLiteHelper.ATTRIBUTE_STATUS,status);
		values.put(MySQLiteHelper.ATTRIBUTE_DESCRIPTION,description);
		database.insert(MySQLiteHelper.TABLE_ATTRIBUTE, null, values);
	}
	public void deleteAttribute (AttributesItem attribute){
		long id=attribute.getId();
		database.delete(MySQLiteHelper.TABLE_ATTRIBUTE, MySQLiteHelper.ATTRIBUTE_ID+ "="+id, null);
	}
	public void deleteAllAttribute (){
		database.delete(MySQLiteHelper.TABLE_ATTRIBUTE, null, null);
	}
	public void editAttribute (long id,ContentValues values){
		database.update(MySQLiteHelper.TABLE_ATTRIBUTE, values, MySQLiteHelper.ATTRIBUTE_ID+"="+id, null);
	}	
	public ArrayList<AttributesItem> getAllAttribute(){
		ArrayList<AttributesItem> attribute=new ArrayList<AttributesItem>();
		Cursor cursor=database.query(MySQLiteHelper.TABLE_ATTRIBUTE,column,null,null,null,null,null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			AttributesItem attributeItem=cursorToAttribute(cursor);
			attribute.add(attributeItem);
			cursor.moveToNext();
		}
		cursor.close();
		return attribute;
	}
	public AttributesItem getAttributeById(long id){
		AttributesItem attribute=new AttributesItem();
		Cursor cursor=database.query(MySQLiteHelper.TABLE_ATTRIBUTE,column,
				MySQLiteHelper.ATTRIBUTE_ID+"="+id,null,null,null,null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			attribute=cursorToAttribute(cursor);
			cursor.moveToNext();
		}
		cursor.close();
		return attribute;
	}	
	public ArrayList<AttributesItem> getAttributeByType(String type){
		ArrayList<AttributesItem> attribute=new ArrayList<AttributesItem>();
		Cursor cursor=database.query(MySQLiteHelper.TABLE_ATTRIBUTE,column,
				MySQLiteHelper.ATTRIBUTE_TYPE+"='"+type+"'",null,null,null,null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			AttributesItem attributeItem=cursorToAttribute(cursor);
			attribute.add(attributeItem);
			cursor.moveToNext();
		}
		cursor.close();
		return attribute;
	}
	private AttributesItem cursorToAttribute(Cursor cursor){
		AttributesItem attributeItem=new AttributesItem();
		attributeItem.setId(cursor.getLong(0));
		attributeItem.setName(cursor.getString(1));
		attributeItem.setStatus(cursor.getString(2));
		attributeItem.setType(cursor.getString(3));
		attributeItem.setDescription(cursor.getString(3));
		return attributeItem;
	}
}