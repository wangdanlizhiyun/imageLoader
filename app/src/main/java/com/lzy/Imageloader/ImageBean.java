package com.lzy.Imageloader;

/**
 * Created by lizhiyun on 2018/4/19.
 */

public class ImageBean {

    //本地图片id
    private int mId;

    //一个图片完整路径
    private String url;
    private String path;

    public ImageBean() {
    }

    public ImageBean(int mId, String url) {
        this.mId = mId;
        this.url = url;
    }
    public ImageBean(String path) {
        this.path = path;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
        this.url = "content://media/external/images/media/"+mId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
