package io.github.junhaoshih.linebotserver.enums.cwb;

public enum Interval {
    Undefined("未知"),
    TwoDays("二天"),
    OneWeek("一周");

    private String interval;

    private Interval(String interval) {
        this.interval = interval;
    }
}
