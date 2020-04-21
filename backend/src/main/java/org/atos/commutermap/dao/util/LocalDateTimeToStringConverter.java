package org.atos.commutermap.dao.util;

import javax.persistence.AttributeConverter;
import java.time.LocalDateTime;

public class LocalDateTimeToStringConverter implements AttributeConverter<LocalDateTime, String> {
    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute.toString();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return LocalDateTime.parse(dbData);
    }
}
