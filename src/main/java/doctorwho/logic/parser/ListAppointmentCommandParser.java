package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DATE;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import doctorwho.logic.commands.ListAppointmentCommand;
import doctorwho.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListAppointmentCommand object.
 */
public class ListAppointmentCommandParser implements Parser<ListAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListAppointmentCommand
     * and returns a ListAppointmentCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ListAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_DATE);

        if (!argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListAppointmentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_APPOINTMENT_DATE);

        if (argMultimap.getValue(PREFIX_APPOINTMENT_DATE).isPresent()) {
            LocalDate appointmentDate = ParserUtil.parseDate(
                    argMultimap.getValue(PREFIX_APPOINTMENT_DATE).get());
            return new ListAppointmentCommand(appointmentDate);
        }

        return new ListAppointmentCommand();
    }
}
