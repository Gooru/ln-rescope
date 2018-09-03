package org.gooru.rescope.responses.auth;

public interface AuthResponseHolder {

    boolean isAuthorized();

    boolean isAnonymous();

    String getUser();
}
