package com.shiva.tissueanalyticsassignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class DetailActivity extends AppCompatActivity {
    //Global declaration of variables
    public TextView tvMin,tvMax,tvAvg,tvSitename,tvSitecode,axisDesc;
    public StreamFlowData streamFlowData;
    public ProgressDialog progressDialog;
    LineGraphSeries<DataPoint> series;
    GraphView graph;
    Button btnViewGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //checking Internet connection
        verifyInternetConnection();

        //fetching textview ids
        tvMin=(TextView)findViewById(R.id.tv_min);
        tvMax=(TextView)findViewById(R.id.tv_max);
        tvAvg=(TextView)findViewById(R.id.tv_avg);
        tvSitename=(TextView)findViewById(R.id.tv_sitename);
        tvSitecode=(TextView)findViewById(R.id.tv_sitecode);
        btnViewGraph=(Button)findViewById(R.id.btn_view);
        graph=(GraphView)findViewById(R.id.gv_graphview);
        axisDesc=(TextView) findViewById(R.id.tv_axisdesc);
        series= new LineGraphSeries<DataPoint>();

      //disable the button and textviews related to the graph generation until the data is loaded
        btnViewGraph.setClickable(false);
        graph.setVisibility(View.GONE);
        axisDesc.setVisibility(View.GONE);

        //verify if the intent is not null
        if(getIntent()!=null)
        {
            //fetch the extras got from the Main Activity
            Bundle bundle= getIntent().getExtras();
            if(bundle != null)
            {
                String site = (String)bundle.get("sitenumber");
                //if key verifies to site1, fetch the data from site1 i.e site number= 08290000
                if(site.equalsIgnoreCase("site1"))
                {
                    new AsyncFetchStreamFlowData(this)
                    .execute("https://waterservices.usgs.gov/nwis/iv/?sites="+getResources().getString(R.string.site1_sitenumber)+"&period=P7D&format=json");
                }
                //if key verifies to site2, fetch the data from site2 i.e site number= 08269000
                else if(site.equalsIgnoreCase("site2"))
                {
                    new AsyncFetchStreamFlowData(this)
                    .execute("https://waterservices.usgs.gov/nwis/iv/?sites="+getResources().getString(R.string.site2_sitenumber)+"&period=P7D&format=json");
                }
            }
        }

        btnViewGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.setVisibility(View.VISIBLE);
                axisDesc.setVisibility(View.VISIBLE);
                int[] xpoints = streamFlowData.getStreamValues();
                int y = 0;
                for(int i=0; i<xpoints.length;i++)
                {
                    series.appendData(new DataPoint(y, xpoints[i]), true, xpoints.length);
                    y += 150;
                }
                graph.addSeries(series);
                btnViewGraph.setClickable(false);
            }
        });
    }

    //method to Verify Internet Connection
    public void verifyInternetConnection()
    {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){

            new AlertDialog.Builder(DetailActivity.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.internet_error))
                    .setPositiveButton("OK", null).show();
        }

    }
}

class AsyncFetchStreamFlowData extends AsyncTask<String, Void, StreamFlowData> {
    private StreamFlowData streamFlowData = null;
    private ProgressDialog progressDialog = null;
    DetailActivity detailActivity = null;

   public AsyncFetchStreamFlowData(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = detailActivity.progressDialog;
        progressDialog = new ProgressDialog(detailActivity);
        progressDialog.setMessage(detailActivity.getResources().getString(R.string.progress_message)); // Setting Message
        progressDialog.setTitle(detailActivity.getResources().getString(R.string.progress_title)); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    @Override
    protected StreamFlowData doInBackground(String... params) {
        String line = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            if (!json.contains("error")) {
                streamFlowData = DataParser.parseStreamFlowData(json);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return streamFlowData;
    }

    @Override
    protected void onPostExecute(StreamFlowData streamFlowData) {
        progressDialog.dismiss();
        if (streamFlowData != null) {
            detailActivity.streamFlowData = streamFlowData;
            //enable the button once the data is loaded
            detailActivity.btnViewGraph.setClickable(true);

            //set the sitename, sitecode, minimum, maximum, average values to the textviews in the layout
            detailActivity.tvMin.setText(Integer.toString((int)streamFlowData.getMinValue())+" ft3/s");
            detailActivity.tvMax.setText(Integer.toString((int)streamFlowData.getMaxValue())+" ft3/s");
            detailActivity.tvAvg.setText(Integer.toString((int)streamFlowData.getAverageValue())+" ft3/s");
            detailActivity.tvSitename.setText(streamFlowData.getSiteName());
            detailActivity.tvSitecode.setText(streamFlowData.getSiteCode());
        }
        else
            new AlertDialog.Builder(detailActivity)
                    .setTitle(detailActivity.getResources().getString(R.string.app_name))
                    .setMessage(detailActivity.getResources().getString(R.string.no_data))
                    .setPositiveButton("OK", null).show();

    }


}


