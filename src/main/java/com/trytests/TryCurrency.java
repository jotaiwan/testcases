package com.trytests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jchen on 29/06/17.
 */
public class TryCurrency {

    public static void main(String[] args) {
        Locale[] locales = Locale.getAvailableLocales();
        List<Currency> currencies = new ArrayList<>(Currency.getAvailableCurrencies());

        Map<String, String> m = new HashMap<>();
        m.put("key", null);

        Collections.sort(currencies, new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                return o1.getCurrencyCode().compareTo(o2.getCurrencyCode());
            }
        });

        for(Locale l: locales){
            if( null == l.getCountry() || l.getCountry().isEmpty())
                continue;
            java.util.Currency c = java.util.Currency.getInstance(l);
            System.out.println(l.getCountry() + "   " + c.getSymbol() + "  " + c.getSymbol(l));
        }
    }

}
