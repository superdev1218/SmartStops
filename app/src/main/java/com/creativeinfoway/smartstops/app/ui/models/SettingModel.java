package com.creativeinfoway.smartstops.app.ui.models;

import com.google.gson.annotations.SerializedName;

public class SettingModel
{
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("value")
    private String value;

    @SerializedName("status")
    private String status;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

}