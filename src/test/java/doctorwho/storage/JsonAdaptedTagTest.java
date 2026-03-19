package doctorwho.storage;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import doctorwho.commons.exceptions.IllegalValueException;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

public class JsonAdaptedTagTest {

    @Test
    public void toModelType_validAllergyTag_returnsAllergy() throws Exception {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("allergy:Aspirin");
        Tag modelTag = adaptedTag.toModelType();

        assertEquals(Allergy.class, modelTag.getClass());
    }

    @Test
    public void toModelType_validConditionTag_returnsCondition() throws Exception {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("condition:asthma");
        Tag modelTag = adaptedTag.toModelType();

        assertEquals(Condition.class, modelTag.getClass());
    }

    @Test
    public void toModelType_invalidTag_throwsException() {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("#invalid");

        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalValueException.class,
            adaptedTag::toModelType
        );
    }

    @Test
    public void toModelType_unknownPrefix_throwsException() {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("unknown:sometag");
        assertThrows(
            IllegalValueException.class,
            adaptedTag::toModelType
        );
    }

    @Test
    public void toJson_jsonCreatorConstructor_returnsTagNameOnly() throws Exception {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag(VALID_ALLERGY_ASPIRIN);
        assertEquals(VALID_ALLERGY_ASPIRIN, adaptedTag.toJson());
    }

    @Test
    public void constructor_fromModelTag_createsCorrectType() throws Exception {
        Tag allergy = new Allergy(VALID_ALLERGY_ASPIRIN);
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag(allergy);

        Tag result = adaptedTag.toModelType();

        assertEquals(Allergy.class, result.getClass());
    }

    @Test
    public void toJson_conditionTag_returnsConditionPrefix() throws Exception {
        Tag condition = new Condition("asthma");
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag(condition);
        assertEquals("condition:asthma", adaptedTag.toJson());
    }

    @Test
    public void toJson_allergyTag_returnsAllergyPrefix() throws Exception {
        Tag allergy = new Allergy(VALID_ALLERGY_ASPIRIN);
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag(allergy);
        assertEquals("allergy:Aspirin", adaptedTag.toJson());
    }
}
