package com.trytests.dto;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a mapping between a particular hotel and supplier or product.
 */
public class HotelMapping {
    private String hotelId;
    private final Set<String> supplierIds = new HashSet<>();
    private final Set<String> productIds = new HashSet<>();

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void addSupplierId(String supplierId) {
        if (supplierId != null) {
            this.supplierIds.add(supplierId);
        }
    }

    public void addProductId(String productId) {
        if (productId != null) {
            this.productIds.add(productId);
        }
    }

    public String getHotelId() {
        return hotelId;
    }

    public Set<String> getSupplierIds() {
        return supplierIds;
    }

    public Set<String> getProductIds() {
        return productIds;
    }

    public void merge(HotelMapping mapping) {
        if (mapping != null && Objects.equals(hotelId, mapping.getHotelId())) {
            this.supplierIds.addAll(mapping.getSupplierIds());
            this.productIds.addAll(mapping.getProductIds());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HotelMapping that = (HotelMapping) o;
        return Objects.equals(hotelId, that.hotelId) &&
            Objects.equals(supplierIds, that.supplierIds) &&
            Objects.equals(productIds, that.productIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelId, supplierIds, productIds);
    }
}
