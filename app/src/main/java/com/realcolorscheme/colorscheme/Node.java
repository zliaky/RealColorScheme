package com.realcolorscheme.colorscheme;

public class Node {
    public int r;
    public int g;
    public int b;
    public int rank;


    public void print() {
        System.out.println("r: " + r + ", g: " + g + ", b: " + b + ", light: " + (r+g+b)/3);
    }
}