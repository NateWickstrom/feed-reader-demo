package demo.kamcord.io;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import demo.kamcord.Config;
import demo.kamcord.model.VideoMeta;

/**
 * Custom Volley request for fetching Kamcord Video feeds.
 */
public class VideoFeedRequest extends Request<List<VideoMeta>> {

    /** tag for logging **/
    private static final String TAG = VideoFeedRequest.class.getSimpleName();
    /** listener for successful responses **/
    private final Response.Listener<List<VideoMeta>> mListener;
    /** Url extension for video feeds **/
    private static final String URL_EXT = "/app/v2/videos/feed/";

    public static class Builder {
        // required parameter
        private final int feedId;

        public Builder(int feedId) {
            this.feedId = feedId;
        }

        public VideoFeedRequest build(Response.Listener<List<VideoMeta>> listener, Response.ErrorListener errorListener) {

            // build the Url
            String baseUrl = Config.KAMCORD_API_DOMAIN + URL_EXT;
            Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
            builder.appendQueryParameter("feed_id", Integer.toString(feedId));

            // return the request
            return new VideoFeedRequest(Method.GET, builder.build().toString(), listener, errorListener);
        }

    }

    /** Creates new request object
     * @param method http method
     * @param url http url
     * @param listener listener for successful responses
     * @param errorListener error listener in case an error happened
     */
    private VideoFeedRequest(int method, String url, Response.Listener<List<VideoMeta>> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected void deliverResponse(List<VideoMeta> response) {
        // notify observer object, thereby notifying front-end UI
        mListener.onResponse(response);
    }

    /**
     * Volley Request method override to catch raw network Response
     *
     * @param response the raw network response
     * @return Response Video list
     */
    @Override
    protected final Response<List<VideoMeta>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            List<VideoMeta> data = parseResponse(json);
            return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonParseException e) {
            Log.e(TAG, "Json Parse Exception", e);
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception", e);
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unsupported Encoding Exception", e);
            return Response.error(new ParseError(e));
        }
    }

    private List<VideoMeta> parseResponse(String response) throws JsonParseException, JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONObject jsonResponse = jsonObject.getJSONObject("response");
        JSONArray jsonArray = jsonResponse.getJSONArray("video_list");

        Type listType = new TypeToken<ArrayList<VideoMeta>>() {}.getType();

        return new Gson().fromJson(jsonArray.toString(), listType);
    }
}
