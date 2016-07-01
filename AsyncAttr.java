package com.puz.salestakingorder.asynchronous;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.puz.salestakingorder.R;
import com.puz.salestakingorder.datasources.AttributeDataSource;
import com.puz.salestakingorder.utils.AsyncResponse;
import com.puz.salestakingorder.utils.SessionManagement;

public class AsyncAttr extends AsyncTask<String, String, String>{
	private Activity activity;
	private String uRL;
	private ProgressDialog progressDialog;
	public AsyncResponse delegate=null;
	private AttributeDataSource dataSource;
	private SessionManagement session;
	public AsyncAttr(Activity activity){
		this.activity=activity;
		session=new SessionManagement(activity.getApplicationContext());
	}
	@Override
	protected void onPostExecute(String result) {
		Log.i("news result", result);
		progressDialog.dismiss();
		delegate.processFinish(result,"attributes");
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		progressDialog=new ProgressDialog(activity);
		dataSource=new AttributeDataSource(activity.getApplicationContext());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		progressDialog=ProgressDialog.show(activity,null,"Loading");
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		uRL=activity.getApplicationContext().getResources().getString(R.string.serverurl)+"api/outlet_attribute?auth_token="+params[0];
		String outPut="";
		try{
			HttpGet httpget = new HttpGet(uRL);
			HttpParams httpParameters=new BasicHttpParams();
			int timeout=5000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpResponse response = client.execute(httpget);
            outPut = EntityUtils.toString(response.getEntity());
            Log.i("output", outPut);
            try{
				JSONObject json= new JSONObject(outPut);
				if(!json.getBoolean("status")){
					return json.getString("code");
				}
				JSONArray jData=json.getJSONArray("data");
				dataSource.open();
				dataSource.deleteAllAttribute();
				for(int i=0;i<jData.length();i++){
					JSONObject jItem=jData.getJSONObject(i);
					dataSource.createAttribute(
							jItem.getInt("categoryid"), 
							jItem.getString("name"),
							jItem.getString("type"),
							jItem.getString("status"), 
							jItem.getString("description"));
				}
				dataSource.close();
				return "200";
			}catch(JSONException e){
				Log.e("JSONException on load Event", e.getMessage());
				dataSource.close();
				return "400";
			}    
		}
		catch (Exception e) {
			e.printStackTrace();
			return "408";
		}
	}

}

