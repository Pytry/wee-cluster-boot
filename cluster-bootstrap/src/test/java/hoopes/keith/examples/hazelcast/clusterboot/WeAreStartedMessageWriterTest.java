package hoopes.keith.examples.hazelcast.clusterboot;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

class WeAreStartedMessageWriterTest{

    @Test
    void write(){

        PrintStream out = mock(PrintStream.class);
        WeAreStartedMessageWriter writer = () -> out.println("We Are Started!");
        writer.write();

        verify(out, only()).println("We Are Started!");
    }
}