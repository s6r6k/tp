package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.patient.Patient;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code DeleteAppointmentCommand}.
 */
public class DeleteAppointmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Patient patientToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteAppointmentCommand command = new DeleteAppointmentCommand(INDEX_FIRST_PERSON);

        Patient updatedPatient = new PatientBuilder(patientToEdit)
                .withAppointment(null)
                .build();

        String expectedMessage = String.format(DeleteAppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
                updatedPatient.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, updatedPatient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getAppointment().isEmpty());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteAppointmentCommand command = new DeleteAppointmentCommand(outOfBoundIndex);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_patientHasNoAppointment_success() {
        Patient originalPatient = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Patient patientWithoutAppointment = new PatientBuilder(originalPatient)
                .withAppointment(null)
                .build();
        model.setPerson(originalPatient, patientWithoutAppointment);

        DeleteAppointmentCommand command = new DeleteAppointmentCommand(INDEX_FIRST_PERSON);
        String expectedMessage = String.format(DeleteAppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
                patientWithoutAppointment.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getAppointment().isEmpty());
    }
}
