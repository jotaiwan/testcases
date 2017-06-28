package com.trytests;

import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

/**
 * Created by jchen on 29/06/17.
 */
public class TryCurrency {

    public static void main(String[] args) {
        Locale[] locales = Locale.getAvailableLocales();
        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();

        for(Locale l: locales){
            if( null == l.getCountry() || l.getCountry().isEmpty())
                continue;
            java.util.Currency c = java.util.Currency.getInstance(l);
            System.out.println(l.getCountry() + "   " + c.getSymbol() + "  " + c.getSymbol(l));
        }
    }

}
