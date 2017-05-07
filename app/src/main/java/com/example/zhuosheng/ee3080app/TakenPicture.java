package com.example.zhuosheng.ee3080app;
public class TakenPicture {
    //Attributes
    private String linkToPicture;
    private String mainName = "?";
    private String[] suggestion =new String[] {"?","?","?","?"};

    //Methods

    //Constructor
    public TakenPicture(){ };
    public TakenPicture(String link)
    {
        setLinkToPicture(link);
    }
    // get set methods
    public String getLinkToPicture(){return this.linkToPicture;}
    public void setLinkToPicture(String path){this.linkToPicture=path;}
    public String getMainName() {return this.mainName;}
    public void setMainName(String mainName) {this.mainName = mainName;}
    public String[] getSuggestion() {return this.suggestion;}
    public void setSuggestion(String[] suggestion) {this.suggestion=suggestion;}



}