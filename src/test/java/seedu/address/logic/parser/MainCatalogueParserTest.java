package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_FILTER_BY_ASSIGNEE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_FILTER_BY_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_FILTER_BY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_PROGRESS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PROJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalTasks.PLAN_MEETING;
import static seedu.address.testutil.TypicalTasks.VALID_TASK_DEADLINE;
import static seedu.address.testutil.TypicalTasks.VALID_TASK_NAME;
import static seedu.address.testutil.TypicalTasks.VALID_TASK_PROGRESS_HALF;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.global.AddCommand;
import seedu.address.logic.commands.global.ClearCommand;
import seedu.address.logic.commands.global.DeleteCommand;
import seedu.address.logic.commands.global.EditCommand;
import seedu.address.logic.commands.global.EditCommand.EditProjectDescriptor;
import seedu.address.logic.commands.global.ExitCommand;
import seedu.address.logic.commands.global.FindCommand;
import seedu.address.logic.commands.global.HelpCommand;
import seedu.address.logic.commands.global.ListCommand;
import seedu.address.logic.commands.global.StartCommand;
import seedu.address.logic.commands.project.AddTaskCommand;
import seedu.address.logic.commands.project.AssignCommand;
import seedu.address.logic.commands.project.EditTaskCommand;
import seedu.address.logic.commands.project.FilterCommand;
import seedu.address.logic.commands.project.LeaveCommand;
import seedu.address.logic.commands.project.NewTeammateCommand;
import seedu.address.logic.commands.project.ViewTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Status;
import seedu.address.model.exceptions.InvalidScopeException;
import seedu.address.model.project.NameContainsKeywordsPredicate;
import seedu.address.model.project.Project;
import seedu.address.testutil.EditProjectDescriptorBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.ProjectBuilder;
import seedu.address.testutil.ProjectUtil;
import seedu.address.testutil.TaskUtil;

public class MainCatalogueParserTest {

    private final MainCatalogueParser parser = new MainCatalogueParser();

    @Test
    public void parseCommand_add() throws Exception {
        Project project = new ProjectBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(ProjectUtil.getAddCommand(project), Status.CATALOGUE);
        assertEquals(new AddCommand(project), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD, Status.CATALOGUE) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3", Status.CATALOGUE) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PROJECT.getOneBased(), Status.CATALOGUE);
        assertEquals(new DeleteCommand(INDEX_FIRST_PROJECT), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Project project = new ProjectBuilder().build();
        EditProjectDescriptor descriptor = new EditProjectDescriptorBuilder(project).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PROJECT.getOneBased() + " " + ProjectUtil.getEditProjectDescriptorDetails(descriptor),
                Status.CATALOGUE);
        assertEquals(new EditCommand(INDEX_FIRST_PROJECT, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD, Status.CATALOGUE) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3", Status.CATALOGUE) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD, Status.PROJECT) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3", Status.PROJECT) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")), Status.CATALOGUE);
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD, Status.CATALOGUE) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3", Status.CATALOGUE) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD, Status.PROJECT) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3", Status.PROJECT) instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD, Status.CATALOGUE) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3", Status.CATALOGUE) instanceof ListCommand);
    }

    @Test
    public void parseCommand_start() throws Exception {
        StartCommand command = (StartCommand) parser.parseCommand(
                StartCommand.COMMAND_WORD + " " + INDEX_FIRST_PROJECT.getOneBased(), Status.CATALOGUE);
        assertEquals(new StartCommand(INDEX_FIRST_PROJECT), command);
    }

    @Test
    public void parseCommand_leave() throws Exception {
        assertTrue(parser.parseCommand(LeaveCommand.COMMAND_WORD, Status.PROJECT) instanceof LeaveCommand);
        assertTrue(parser.parseCommand(LeaveCommand.COMMAND_WORD + " 3", Status.PROJECT) instanceof LeaveCommand);
    }

    @Test
    public void parseCommand_assign() throws Exception {
        AssignCommand command = (AssignCommand) parser.parseCommand(
                AssignCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased() + " " + ALICE.getPersonName(),
                Status.PROJECT);
        assertEquals(new AssignCommand(INDEX_FIRST_TASK, ALICE.getPersonName().toString()), command);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        // FilterCommand does not have equal method as one cannot compare two predicates unless they are identical
        assertTrue(parser.parseCommand(FilterCommand.COMMAND_WORD + " "
            + PREFIX_TASK_FILTER_BY_ASSIGNEE + ALICE.getPersonName(), Status.PROJECT) instanceof FilterCommand);
        assertTrue(parser.parseCommand(FilterCommand.COMMAND_WORD + " "
            + PREFIX_TASK_FILTER_BY_DEADLINE + VALID_TASK_DEADLINE, Status.PROJECT) instanceof FilterCommand);
        assertTrue(parser.parseCommand(FilterCommand.COMMAND_WORD + " "
            + PREFIX_TASK_FILTER_BY_NAME + VALID_TASK_NAME, Status.PROJECT) instanceof FilterCommand);
    }

    @Test
    public void parseCommand_viewtask() throws Exception {
        ViewTaskCommand command = (ViewTaskCommand) parser.parseCommand(
                ViewTaskCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased(), Status.PROJECT
        );
        assertEquals(new ViewTaskCommand(INDEX_FIRST_TASK), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand("", Status.CATALOGUE));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand",
                Status.CATALOGUE));
    }

    @Test
    public void parseCommand_invalidScope_throwsInvalidScopeException() {

        try {
            parser.parseCommand(
                    AssignCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased() + " " + ALICE.getPersonName(),
                    Status.CATALOGUE);
            fail();
        } catch (Exception e) {
            assertEquals(new InvalidScopeException(Status.PROJECT, Status.CATALOGUE), e);
        }

        try {
            parser.parseCommand(
                FilterCommand.COMMAND_WORD + " " + PREFIX_TASK_FILTER_BY_ASSIGNEE + ALICE.getPersonName(),
                Status.CATALOGUE);
            fail();
        } catch (Exception e) {
            assertEquals(new InvalidScopeException(Status.PROJECT, Status.CATALOGUE), e);
        }

        try {
            parser.parseCommand(NewTeammateCommand.COMMAND_WORD + " "
                    + PersonUtil.getCommandInfo(ALICE), Status.CATALOGUE);
            fail();
        } catch (Exception e) {
            assertEquals(new InvalidScopeException(Status.PROJECT, Status.CATALOGUE), e);
        }

        try {
            parser.parseCommand(AddTaskCommand.COMMAND_WORD + " "
                    + TaskUtil.getTaskCommand(PLAN_MEETING), Status.CATALOGUE);
            fail();
        } catch (Exception e) {
            assertEquals(new InvalidScopeException(Status.PROJECT, Status.CATALOGUE), e);
        }

        try {
            parser.parseCommand(EditTaskCommand.COMMAND_WORD + " " + PREFIX_TASK_PROGRESS
                    + " " + VALID_TASK_PROGRESS_HALF, Status.CATALOGUE);
            fail();
        } catch (Exception e) {
            assertEquals(new InvalidScopeException(Status.PROJECT, Status.CATALOGUE), e);
        }

        try {
            parser.parseCommand(ViewTaskCommand.COMMAND_WORD + " " + INDEX_FIRST_TASK.getOneBased(),
                    Status.CATALOGUE);
            fail();
        } catch (Exception e) {
            assertEquals(new InvalidScopeException(Status.PROJECT, Status.CATALOGUE), e);
        }
    }
}
