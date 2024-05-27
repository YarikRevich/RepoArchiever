package com.repoachiever.exception;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when smart graph properties file was not found.
 */
public class SmartGraphPropertiesFileNotFoundException extends FileNotFoundException {
    public SmartGraphPropertiesFileNotFoundException(Object... message) {
        super(
                new Formatter()
                        .format("SmartGraph properties file at the given location is not found: %s",
                                Arrays.stream(message).toArray())
                        .toString());
    }
}
