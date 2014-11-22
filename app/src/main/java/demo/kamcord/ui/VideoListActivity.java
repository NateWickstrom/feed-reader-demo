package demo.kamcord.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import demo.kamcord.R;
import demo.kamcord.ui.interfaces.OnThumbnailClickListener;

/**
 * Demo Video List Activity for Kamcord.
 */
public class VideoListActivity extends ActionBarActivity implements
        OnThumbnailClickListener, AdapterView.OnItemClickListener {

    private VideoListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_view);

        mAdapter = new VideoListAdapter();

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        mAdapter.setListener(this);
        mAdapter.triggerRequest();
    }

    @Override
    protected void onStop() {
        mAdapter.cancelRequest();
        mAdapter.setListener(null);
        super.onStop();
    }

    @Override
    public void OnThumbnailClick(String videoUrl) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(videoUrl));
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, R.string.list_item_click_toast_message, Toast.LENGTH_SHORT).show();
    }
}
