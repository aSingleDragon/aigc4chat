package pers.hll.aigc4chat.model.ollama.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Gson的LocalDateTime反序列化器
 *
 * @author hll
 * @since 2024/04/30
 */
public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {  

    @Override  
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {  
        String dateTimeString = json.getAsString();
        Instant instant = Instant.parse(dateTimeString);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}