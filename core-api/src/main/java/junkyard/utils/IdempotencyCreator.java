package junkyard.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class IdempotencyCreator {

    public String create() {
        return UUID.randomUUID().toString();
    }
}
