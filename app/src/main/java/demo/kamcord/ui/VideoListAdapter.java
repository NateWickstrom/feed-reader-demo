package demo.kamcord.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import demo.kamcord.App;
import demo.kamcord.Config;
import demo.kamcord.R;
import demo.kamcord.io.VideoFeedRequest;
import demo.kamcord.model.VideoMeta;
import demo.kamcord.ui.interfaces.OnThumbnailClickListener;

/**
 * Adapter to display a list of videos from a feed.
 */
public class VideoListAdapter extends BaseAdapter implements
        Response.ErrorListener, Response.Listener<List<VideoMeta>> {

    private Request mInFlightRequest;

    private OnThumbnailClickListener mListener;

    private List<VideoMeta> mList = new ArrayList<>();

    public void triggerRequest(){
        cancelRequest();
        mList.clear();
        notifyDataSetChanged();

        mInFlightRequest = App.getQueue().add(
                new VideoFeedRequest.Builder(Config.FEED_ID)
                        .build(this, this));
    }

    public void cancelRequest(){
        if (mInFlightRequest != null) {
            mInFlightRequest.cancel();
            mInFlightRequest = null;
        }
    }

    public void setListener(OnThumbnailClickListener listener){
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public VideoMeta getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        VideoMeta meta = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_video, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);  // set tag on view
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String title = meta.getTitle();
        final String videoUrl = meta.getVideoUrl();
        final String url = mList.get(position).getThumbnail();

        // Set the Title
        holder.getTitle().setText(title);

        // Set the Thumbnail
        Picasso.with(parent.getContext())
                .load(url)
                .error(R.drawable.ic_no_image_available)
                .into(holder.getThumbnail());

        // Set the onClick response
        holder.getThumbnail().setOnClickListener(
                new View.OnClickListener() {

                     @Override
                     public void onClick(View v) {
                         if (mListener != null) {
                             mListener.OnThumbnailClick(videoUrl);
                         }
                     }
                 }

        );

        return convertView;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(App.get(), R.string.network_error_toast_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(List<VideoMeta> videoMetas) {
        mList.addAll(videoMetas);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        private final View row;
        private ImageView thumbnail;
        private TextView title;

        ViewHolder(View view){
            row = view;
        }

        ImageView getThumbnail() {
            if (thumbnail == null) {
                thumbnail = (ImageView) row.findViewById(R.id.thumbnail);
            }
            return thumbnail;
        }

        TextView getTitle() {
            if (title == null) {
                title = (TextView) row.findViewById(R.id.video_title);
            }
            return title;
        }

    }
}
