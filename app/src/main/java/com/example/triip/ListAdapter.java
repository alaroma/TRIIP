package com.example.triip;


import android.net.Uri;

public class ListAdapter {

    private String name; // название
    private String data;  // дата
    private String tag; // теги
  //  private Uri image; // ресурс фото

    public ListAdapter(String name, String data, String tag){

        this.name=name;
        this.data=data;
        this.tag=tag;
        //this.image=image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    //public Uri getImage() {
      //  return this.image;
    //}

   // public void setImage(Uri image) {
      //  this.image = image;
    //}

}

