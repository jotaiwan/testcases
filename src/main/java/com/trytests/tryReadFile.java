package com.trytests;


import com.google.common.collect.Iterables;
import com.trytests.dto.HotelMapping;
import com.trytests.dto.HotelSupplierProduct;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

public class tryReadFile {

    private static final int batchSize = 2;

    // read the file line by line and create an object
    public static void main(String[] args) {
        // get from database
//        List<HotelSupplierProduct> hotelSupplierProducts =
//            getHotelSupplierProducts("/home/jchen/aChenBox/Desktop/hotel-28628-from-db.csv", ",");

        // ie. get zeus from url in vcs
        Collection<HotelMapping> zeusHotelMappings =
            getZeusHotelMappings("/home/jchen/aChenBox/Desktop/hotel-28628.txt", "\\|");


        removeHotelNotInZeusLists(zeusHotelMappings, 123);

        List<HotelMapping> hotelMappings = new ArrayList<>();
        Collection<HotelMapping> hotelSupplierProducFromZeusList = updateHotelMappings(hotelMappings);

        // now get Zeus list
        // ie. cop
    }


    private static Collection<HotelMapping> getZeusHotelMappings(String fileName, String delimeter) {
        List<String> lines = getStringLinesFromFile(fileName);

        List<HotelMapping> hotelMappings = new ArrayList<>();
        for (String line : lines) {
            // breaking the line with delimeter
            String[] lineBreaks = line.split(delimeter);
            HotelMapping hotelMapping = new HotelMapping();
            hotelMapping.setHotelId(lineBreaks[3]);
            hotelMapping.addSupplierId(lineBreaks[1]);
            hotelMapping.addProductId(lineBreaks[2]);
            hotelMappings.add(hotelMapping);
        }

        Collection<HotelMapping> mergedMapping = updateHotelMappings(hotelMappings);

        return mergedMapping;
    }

    private static Collection<HotelMapping> updateHotelMappings(List<HotelMapping> hotelMappings) {
        Collection<HotelMapping> mergedMappings = hotelMappings.stream().collect(
            groupingBy(
                HotelMapping::getHotelId,
                reducing(
                    null,
                    (first, second) -> {
                        if (first != null) {
                            // Merging mapping data for the same hotel IDs.
                            first.merge(second);
                            return first;
                        }
                        return second;
                    }
                )
            )
        ).values();

        return mergedMappings;
    }


    private static List<HotelSupplierProduct> getHotelSupplierProducts(String fileName, String delimeter) {
        List<String> lines = getStringLinesFromFile(fileName);

        List<HotelSupplierProduct> hotelSupplierProducts = new ArrayList<>();
        for (String line : lines) {
            // breaking the line with delimeter
            String[] lineBreaks = line.split(",");
            HotelSupplierProduct hotelSupplierProduct = new HotelSupplierProduct();
            hotelSupplierProduct.setHotelId(lineBreaks[0]);
            hotelSupplierProduct.setSupplierId(lineBreaks[1]);
            hotelSupplierProduct.setProductCode(lineBreaks[2]);
            hotelSupplierProducts.add(hotelSupplierProduct);
        }

        return hotelSupplierProducts;
    }

    public static List<String> getStringLinesFromFile(String fileName) {
        List<String> stringLines = new ArrayList<>();
        try {
            //
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //            StringBuffer stringBuffer = new StringBuffer();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //                stringBuffer.append(line);
                //                stringBuffer.append("\n");
                stringLines.add(line);
            }
            fileReader.close();
            System.out.println("Contents of file:");
            //            System.out.println(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringLines;
    }

    private static void removeHotelNotInZeusLists(Collection<HotelMapping> hotelMappings, int destinationId) {

        // Convert hotelMapping to productCode with matched destination ids
        Set<String> supplierIdsInZeusList = getSuppliersInZeusList(hotelMappings);

        // retrieve all hotel lists from database
        List<HotelSupplierProduct> allHotelSupplierProducts =
            getHotelSupplierProducts("/home/jchen/aChenBox/Desktop/hotel-28628-from-db.csv", ",");
//            getAllHotelListForSupplierAndProducts(supplierIdsInZeusList, destinationId);

        // find the hotels that do not have in Zeus list
        List<HotelSupplierProduct> hotelSupplierProductsNotInZeusList =
            allHotelSupplierProducts.stream().filter(h -> isHotelNotInZeusList(h, hotelMappings))
                                    .collect(Collectors.toList());

        String hotelRemovedListString =
            hotelSupplierProductsNotInZeusList.stream()
                  .map(HotelSupplierProduct::getHotelId).collect(Collectors.joining(","));

        System.out.println("Remove hotel list: " + hotelRemovedListString);
    }


    private static Set<String> getSuppliersInZeusList(Collection<HotelMapping> hotelMappings) {
        return hotelMappings.stream().flatMap(h -> h.getSupplierIds().stream()).collect(Collectors.toSet());
    }


    private static List<HotelSupplierProduct> getAllHotelListForSupplierAndProducts(Set<String> supplierInZeusLists,
        int destinationId) {

        List<HotelSupplierProduct> hotelSupplierProducts = new ArrayList<>();
        if (supplierInZeusLists != null) {
            for (List<String> supplierIdsBatch : Iterables.partition(supplierInZeusLists, batchSize)) {
                try {
                    hotelSupplierProducts.addAll(getHotelSupplierProducts("/home/jchen/aChenBox/Desktop/hotel-28628-from-db.csv", ","));
                } catch (Exception e) {
                }
            }
        }

        return hotelSupplierProducts;
    }



    private static boolean isHotelNotInZeusList(HotelSupplierProduct hotelSupplierProduct,
        Collection<HotelMapping> hotelMappings) {
        for (HotelMapping mergeMapping: hotelMappings) {
            if (StringUtils.equalsIgnoreCase(mergeMapping.getHotelId(), hotelSupplierProduct.getHotelId())) {
                return ! (mergeMapping.getSupplierIds().contains(hotelSupplierProduct.getSupplierId()) ||
                    mergeMapping.getProductIds().contains(hotelSupplierProduct.getProductCode()));
            }
        }
        return true;
    }


}
