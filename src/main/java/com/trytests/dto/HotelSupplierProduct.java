package com.trytests.dto;

import java.util.Objects;

public class HotelSupplierProduct {

    private String hotelId;
    private String supplierId;
    private String productCode;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HotelSupplierProduct that = (HotelSupplierProduct) o;
        return Objects.equals(hotelId, that.hotelId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(productCode, that.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelId, supplierId, productCode);
    }

}
