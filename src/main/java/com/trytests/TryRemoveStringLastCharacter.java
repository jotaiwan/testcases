package com.trytests;

public class TryRemoveStringLastCharacter {

    public static void main(String[] args) {

        String requestURI = "http://viator.com/booking/book/";

        String newRequestURI = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;

        System.out.println("change request uri from '" + requestURI + "' to '" + newRequestURI + "'");

        requestURI = "http://viator.com/booking/book/";

        newRequestURI = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;

        System.out.println("change request uri from '" + requestURI + "' to '" + newRequestURI + "'");

    }


}
