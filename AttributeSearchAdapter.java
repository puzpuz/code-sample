package com.puz.salestakingorder.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.puz.salestakingorder.R;
import com.puz.salestakingorder.models.AttributesItem;

public class AttributeSearchAdapter extends BaseAdapter implements Filterable{
    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<AttributesItem> attributeItems;
	private ArrayList<AttributesItem> filteredItems;
    private SparseBooleanArray mSelectedItemsIds;
    private long selectedId=-1;
    private int setPosition1= 22;
    public AttributeSearchAdapter(Context context, ArrayList<AttributesItem> items){
    	this.context=context;
    	this.attributeItems=items;
        this.filteredItems=items;
        mSelectedItemsIds=new SparseBooleanArray();
        mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return filteredItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return filteredItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return filteredItems.get(position).getId();
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_attribute_search_item, parent,false);
            holder=new ViewHolder();
            holder.textViewProductTitle=(TextView)convertView.findViewById(R.id.textViewTitle);
            holder.radioButtonSelectedProduct=(RadioButton)convertView.findViewById(R.id.radioButtonSelectedProduct);

            convertView.setTag(holder);
        }else{
        	holder=(ViewHolder)convertView.getTag();

        }
        holder.textViewProductTitle.setText(filteredItems.get(position).getName());
        holder.textViewProductTitle.setTag(filteredItems.get(position).getId());

        if(filteredItems.get(position).getId()  == selectedId) {
            holder.radioButtonSelectedProduct.setChecked(true);

        }
       else{
         holder.radioButtonSelectedProduct.setChecked(false);

    }


		return convertView;
	}
	private static class ViewHolder{
		RadioButton radioButtonSelectedProduct;
		TextView textViewProductTitle;
	}
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
           mSelectedItemsIds.delete(position);
 
        notifyDataSetChanged();
    }
 
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
        notifyDataSetChanged();
    }
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }    
    public void setSelected(long id){
    	selectedId=id;
    	notifyDataSetChanged();
    }
    public long getSelected(){
    	return selectedId;
    }
	@Override
	public Filter getFilter() {
		return new Filter() {
			
			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResult) {
				filteredItems = (ArrayList<AttributesItem>)filterResult.values;
                notifyDataSetChanged();
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
	            FilterResults results = new FilterResults();

                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = attributeItems;
                    results.count = attributeItems.size();
                }
                else
                {
                    ArrayList<AttributesItem> filterResultsData = new ArrayList<AttributesItem>();
                    for(AttributesItem data : attributeItems)
                    {
                        if(data.getName().toLowerCase().contains(charSequence.toString().toLowerCase())
                        		){
                            filterResultsData.add(data);
                        }
                    }            
                    results.values = filterResultsData;
                    Log.i("filtereddata", filterResultsData.toString());
                    results.count = filterResultsData.size();
                }

                return results;
			}
		};
	}	
}