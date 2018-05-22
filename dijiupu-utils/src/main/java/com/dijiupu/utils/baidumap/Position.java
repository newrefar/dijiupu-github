package com.dijiupu.utils.baidumap;

import java.math.BigDecimal;

/**
 * @author newr
 */
public class Position {
    /**
     * 经度值
     */
    private BigDecimal lng;

    /**
     * 纬度值
     */
    private BigDecimal lat;

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getX() {
        return lat;
    }

    public void setX(BigDecimal x) {
        this.lat = x;
    }

    public BigDecimal getY() {
        return lng;
    }

    public void setY(BigDecimal y) {
        this.lng = y;
    }
}
