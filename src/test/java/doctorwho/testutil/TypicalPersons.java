package doctorwho.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import doctorwho.model.AddressBook;
import doctorwho.model.patient.Patient;

import static doctorwho.logic.commands.CommandTestUtil.*;

/**
 * A utility class containing a list of {@code Patient} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Patient ALICE = new PatientBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withAllergies(VALID_ALLERGY_ASPIRIN).withConditions(VALID_CONDITION_DIABETES).build();
    public static final Patient BENSON = new PatientBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withAllergies(VALID_ALLERGY_IBUPROFEN, VALID_ALLERGY_ASPIRIN).build();
    public static final Patient CARL = new PatientBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").withConditions(VALID_CONDITION_ASTHMA).build();
    public static final Patient DANIEL = new PatientBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withAllergies(VALID_ALLERGY_IBUPROFEN).build();
    public static final Patient ELLE = new PatientBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").build();
    public static final Patient FIONA = new PatientBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo")
        .withAllergies(VALID_ALLERGY_ASPIRIN, VALID_ALLERGY_PENICILLIN).withConditions(VALID_CONDITION_HYPERTENSION).build();
    public static final Patient GEORGE = new PatientBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").build();

    // Manually added
    public static final Patient HOON = new PatientBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Patient IDA = new PatientBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Patient AMY = new PatientBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withAllergies(VALID_ALLERGY_ASPIRIN).
        withConditions(VALID_CONDITION_ASTHMA).build();
    public static final Patient BOB = new PatientBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withAllergies(VALID_ALLERGY_IBUPROFEN, VALID_ALLERGY_ASPIRIN).withConditions(VALID_CONDITION_HYPERTENSION)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical patients.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Patient patient : getTypicalPersons()) {
            ab.addPerson(patient);
        }
        return ab;
    }

    public static List<Patient> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
