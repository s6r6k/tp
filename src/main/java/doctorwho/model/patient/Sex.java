package doctorwho.model.patient;

import static doctorwho.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Patient's biological sex in doctorwho.
 * Guarantees: immutable; is valid as declared in {@link #isValidSex(String)}
 */
public class Sex {

    public static final String MESSAGE_CONSTRAINTS =
        "Sex should be M (male), F (female), or O (other), and it is case-insensitive.";
    public static final String VALIDATION_REGEX = "[mMfF]";
    public final String value;

    /**
     * Constructs a {@code Sex}.
     *
     * @param sex A valid sex string.
     */
    public Sex(String sex) {
        requireNonNull(sex);
        checkArgument(isValidSex(sex), MESSAGE_CONSTRAINTS);
        this.value = sex.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid sex value.
     */
    public static boolean isValidSex(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Sex)) {
            return false;
        }
        Sex otherSex = (Sex) other;
        return value.equals(otherSex.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}