package com.cxc.firstproject.bean.moviechild;


import com.example.http.ParamNames;

import java.io.Serializable;

/**
 * Created by jingbin on 2016/11/25.
 */

public class RatingBean  implements Serializable {
    /**
     * max : 10
     * average : 6.9
     * stars : 35
     * min : 0
     */
    @ParamNames("max")
    private int max;
    @ParamNames("average")
    private double average;
    @ParamNames("stars")
    private String stars;
    @ParamNames("min")
    private int min;

    public int getMax() {
        return max;
    }

    public double getAverage() {
        return average;
    }

    public String getStars() {
        return stars;
    }

    public int getMin() {
        return min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
