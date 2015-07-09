package org.gnode.nix;

import org.gnode.nix.valid.Result;
import org.gnode.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TestFeature {

    private File file;
    private Block block;
    private DataArray data_array;
    private Tag tag;

    @Before
    public void setUp() {
        file = File.open("test_Feature_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);
        block = file.createBlock("featureTest", "test");

        data_array = block.createDataArray("featureTest", "Test",
                DataType.Double, new NDSize(new int[]{0}));

        tag = block.createTag("featureTest", "Test", new double[]{0.0, 2.0, 3.4});
    }

    @After
    public void tearDown() {
        String location = file.getLocation();

        file.deleteBlock(block.getId());
        file.close();

        // delete file
        java.io.File f = new java.io.File(location);
        f.delete();
    }

    @Test
    public void testValidate() {
        Feature rp = tag.createFeature(data_array, LinkType.Tagged);

        Result result = Validator.validate(rp);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
    }


    @Test
    public void testId() {
        Feature rp = tag.createFeature(data_array, LinkType.Tagged);
        assertEquals(rp.getId().length(), 36);
        tag.deleteFeature(rp.getId());
    }

    @Test
    public void testLinkType() {
        Feature rp = tag.createFeature(data_array, LinkType.Tagged);
        assertEquals(rp.getLinkType(), LinkType.Tagged);
        rp.setLinkType(LinkType.Untagged);

        assertEquals(rp.getLinkType(), LinkType.Untagged);
        rp.setLinkType(LinkType.Tagged);
        assertEquals(rp.getLinkType(), LinkType.Tagged);

        rp.setLinkType(LinkType.Indexed);
        assertEquals(rp.getLinkType(), LinkType.Indexed);

        tag.deleteFeature(rp.getId());
    }

    @Test
    public void testData() {
        DataArray a = null;
        Feature f = null;

        try {
            tag.createFeature(a, LinkType.Tagged);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            f.setData(a);
            fail();
        } catch (RuntimeException re) {
        }

        Feature rp = tag.createFeature(data_array, LinkType.Tagged);
        DataArray da_2 = block.createDataArray("array2", "Test",
                DataType.Double, new NDSize(new int[]{0}));
        assertEquals(rp.getData().getId(), data_array.getId());
        rp.setData(da_2);
        assertEquals(rp.getData().getId(), da_2.getId());
        block.deleteDataArray(da_2.getId());
        // make sure link is gone with deleted data array
        try {
            rp.getData();
            fail();
        } catch (RuntimeException re) {
        }
        tag.deleteFeature(rp.getId());
    }

    @Test
    public void testLinkType2Str() {
        assertEquals(Feature.linkTypeToString(LinkType.Tagged), "Tagged");
        assertEquals(Feature.linkTypeToString(LinkType.Untagged), "Untagged");
        assertEquals(Feature.linkTypeToString(LinkType.Indexed), "Indexed");
    }
}