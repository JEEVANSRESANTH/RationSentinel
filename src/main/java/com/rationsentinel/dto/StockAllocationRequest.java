package com.rationsentinel.dto;

import com.rationsentinel.entity.CommodityType;

public class StockAllocationRequest {

    private Long fpsId;
    private CommodityType commodity;
    private int quantity;
    private int month;
    private int year;

    // getters and setters
    public Long getFpsId() {
        return fpsId;
    }

    public void setFpsId(Long fpsId) {
        this.fpsId = fpsId;
    }

    public CommodityType getCommodity() {
        return commodity;
    }

    public void setCommodity(CommodityType commodity) {
        this.commodity = commodity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
