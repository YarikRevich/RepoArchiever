package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used to indicate command executor failure.
 */
public class CommandExecutorException extends IOException {
  public CommandExecutorException(Object... message) {
    super(
        new Formatter()
            .format("Invalid command executor behaviour: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
