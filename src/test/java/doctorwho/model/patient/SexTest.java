package doctorwho.model.patient;

import static doctorwho.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SexTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Sex(null));
    }

    @Test
    public void constructor_invalidSex_throwsIllegalArgumentException() {
        String invalidSex = "";
        assertThrows(IllegalArgumentException.class, () -> new Sex(invalidSex));
    }

    @Test
    public void isValidSex() {
        // null sex
        assertThrows(NullPointerException.class, () -> Sex.isValidSex(null));

        // invalid sex
        assertFalse(Sex.isValidSex("")); // empty string
        assertFalse(Sex.isValidSex(" ")); // spaces only
        assertFalse(Sex.isValidSex("X")); // not an accepted value
        assertFalse(Sex.isValidSex("Male")); // full word not allowed
        assertFalse(Sex.isValidSex("Female")); // full word not allowed
        assertFalse(Sex.isValidSex("1")); // numbers not allowed
        assertFalse(Sex.isValidSex("MF")); // multiple characters not allowed

        // valid sex
        assertTrue(Sex.isValidSex("M")); // uppercase male
        assertTrue(Sex.isValidSex("m")); // lowercase male
        assertTrue(Sex.isValidSex("F")); // uppercase female
        assertTrue(Sex.isValidSex("f")); // lowercase female
    }

    @Test
    public void equals() {
        Sex sex = new Sex("M");

        // same values -> returns true
        assertTrue(sex.equals(new Sex("M")));

        // same object -> returns true
        assertTrue(sex.equals(sex));

        // null -> returns false
        assertFalse(sex.equals(null));

        // different types -> returns false
        assertFalse(sex.equals(5.0f));

        // different values -> returns false
        assertFalse(sex.equals(new Sex("F")));
    }
}
