package com.example.jazzi.materialtest;

/*这个类代表滑动窗口中每一张图片的属性以及方法*/
public class Fruit {
    private String name;
    private int imageId;

    public Fruit(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
