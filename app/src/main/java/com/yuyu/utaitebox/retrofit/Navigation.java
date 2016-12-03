package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Navigation {

    @SerializedName("currentPage")
    private String currentPage;

    @SerializedName("pageCount")
    private String pageCount;

    @SerializedName("data")
    private ArrayList<Data> data;

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
