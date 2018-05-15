package org.gooru.rescope.infra.exceptions;

import org.gooru.rescope.responses.MessageResponse;

/**
 * @author ashish
 */
public class MessageResponseWrapperException extends RuntimeException {
    private final MessageResponse response;

    public MessageResponseWrapperException(MessageResponse response) {
        this.response = response;
    }

    public MessageResponse getMessageResponse() {
        return this.response;
    }
}
