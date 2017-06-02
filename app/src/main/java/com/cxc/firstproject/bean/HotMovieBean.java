package com.cxc.firstproject.bean;


import com.cxc.firstproject.bean.moviechild.SubjectsBean;
import com.example.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingbin on 2016/11/25.
 */

public class HotMovieBean  implements Serializable {

    @ParamNames("count")
    private int count;
    @ParamNames("start")
    private int start;
    @ParamNames("total")
    private int total;
    @ParamNames("title")
    private String title;
    @ParamNames("subjects")
    private List<SubjectsBean> subjects;

    public int getCount() {
        return count;
    }
    public int getStart() {
        return start;
    }
    public int getTotal() {
        return total;
    }
    public String getTitle() {
        return title;
    }
    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }
}
