package com.talengu.wordwarrior.model;


import android.graphics.Color;


public class ListviewItem {
	public int fileIconRes;
	public String fileName;
	public int change;
	public int color;

	public ListviewItem(String fileName) {
		//this.fileIconRes = fileIconRes;
		this.fileName = fileName;
		this.change = 0;
		this.color=0;

	}
	public void changedo() {
		switch (change) {
		case 0:change = 1;break;
		case 1:change = 0;break;
		default:change = 0;break;
		}
	}
	public void isdeletedo(){
		color= Color.rgb(91, 90, 90);
	}
	public void isback(){color=0;}
}