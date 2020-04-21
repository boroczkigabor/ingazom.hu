package org.atos.commutermap.dao.util;

import org.springframework.data.convert.Jsr310Converters;

import javax.persistence.AttributeConverter;
import java.time.Duration;

public class DurationConverter implements AttributeConverter<Duration, String> {
    @Override
    public String convertToDatabaseColumn(Duration attribute) {
        return Jsr310Converters.DurationToStringConverter.INSTANCE.convert(attribute);
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        return Jsr310Converters.StringToDurationConverter.INSTANCE.convert(dbData);
    }
}
