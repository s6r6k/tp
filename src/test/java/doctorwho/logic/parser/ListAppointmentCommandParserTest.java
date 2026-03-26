package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DATE;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseFailure;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import doctorwho.logic.Messages;
import doctorwho.logic.commands.ListAppointmentCommand;

public class ListAppointmentCommandParserTest {

    private ListAppointmentCommandParser parser = new ListAppointmentCommandParser();

    @Test
    public void parse_noArgs_returnsListAllAppointmentsCommand() {
        assertParseSuccess(parser, "", new ListAppointmentCommand());
    }

    @Test
    public void parse_validDate_returnsDateFilteredCommand() {
        assertParseSuccess(parser, " " + PREFIX_APPOINTMENT_DATE + "12-03-2026",
                new ListAppointmentCommand(LocalDate.of(2026, 3, 12)));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_APPOINTMENT_DATE + "2026-03-12",
                ParserUtil.MESSAGE_INVALID_DATE_FORMAT);
    }

    @Test
    public void parse_invalidCalendarDate_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_APPOINTMENT_DATE + "29-02-2026", ParserUtil.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, " abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateDatePrefix_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_APPOINTMENT_DATE + "12-03-2026 "
                        + PREFIX_APPOINTMENT_DATE + "13-03-2026",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_APPOINTMENT_DATE));
    }
}
