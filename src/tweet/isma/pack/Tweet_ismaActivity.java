package tweet.isma.pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import tweet.isma.pack.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Tweet_ismaActivity extends ListActivity {

	public String readTwitterFeed(){
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://twitter.com/statuses/user_timeline/ismaeil_.json");
		
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				Log.v("Tweet-isma app", "J'ai réussi à lire ton fichier json :)");
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				
			} else {
				Log.v("Tweet-isma app", "Échec à la lecture du fichier !");
			}
		
			
		} catch (ClientProtocolException e)
		{ 	e.printStackTrace();
			Log.v("Tweet-isma app", "Client Protocol Exception"+ e.getMessage()); 		}
		catch (IOException e) 		
		{ 	e.printStackTrace();
		Log.v("Tweet-isma app", "Client Protocol Exception"+ e.getMessage()); 		}
		
		return builder.toString(); 
		
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Tweet-isma app","Création de l'application");
        
        String readTF = readTwitterFeed();
        try {
        	Log.v("Tweet-isma app", "... lecture du flux json en cours ...");
        	JSONArray jsonArray = new JSONArray(readTF);
        	final ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
        	Log.v("Tweet-isma app","Nombre de tweets : "+ jsonArray.length()+ ". Voici la liste :");
        	for (int i=0 ; i< jsonArray.length(); i++)
        	{
        		JSONObject jsonObject = jsonArray.getJSONObject(i);
        		Log.v("Tweet-isma app", jsonObject.getString("text"));
                HashMap<String, String> map = new HashMap<String,String>();     
                map.put("tweet", jsonObject.getString("text"));
                map.put("image", String.valueOf(R.drawable.isma));
                listItem.add(map);
                
                ListView maliste = getListView();
                maliste.setOnItemClickListener(new OnItemClickListener() {

                				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
               						long arg3) {
              					Toast.makeText(getApplicationContext(), "Vous avez cliqué sur '"+ listItem.get(arg2).get("tweet")+ "'", Toast.LENGTH_SHORT).show();
               				}
        				
        	});
                
    		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.twitter, new String[] {"tweet", "image"}, new int[]{ R.id.tweet, R.id.image});
            setListAdapter(mSchedule);
            
        }} catch (Exception e) {
        	e.printStackTrace();
        }
        



        
    }
    
}