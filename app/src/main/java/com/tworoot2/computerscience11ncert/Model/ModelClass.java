package com.tworoot2.computerscience11ncert.Model;

public class ModelClass {

    String Link, Title, chNo;

    public ModelClass() {
    }

    public ModelClass(String link, String title, String chNo) {
        Link = link;
        Title = title;
        this.chNo = chNo;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getChNo() {
        return chNo;
    }

    public void setChNo(String chNo) {
        this.chNo = chNo;
    }
}
