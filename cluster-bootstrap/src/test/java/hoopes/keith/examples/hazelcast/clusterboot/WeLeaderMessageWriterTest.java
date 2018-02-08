package hoopes.keith.examples.hazelcast.clusterboot;

import hoopes.keith.examples.hazelcast.clusterboot.beans.WeLeaderMessageWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.PrintStream;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;

@TestInstance(PER_CLASS)
class WeLeaderMessageWriterTest{

    @Test
    void write(){

        PrintStream out = mock(PrintStream.class);
        WeLeaderMessageWriter writer = () -> out.println("We Are Started!");
        writer.write();

        verify(out, only()).println("We Are Started!");
    }
}