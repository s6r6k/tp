package doctorwho.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ConditionTest {

    @Test
    public void constructor_validName_success() {
        Condition condition = new Condition("Asthma");
        assertEquals("[Asthma]", condition.toString());
    }
}
