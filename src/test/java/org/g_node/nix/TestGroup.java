package org.g_node.nix;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static org.g_node.nix.util.Assert.assertThrows;
import static org.junit.Assert.*;

import net.jcip.annotations.NotThreadSafe;
import org.junit.*;


@NotThreadSafe
public class TestGroup {

    private File file;
    private Block block;
    private Group group;
    private Section section;

    @Before
    public void setUp() {
        file = File.open("test_Group_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);
        block = file.createBlock("block_one", "dataset");
        group = block.createGroup("group_one", "trial");
        section = file.createSection("session01", "RecordingSession");
        section.createProperty("experimenter", new Value("me"));
    }

    @After
    public void tearDown() throws IOException {
        String location = file.getLocation();
        file.close();
        Files.delete(Paths.get(location));
    }

    @Test
    public void testId() {
        assertEquals(group.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(group.getName(), "group_one");
    }

    @Test
    public void testType() {
        assertEquals(group.getType(), "trial");
        group.setType("something_else");
        assertEquals(group.getType(), "something_else");
    }

    @Test
    public void testMetadataAccess() {
        assertNull(group.getMetadata());
        group.setMetadata(section);
        assertEquals(group.getMetadata().getName(), "session01");

        group.removeMetadata();
        assertNull(group.getMetadata());

        group.setMetadata(section);
        file.deleteSection(section);
        assertNull(group.getMetadata());
    }

    @Test
    public void testSourcesAccess() {
        List<String> names = Arrays.asList("source_a", "source_b", "source_c", "source_d", "source_e");

        assertEquals(group.getSourceCount(), 0);
        assertTrue(group.getSources().isEmpty());
        assertFalse(group.hasSource(names.get(0)));

        List<String> ids = new ArrayList<>();
        for (String name: names) {
            Source s = block.createSource(name, "some_type");
            ids.add(s.getId());
            group.addSource(s);
            assertEquals(group.getSource(name), s);
        }

        assertEquals(group.getSourceCount(), names.size());
        assertEquals(group.getSources().size(), names.size());

        for (String id: ids) {
            Source s = group.getSource(id);
            assertEquals(s.getId(), id);
            group.removeSource(s);
        }

        assertEquals(group.getSourceCount(), 0);
        assertEquals(group.getSources().size(), 0);
    }

}
