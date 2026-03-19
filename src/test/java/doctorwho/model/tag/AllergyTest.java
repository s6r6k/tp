package doctorwho.model.tag;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AllergyTest {

    @Test
    public void constructor_validName_success() {
        Allergy allergy = new Allergy(VALID_ALLERGY_ASPIRIN);
        assertEquals("[Aspirin]", allergy.toString());
    }
}
