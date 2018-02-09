package hoopes.keith.examples.hazelcast.clusterboot.beans;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.PrintStream;

import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.Mockito.*;

@TestInstance(PER_CLASS)
class WeLeaderMessageWriterTest{

    @Test
    void write(){

        PrintStream out = mock(PrintStream.class);
        WeLeaderMessageWriter writer = () -> out.println("That be a wee cluster boot!");
        writer.write();

        verify(out, only()).println("That be a wee cluster boot!");
    }
}