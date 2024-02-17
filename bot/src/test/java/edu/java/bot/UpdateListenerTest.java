package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.pengrad.UpdateListener;
import edu.java.bot.pengrad.command.Command;
import edu.java.bot.pengrad.command.HelpCommand;
import edu.java.bot.pengrad.command.ListCommand;
import edu.java.bot.pengrad.command.StartCommand;
import edu.java.bot.pengrad.command.TrackCommand;
import edu.java.bot.pengrad.command.UntrackCommand;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateListenerTest {

    @Mock
    private TelegramBot bot;

    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @Mock
    private List<Command> commands;

    private UpdateListener updateListener;
    private final List<Command> supportedCommands = List.of(
        new StartCommand(),
        new ListCommand(),
        new TrackCommand(),
        new UntrackCommand()
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        updateListener = new UpdateListener(commands, bot);
        bot.setUpdatesListener(updateListener);
    }

    @Test
    public void testStart() {
        successfulCommandTest(
            Stream.of(new StartCommand()),
            "/start",
            """
                Hello! I'm a Link Listener bot.
                I'm keep track of updates of provided links.
                Use /help to see available commands
                """
        );
    }

    @Test
    public void testHelp() {
        var response = supportedCommands.stream()
            .map(command -> command.command() + " - " + command.helpInfo())
            .reduce((s1, s2) -> s1 + "\n\n" + s2)
            .orElse("No commands available");

        successfulCommandTest(
            Stream.of(new HelpCommand(supportedCommands)),
            "/help",
            response
        );
    }

    @Test
    public void testUnknownCommand() {
        unknownCommand(List.of(
                "/unknown",
                "/start!",
                "/help?",
                "/trackLink",
                "/untrackLink",
                "/listLinks",
                ".",
                "abacaba"
            )
        );
    }

    @Test
    public void testTrack() {
        successfulCommandTest(
            Stream.of(new TrackCommand()),
            "/track link",
            "Track command not supported yet."
        );
    }

    @Test
    public  void testUntrack() {
        successfulCommandTest(
            Stream.of(new UntrackCommand()),
            "/untrack link",
            "Untrack command not supported yet."
        );
    }

    @Test
    public void testList() {
        successfulCommandTest(
            Stream.of(new ListCommand()),
            "/list",
            "List command not supported yet."
        );
    }

    @Test
    public void testInvalidNumberOfArguments() {
        successfulCommandTest(
            Stream.of(new UntrackCommand()),
            "/untrack",
            "Invalid command. Usage: /untrack <link>"
        );
        successfulCommandTest(
            Stream.of(new TrackCommand()),
            "/track",
            "Invalid command. Usage: /track <link>"
        );
    }

    private void unknownCommand(List<String> commandNames) {
        commandNames.forEach(commandName ->
            successfulCommandTest(
                supportedCommands.stream(),
                commandName,
                "Unknown command"
            )
        );
    }

    private void successfulCommandTest(Stream<Command> commands, String commandName, String response) {
        var captor = ArgumentCaptor.forClass(SendMessage.class);
        setUpMocks(commands, commandName);
        updateListener.process(List.of(update));
        verify(bot, atLeast(1)).execute(captor.capture());
        assertThat(captor.getValue().getParameters().get("text").toString()).isEqualTo(response);
    }

    private void setUpMocks(Stream<Command> command, String text) {
        when(commands.stream()).thenReturn(command);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(text);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);
    }
}
