package com.example.primeraapp;

public class MyModel {
    String ImageRes;
    String Symbol, Name, Price;

    public MyModel(String imageRes, String symbol, String name, String price) {
        ImageRes = imageRes;
        Symbol = symbol;
        Price = price;
        Name = name;
    }

    public String getImageRes() {
        return ImageRes;
    }

    public void setImageRes(String imageRes) {
        ImageRes = imageRes;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getName() {
        return Name;
    }

    public void setSName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
