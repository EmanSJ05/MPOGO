package com.emansj.mpogo.helper;

import java.util.List;

public class AppGlobal {
    private static final AppGlobal ourInstance = new AppGlobal();

    public static synchronized AppGlobal getInstance() {
        return ourInstance;
    }

    private AppGlobal(){}

    //Static Global vars
    public static String url= "http://192.168.100.5:8080/mpogo";
    public static String URL_LOGIN = url+"/user/signin";

    //Global vars
    private int UserId;
    private String UserName;
    private String UserFullName;
    private List<String> TahunRKAs;
    private int TahunRKA;

    //Methods
    public int UserId() {return UserId;}
    public void UserId(int userId) {UserId = userId;}

    public String UserName() {return UserName;}
    public void UserName(String userName) {UserName = userName;}

    public String UserFullName() {return UserFullName;}
    public void UserFullName(String userFullName) {UserFullName = userFullName;}

    public List<String> TahunRKAs() {return TahunRKAs;}
    public void TahunRKAs(List<String> tahunRKAs) {TahunRKAs = tahunRKAs;}

    public int TahunRKA() {return TahunRKA;}
    public void TahunRKA(int tahunRKA) {TahunRKA = tahunRKA;}

}
