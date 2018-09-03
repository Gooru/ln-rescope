package org.gooru.rescope.infra.data.competency;

import java.util.Objects;

/**
 * @author ashish.
 */
public class SubjectCode {

    private final String code;

    public SubjectCode(String code) {
        Objects.requireNonNull(code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubjectCode that = (SubjectCode) o;

        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "SubjectCode{" + "code='" + code + '\'' + '}';
    }
}
