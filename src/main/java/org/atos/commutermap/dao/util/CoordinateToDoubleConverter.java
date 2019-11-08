package org.atos.commutermap.dao.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CoordinateToDoubleConverter implements AttributeConverter<Double, Long> {

    @Override
    public Long convertToDatabaseColumn(Double attribute) {
        throw new IllegalStateException("Should not change any stations.");
    }

    @Override
    public Double convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        String stringValue = String.valueOf(dbData);
        return Double.valueOf(stringValue.substring(0, 2) + "." + stringValue.substring(2));
    }

}
