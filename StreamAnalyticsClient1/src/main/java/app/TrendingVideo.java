package app;


import java.io.Serializable;

public class TrendingVideo  {

    private String video_id,
            trending_date,
            title,
            channel_title,
            category_id,
            publish_time,
            tags,
            thumbnail_link,
            description;

    private double views,
            likes,
            dislikes,
            comment_count;

    private boolean comments_disabled,
            ratings_disabled,
            video_error_or_removed;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getTrending_date() {
        return trending_date;
    }

    public void setTrending_date(String trending_date) {
        this.trending_date = trending_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel_title() {
        return channel_title;
    }

    public void setChannel_title(String channel_title) {
        this.channel_title = channel_title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getThumbnail_link() {
        return thumbnail_link;
    }

    public void setThumbnail_link(String thumbnail_link) {
        this.thumbnail_link = thumbnail_link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getViews() {
        return views;
    }

    public void setViews(double views) {
        this.views = views;
    }

    public double getLikes() {
        return likes;
    }

    public void setLikes(double likes) {
        this.likes = likes;
    }

    public double getDislikes() {
        return dislikes;
    }

    public void setDislikes(double dislikes) {
        this.dislikes = dislikes;
    }

    public double getComment_count() {
        return comment_count;
    }

    public void setComment_count(double comment_count) {
        this.comment_count = comment_count;
    }

    public boolean isComments_disabled() {
        return comments_disabled;
    }

    public void setComments_disabled(boolean comments_disabled) {
        this.comments_disabled = comments_disabled;
    }

    public boolean isRatings_disabled() {
        return ratings_disabled;
    }

    public void setRatings_disabled(boolean ratings_disabled) {
        this.ratings_disabled = ratings_disabled;
    }

    public boolean isVideo_error_or_removed() {
        return video_error_or_removed;
    }

    public void setVideo_error_or_removed(boolean video_error_or_removed) {
        this.video_error_or_removed = video_error_or_removed;
    }
}
