package com.puz.salestakingorder.activities;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.puz.salestakingorder.R;
import com.puz.salestakingorder.adapters.AttributeMultipleSearchAdapter;
import com.puz.salestakingorder.adapters.AttributeSearchAdapter;
import com.puz.salestakingorder.datasources.AttributeDataSource;
import com.puz.salestakingorder.models.AttributesItem;

public class AttributeSearchActivity extends Activity implements OnClickListener{
	AttributeDataSource dataSource;
	ListView listViewItems;
	ArrayList<AttributesItem> items;
	AttributeSearchAdapter adapter;
	AttributeMultipleSearchAdapter multiAdapter;
	Button buttonOk,buttonCancel;
	String attribute_name;
	String attribute_type;
	private ArrayList<AttributesItem> selectedItems;
    private long ids ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_search);
		/*
		 * parameter passing dari activity sebelumnya
		 * attribute_name: tipe dari attribute yang dicari
		 * attribute_type: single choice atau multipe choice
		 */


		Intent intent=getIntent();
		selectedItems=new ArrayList<AttributesItem>();
		attribute_name=intent.getStringExtra("attribute_name");
		attribute_type=intent.getStringExtra("attribute_type");


		getActionBar().setTitle("Search "+attribute_name);
		
		listViewItems=(ListView)findViewById(R.id.listViewItems);
		buttonCancel=(Button)findViewById(R.id.buttonCancel);
		buttonOk=(Button)findViewById(R.id.buttonOK);
		
		/*
		 * init datasource
		 */
		dataSource=new AttributeDataSource(getApplicationContext());
		dataSource.open();
		/*
		 * add attribute to the list
		 */
		items=new ArrayList<AttributesItem>();
		items.addAll(dataSource.getAttributeByType(attribute_name));
		/*
		 * jika single choice assign ke single choice adapter
		 */
		if(attribute_type.equals("single")){
			adapter=new AttributeSearchAdapter(getApplicationContext(), items);
			listViewItems.setAdapter(adapter);

            ids = intent.getLongExtra("selected", 0);

            Log.i("idsingle1:",ids+"");

            if (ids!=0)
                adapter.setSelected(ids);


            /*
			 * jika multi choice assign ke multichoice adapter
			 */
		}else{
			multiAdapter=new AttributeMultipleSearchAdapter(getApplicationContext(), items);
			listViewItems.setAdapter(multiAdapter);

            String idds = intent.getStringExtra("selected");
            if(!idds.equals("")){
                for (int i = 0; i < items.size(); i++) {
                    AttributesItem it = items.get(i);
                    if(idds.indexOf(it.getId()+"")>-1) {
                        multiAdapter.toggleSelection(i);
                    }
                }

            }

		}
		listViewItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemSelect(position);
			}
		});
		buttonOk.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
	}

	/*
	 * melakukan select pada salah satu item,
	 * set selected item pada adapter
	 */
	private void onListItemSelect(int position) {
		if(position != -1){
			Log.i("position", position+"");
//			Log.i("id", adapter.getItemId(position)+"");
			if(attribute_type.equals("single")){
				adapter.setSelected(adapter.getItemId(position));

			}
            else{
		        multiAdapter.toggleSelection(position);
			}
		}
    }	
	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attribute_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		/*
		 * jika button cancel ditekan, maka tutup activity dan set result canceled ke activity sebelumnya
		 */
		if(v==buttonCancel){
			setResult(Activity.RESULT_CANCELED);
			finish();
			/*
			 * jika button ok ditekan, maka tutup activity dan set result ok ke activity sebelumnya
			 * kembalikan ID dari item yang dipilih
			 */
		}else if(v==buttonOk){
            Intent resultIntent=new Intent();
           // Intent resultIntent=new Intent();
			if(attribute_type.equals("single")){
				long id=adapter.getSelected();
				resultIntent.putExtra("id", id);
			}else{
				SparseBooleanArray selected = multiAdapter.getSelectedIds();
				long[] ids = new long[selected.size()];
                String[] idName= new String[selected.size()];
                for (int i = 0; i<selected.size(); i++) {
                    if (selected.valueAt(i)) {
                        AttributesItem selectedItem = (AttributesItem)multiAdapter.getItem(selected.keyAt(i));
                    	ids[i]=selectedItem.getId();
                        idName[i]=selectedItem.getName();
                    }
                }

			   resultIntent.putExtra("ids", ids);
                resultIntent.putExtra("idName", idName);
			}
			resultIntent.putExtra("attribute_name", attribute_name);
			resultIntent.putExtra("attribute_type", attribute_type);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();

		}
	}

}
