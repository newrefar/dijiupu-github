package com.dijiupu.utils.baidumap;

import com.dijiupu.utils.HttpUtil;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 判断坐标点是否在坐标集合内
 * @author newre
 */
public class PositionUtil {
    public static Boolean isInPolygon(Position checkPoint, List<Position> polygonPoints) {
        if (polygonPoints == null || checkPoint == null) {
            return false;
        }
        Boolean inside = false;
        int pointCount = polygonPoints.size();
        Position p1, p2;
        //第一个点和最后一个点作为第一条线，之后是第一个点和第二个点作为第二条线，之后是第二个点与第三个点，第三个点与第四个点...
        for (int i = 0, j = pointCount - 1; i < pointCount; j = i, i++) {
            p1 = polygonPoints.get(i);
            p2 = polygonPoints.get(j);
            //p2在射线之上
            if (checkPoint.getY().compareTo(p2.getY()) < 0) {
                //p1正好在射线中或者射线下方
                if (p1.getY().compareTo(checkPoint.getY()) <= 0) {
                    //斜率判断,在P1和P2之间且在P1P2右侧
                    if ((checkPoint.getY().subtract(p1.getY())).multiply(p2.getX().subtract(p1.getX())).compareTo(checkPoint.getX().subtract(p1.getX()).multiply((p2.getY().subtract(p1.getY())))) > 0) {
                        //射线与多边形交点为奇数时则在多边形之内，若为偶数个交点时则在多边形之外。
                        //由于inside初始值为false，即交点数为零。所以当有第一个交点时，则必为奇数，则在内部，此时为inside=(!inside)
                        //所以当有第二个交点时，则必为偶数，则在外部，此时为inside=(!inside)
                        inside = (!inside);
                    }
                }
            } else if (checkPoint.getY().compareTo(p1.getY()) < 0) {
                //p2正好在射线中或者在射线下方，p1在射线上
                //斜率判断,在P1和P2之间且在P1P2右侧
                if (((checkPoint.getY().subtract(p1.getY())).multiply((p2.getX().subtract(p1.getX())))).compareTo((checkPoint.getX().subtract(p1.getX())).multiply((p2.getY().subtract(p1.getY())))) < 0) {
                    inside = (!inside);
                }
            }
        }
        return inside;
    }

    public static List<Position> transformBaiduPointsToPosition(String points) {
        Gson gson = new Gson();
        if (points != null && !"".equals(points.trim())) {
            List<Position> positions = new ArrayList<>();
            String[] point = points.split("\\*");
            Position position;
            for (String p : point) {
                position = gson.fromJson(p, Position.class);
                positions.add(position);
            }
            return positions;
        } else {
            return null;
        }
    }

    /**
     * 取百度地图坐标
     * @param address
     * @return 如果没有精确匹配，则返回null
     */
    public static Position getBaiduPosition(String address) {
        String result = HttpUtil.doGet("http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=dVXpSH1sD3dnvNPMOVHb4HEIESgz37UB");
        Gson gson = new Gson();
        Map baiduPoint = gson.fromJson(result, Map.class);
        Double precise = (Double) ((Map) baiduPoint.get("result")).get("precise");
        if (precise.equals(1)) {
            Map location = (Map) ((Map) baiduPoint.get("result")).get("location");
            BigDecimal lng = (BigDecimal) location.get("lng");
            BigDecimal lat = (BigDecimal) location.get("lat");
            Position position = new Position();
            position.setLng(lng);
            position.setLat(lat);
            return position;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        List<Position> positions = transformBaiduPointsToPosition("{\"lng\":114.215116,\"lat\":30.57238}*{\"lng\":114.606633,\"lat\":30.57238}*{\"lng\":114.606633,\"lat\":30.381152}*{\"lng\":114.215116,\"lat\":30.381152}");
//        Position checkPoint = new Position();
//        checkPoint.setLat(new BigDecimal("30.511383035610739"));
//        checkPoint.setLng(new BigDecimal("114.3456265358652"));
        Position checkPoint = getBaiduPosition("湖北省武汉市洪山区书城路27号");
        System.out.println(isInPolygon(checkPoint, positions));
    }

}
