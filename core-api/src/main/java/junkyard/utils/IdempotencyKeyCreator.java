package junkyard.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class IdempotencyKeyCreator {

    public String create(Object data) {
        return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
    }
}
