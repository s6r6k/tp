package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICALCONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Allergy;
import seedu.address.model.tag.GeneralTag;
import seedu.address.model.tag.MedicalCondition;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing patient in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the patient identified "
            + "by the index number used in the displayed patient list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]..."
            + "[" + PREFIX_ALLERGY + "ALLERGY]..."
            + "[" + PREFIX_MEDICALCONDITION + "MEDICALCONDITION]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This patient already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the patient in the filtered patient list to edit
     * @param editPersonDescriptor details to edit the patient with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Patient editedPatient = createEditedPerson(patientToEdit, editPersonDescriptor);

        if (!patientToEdit.isSamePerson(editedPatient) && model.hasPerson(editedPatient)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(patientToEdit, editedPatient);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPatient)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Patient createEditedPerson(Patient patientToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert patientToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(patientToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(patientToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(patientToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(patientToEdit.getAddress());

        Set<Tag> existingTags = patientToEdit.getTags();

        //Keep the existing tags of each type unless that specific type is being replaced
        Set<Tag> existingGeneralTags = existingTags.stream()
                .filter(t -> t instanceof GeneralTag)
                .collect(Collectors.toSet());
        Set<Tag> existingAllergies = existingTags.stream()
                .filter(t -> t instanceof Allergy)
                .collect(Collectors.toSet());
        Set<Tag> existingMedicalConditions = existingTags.stream()
                .filter(t -> t instanceof MedicalCondition)
                .collect(Collectors.toSet());

        Set<Tag> finalGeneralTags = editPersonDescriptor.getGeneralTags().orElse(existingGeneralTags);
        Set<Tag> finalAllergies = editPersonDescriptor.getAllergies().orElse(existingAllergies);
        Set<Tag> finalMedicalConditions = editPersonDescriptor.getMedicalConditions().orElse(existingMedicalConditions);

        Set<Tag> updatedTags = new HashSet<>();
        updatedTags.addAll(finalGeneralTags);
        updatedTags.addAll(finalAllergies);
        updatedTags.addAll(finalMedicalConditions);

        return new Patient(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the patient with. Each non-empty field value will replace the
     * corresponding field value of the patient.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> generalTags;
        private Set<Tag> allergies;
        private Set<Tag> medicalConditions;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setGeneralTags(toCopy.generalTags);
            setAllergies(toCopy.allergies);
            setMedicalConditions(toCopy.medicalConditions);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address,
                    generalTags, allergies, medicalConditions);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code generalTags} to this object's {@code generalTags}.
         * A defensive copy of {@code generalTags} is used internally.
         */
        public void setGeneralTags(Set<Tag> generalTags) {
            this.generalTags = (generalTags != null) ? new HashSet<>(generalTags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code generalTags} is null.
         */
        public Optional<Set<Tag>> getGeneralTags() {
            return (generalTags != null) ? Optional.of(Collections.unmodifiableSet(generalTags)) : Optional.empty();
        }

        /**
         * Sets {@code allergies} to this object's {@code allergies}.
         * A defensive copy of {@code allergies} is used internally.
         */
        public void setAllergies(Set<Tag> allergies) {
            this.allergies = (allergies != null) ? new HashSet<>(allergies) : null;
        }

        /**
         * Returns an unmodifiable allergy set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code allergies} is null.
         */
        public Optional<Set<Tag>> getAllergies() {
            return (allergies != null) ? Optional.of(Collections.unmodifiableSet(allergies)) : Optional.empty();
        }

        /**
         * Sets {@code medicalConditions} to this object's {@code medicalConditions}.
         * A defensive copy of {@code medicalConditions} is used internally.
         */
        public void setMedicalConditions(Set<Tag> medicalConditions) {
            this.medicalConditions = (medicalConditions != null) ? new HashSet<>(medicalConditions) : null;
        }

        /**
         * Returns an unmodifiable medical condition set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code medicalConditions} is null.
         */
        public Optional<Set<Tag>> getMedicalConditions() {
            return (medicalConditions != null)
                    ? Optional.of(Collections.unmodifiableSet(medicalConditions)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(generalTags, otherEditPersonDescriptor.generalTags)
                    && Objects.equals(allergies, otherEditPersonDescriptor.allergies)
                    && Objects.equals(medicalConditions, otherEditPersonDescriptor.medicalConditions);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("generalTags", generalTags)
                    .add("allergies", allergies)
                    .add("medicalConditions", medicalConditions)
                    .toString();
        }
    }
}
