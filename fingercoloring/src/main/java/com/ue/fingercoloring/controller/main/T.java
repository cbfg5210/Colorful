package com.ue.fingercoloring.controller.main;

/**
 * Created by hawk on 2017/12/27.
 */

public class T {
    private static T instance;

    public static T getInstance(){
        if(instance==null){
            instance=new T();
        }
        return instance;
    }
}
