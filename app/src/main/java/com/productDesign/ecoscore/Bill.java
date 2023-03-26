package com.productDesign.ecoscore;

import java.io.Serializable;

public class Bill implements Serializable {

    private String Month;
    private double Consumption;
    private double Ratio;
    public static double Mean = 489.5;
    public static double Std = 122.3;
    private double Co2;
    private double Precentage;

    public Bill(String month, double consumption) {
        Consumption = consumption;
        Month = month;
        Ratio = 0.553;
        Co2 = consumption * Ratio;
        double z = (Consumption-Mean) / Std;
        Precentage = 100 * (1-calculateCumulativeProbability(z));
    }

    private static double calculateCumulativeProbability(double z) {
        double t = 1.0 / (1.0 + 0.2316419 * Math.abs(z));
        double d = 0.3989423 * Math.exp(-z * z / 2.0);
        double p = d * t * (0.3193815 + t * (-0.3565638 + t * (1.781478 + t * (-1.821256 + t * 1.330274))));
        if (z > 0) {
            return 1 - p;
        } else {
            return p;
        }
    }

    public String getMonth() {
        return Month;
    }

    public double getConsumption() {
        return Consumption;
    }

    public double getCo2() {
        return Co2;
    }
    public double getRatio(){
        return Ratio;
    }

    public double getPrecentage(){
        return Precentage;
    }


}
