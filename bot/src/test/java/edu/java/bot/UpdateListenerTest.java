package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.httpclient.ScrapperClient;
import edu.java.bot.httpclient.dto.Response.LinkResponse;
import edu.java.bot.httpclient.dto.Response.ListLinksResponse;
import edu.java.bot.pengrad.UpdateListener;
import edu.java.bot.pengrad.command.Command;
import edu.java.bot.pengrad.command.HelpCommand;
import edu.java.bot.pengrad.command.ListCommand;
import edu.java.bot.pengrad.command.StartCommand;
import edu.java.bot.pengrad.command.TrackCommand;
import edu.java.bot.pengrad.command.UntrackCommand;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private ScrapperClient scrapperClient;

    @Mock
    private List<Command> commands;

    private UpdateListener updateListener;
    private final List<Command> supportedCommands = List.of(
        new StartCommand(scrapperClient),
        new ListCommand(scrapperClient),
        new TrackCommand(scrapperClient),
        new UntrackCommand(scrapperClient)
    );

    public void setUpScrapperClient() throws URISyntaxException {
        when(scrapperClient.register(any())).thenReturn(Mono.empty());
        when(scrapperClient.addLink(any(), eq("https://github.com/owner/repo")))
            .thenReturn(Mono.just(new LinkResponse(0L, new URI("https://github.com/owner/repo"))));
        when(scrapperClient.removeLink(any(), eq("https://github.com/owner/repo")))
            .thenReturn(Mono.just(new LinkResponse(0L, new URI("https://github.com/owner/repo"))));
        when(scrapperClient.getLinks(any())).thenReturn(Mono.just(new ListLinksResponse(0, List.of())));
    }

    @BeforeEach
    public void setUp() throws URISyntaxException {
        MockitoAnnotations.openMocks(this);
        setUpScrapperClient();
        updateListener = new UpdateListener(commands, bot);
        bot.setUpdatesListener(updateListener);
    }

    @Test
    public void testStart() {
        successfulCommandTest(
            Stream.of(new StartCommand(scrapperClient)),
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
            Stream.of(new TrackCommand(scrapperClient)),
            "/track https://github.com/owner/repo",
            "Link https://github.com/owner/repo successfully added for tracking"
        );
    }

    @Test
    public void testUntrack() {
        successfulCommandTest(
            Stream.of(new UntrackCommand(scrapperClient)),
            "/untrack https://github.com/owner/repo",
            "Link https://github.com/owner/repo successfully removed from tracking"
        );
    }

    @Test
    public void testList() {
        successfulCommandTest(
            Stream.of(new ListCommand(scrapperClient)),
            "/list",
            "No links are being tracked"
        );
    }

    @Test
    public void testInvalidNumberOfArguments() {
        successfulCommandTest(
            Stream.of(new UntrackCommand(null)),
            "/untrack",
            "Invalid command. Usage: /untrack <link>"
        );
        successfulCommandTest(
            Stream.of(new TrackCommand(null)),
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
