package org.atos.commutermap.dao.util;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {

    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        return String.format("%s %f", attribute.getCurrency().getCurrencyCode(), attribute.getNumber().doubleValue());
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        int spaceIndex = dbData.indexOf(" ");
        return Money.of(new BigDecimal(dbData.substring(0, spaceIndex)),
                        dbData.substring(spaceIndex + 1));
    }
}
