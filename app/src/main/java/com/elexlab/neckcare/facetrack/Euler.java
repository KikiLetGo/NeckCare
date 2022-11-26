package com.elexlab.neckcare.facetrack;

public class Euler {
    private double x;
    private double y;
    private double z;

    public Euler(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Euler() {
    }

    private String order = "XYZ";//default

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
