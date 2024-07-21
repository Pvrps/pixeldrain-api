package ca.purps.pixeldrain.model;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class ApiData implements Serializable {

    private String id;
    private String name;
    private int size;
    private int views;
    @SerializedName("bandwidth_used")
    private int bandwidthUsed;
    @SerializedName("bandwidth_used_paid")
    private int bandwidthUsedPaid;
    private int downloads;
    @SerializedName("date_upload")
    private Date dateUpload;
    @SerializedName("date_last_view")
    private Date dateLastView;
    @SerializedName("mime_type")
    private String mimeType;
    @SerializedName("thumbnail_href")
    private String thumbnailHref;
    @SerializedName("hash_sha256")
    private String hashSha256;
    @SerializedName("delete_after_date")
    private Date deleteAfterDate;
    @SerializedName("delete_after_downloads")
    private int deleteAfterDownloads;
    private String availability;
    @SerializedName("availability_message")
    private String availabilityMessage;
    @SerializedName("abuse_type")
    private String abuseType;
    @SerializedName("abuse_reporter_name")
    private String abuseReporterName;
    @SerializedName("can_edit")
    private boolean canEdit;
    @SerializedName("can_download")
    private boolean canDownload;
    @SerializedName("show_ads")
    private boolean showAds;
    @SerializedName("allow_video_player")
    private boolean allowVideoPlayer;
    @SerializedName("download_speed_limit")
    private int downloadSpeedLimit;

    @Setter
    private byte[] thumbnailData;

}