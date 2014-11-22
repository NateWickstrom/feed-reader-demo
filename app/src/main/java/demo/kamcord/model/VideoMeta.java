package demo.kamcord.model;

import com.google.gson.annotations.SerializedName;

/**
 *  Java Object representation of items in a Kamcord video feed.
 */
public class VideoMeta {

    @SerializedName("featured_time")
    private long mFeaturedTime;
    @SerializedName("duration")
    private double mDuration;
    @SerializedName("video_site_watch_page")
    private String mVideoSiteWatchPage;
    @SerializedName("interaction_counts")
    private Interaction mInteractions;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("video_id")
    private String mVideoId;
    @SerializedName("staff_picked_time")
    private long mStaffPickTime;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("upload_time")
    private long mUploadTime;
    @SerializedName("game_primary_id")
    private long mGamePrimaryId;
    @SerializedName("thumbnails")
    private Thumbnails mThumbnails;
    @SerializedName("level")
    private String mlevel;
    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("game_name")
    private String mGameName;
    @SerializedName("video_url")
    private String mVideoUrl;

    static class Interaction {
        int likes;
        int comments;
        int views;
    }

    static class Thumbnails {
        @SerializedName("REGULAR")
        String regular;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getThumbnail(){
        return mThumbnails.regular;
    }

    public String getVideoUrl(){
        return mVideoUrl;
    }

}
