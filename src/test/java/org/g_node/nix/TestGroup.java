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

        List<Source> filtered = group.getSources((s) -> s.getName().endsWith("a") || s.getName().endsWith("d"));
        assertEquals(filtered.size(), 2);

        for (String id: ids) {
            Source s = group.getSource(id);
            assertEquals(s.getId(), id);
            group.removeSource(s);
        }

        assertEquals(group.getSourceCount(), 0);
        assertEquals(group.getSources().size(), 0);
    }

    @Test
    public void testDataArrayAccess() {
        List<String> names = Arrays.asList("array_a", "array_b", "array_c", "array_d", "array_e");

        assertEquals(group.getDataArrayCount(), 0);
        assertTrue(group.getDataArrays().isEmpty());
        assertFalse(group.hasDataArray(names.get(0)));

        List<String> ids = new ArrayList<>();
        for (String name: names) {
            DataArray da = block.createDataArray(name, "some_type", DataType.Double, new NDSize(new int[]{0}));
            ids.add(da.getId());
            group.addDataArray(da);
            assertEquals(group.getDataArray(name), da);
        }

        assertEquals(group.getDataArrayCount(), names.size());
        assertEquals(group.getDataArrays().size(), names.size());

        List<DataArray> filtered = group.getDataArrays((da) -> da.getName().endsWith("a") || da.getName().endsWith("d"));
        assertEquals(filtered.size(), 2);

        for (String id: ids) {
            DataArray da = group.getDataArray(id);
            assertEquals(da.getId(), id);
            group.removeDataArray(da);
        }

        assertEquals(group.getDataArrayCount(), 0);
        assertEquals(group.getDataArrays().size(), 0);
    }

    @Test
    public void testTagAccess() {
        List<String> tagNames = Arrays.asList("tag_a", "tag_b", "tag_c", "tag_d", "tag_e");

        assertEquals(group.getTagCount(), 0);
        assertTrue(group.getTags().isEmpty());
        assertFalse(group.hasTag(tagNames.get(0)));

        List<String> ids = new ArrayList<>();
        for (String name: tagNames) {
            DataArray da = block.createDataArray(name.replace("tag", "array"), "some_type", DataType.Double, new NDSize(new int[]{0}));
            Tag tag = block.createTag(name, "some_type", new double[]{0});
            tag.addReference(da);
            ids.add(tag.getId());
            group.addTag(tag);
            assertEquals(group.getTag(name), tag);
        }

        assertEquals(group.getTagCount(), tagNames.size());
        assertEquals(group.getTags().size(), tagNames.size());

        List<Tag> filtered = group.getTags((tag) -> tag.getName().endsWith("a") || tag.getName().endsWith("d"));
        assertEquals(filtered.size(), 2);

        for (String id: ids) {
            Tag tag = group.getTag(id);
            assertEquals(tag.getId(), id);
            group.removeTag(tag);
        }

        assertEquals(group.getTagCount(), 0);
        assertEquals(group.getTags().size(), 0);
    }

    @Test
    public void testMultiTagAccess() {
        List<String> tagNames = Arrays.asList("tag_a", "tag_b", "tag_c", "tag_d", "tag_e");

        assertEquals(group.getMultiTagCount(), 0);
        assertTrue(group.getMultiTags().isEmpty());
        assertFalse(group.hasMultiTag(tagNames.get(0)));

        List<String> ids = new ArrayList<>();
        for (String name: tagNames) {
            DataArray da = block.createDataArray(name.replace("tag", "array"), "some_type", DataType.Double, new NDSize(new int[]{0}));
            DataArray pos = block.createDataArray(name.replace("tag", "pos"), "some_type", DataType.Double, new NDSize(new int[]{0}));
            MultiTag tag = block.createMultiTag(name, "some_type", pos);
            tag.addReference(da);
            ids.add(tag.getId());
            group.addMultiTag(tag);
            assertEquals(group.getMultiTag(name), tag);
        }

        assertEquals(group.getMultiTagCount(), tagNames.size());
        assertEquals(group.getMultiTags().size(), tagNames.size());

        List<MultiTag> filtered = group.getMultiTags((tag) -> tag.getName().endsWith("a") || tag.getName().endsWith("d"));
        assertEquals(filtered.size(), 2);

        for (String id: ids) {
            MultiTag tag = group.getMultiTag(id);
            assertEquals(tag.getId(), id);
            group.removeMultiTag(tag);
        }

        assertEquals(group.getMultiTagCount(), 0);
        assertEquals(group.getMultiTags().size(), 0);
    }

}
