package pers.hll.aigc4chat.model.ollama.gson;

import com.google.gson.*;
import pers.hll.aigc4chat.model.ollama.constant.Role;

import java.lang.reflect.Type;

/**
 * 角色枚举序列化/反序列化器
 *
 * @author hll
 * @since 2024/05/04
 */
public class RoleSerializer implements JsonSerializer<Role>, JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return Role.codeOf(json.getAsString());
    }

    @Override
    public JsonElement serialize(Role role, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(role.getCode());
    }
}
