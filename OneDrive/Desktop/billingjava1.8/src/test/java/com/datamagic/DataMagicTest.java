package com.datamagic;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;


import java.io.IOException;
import java.net.URISyntaxException;

public class DataMagicTest {



    @Test
    public void testApiStringToJson() throws URISyntaxException, IOException, InterruptedException {
        // Arrange: Prepare the test data (input API string)
        String apiString = "{\"statusCode\":200,\"body\":\"[\\\"{\\\\\\\"id\\\\\\\":\\\\\\\"BSMGd8cb7050-209f-44b9-9c90-04a1695217e0\\\\\\\",\\\\\\\"to\\\\\\\":\\\\\\\"billing%grp1@cyberthentic.com\\\\\\\",\\\\\\\"from\\\\\\\":\\\\\\\"operations%grp1@cyberthentic.com\\\\\\\",\\\\\\\"body\\\\\\\":\\\\\\\"Hello\\\\\\\",\\\\\\\"time\\\\\\\":\\\\\\\"16:00:16.296215300\\\\\\\"}\\\",\\\"{\\\\\\\"id\\\\\\\":\\\\\\\"BSMG89dc672d-3ae7-4da3-a000-92760b9ab80f\\\\\\\",\\\\\\\"to\\\\\\\":\\\\\\\"billing%grp1@cyberthentic.com\\\\\\\",\\\\\\\"from\\\\\\\":\\\\\\\"operations%grp1@cyberthentic.com\\\\\\\",\\\\\\\"body\\\\\\\":\\\\\\\"Hello DataMagic Demo1 for friday 7/21\\\\\\\",\\\\\\\"time\\\\\\\":\\\\\\\"21:11:22.674421900\\\\\\\"}\\\"]\"}";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] result = dataMagic.apiStringToJson(apiString);

        // Assert: Verify the expected outcome
        String[] expectedArray = {
                "{\"id\":\"BSMGd8cb7050-209f-44b9-9c90-04a1695217e0\",\"to\":\"billing%grp1@cyberthentic.com\",\"from\":\"operations%grp1@cyberthentic.com\",\"body\":\"Hello\",\"time\":\"16:00:16.296215300\"}",
                "{\"id\":\"BSMG89dc672d-3ae7-4da3-a000-92760b9ab80f\",\"to\":\"billing%grp1@cyberthentic.com\",\"from\":\"operations%grp1@cyberthentic.com\",\"body\":\"Hello DataMagic Demo1 for friday 7/21\",\"time\":\"21:11:22.674421900\"}"
        };
        Assertions.assertArrayEquals(expectedArray, result, "Array conversion failed.");
    }


    @Test
    public void testApiStringToJson_EmptyString() throws URISyntaxException, IOException, InterruptedException {
        // Arrange: Prepare the test data (empty input API string)
        String apiString = "";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] result = dataMagic.apiStringToJson(apiString);

        // Assert: Verify the expected outcome
        String[] expectedArray = {};
        Assertions.assertArrayEquals(expectedArray, result, "Array should be empty for empty input.");
    }

    @Test
    public void testApiStringToJson_NullString() throws URISyntaxException, IOException, InterruptedException {
        // Arrange: Prepare the test data (null input API string)
        String apiString = null;

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] result = dataMagic.apiStringToJson(null);

        // Assert: Verify the expected outcome
        String[] expectedArray = {};
        Assertions.assertArrayEquals(expectedArray, result, "Array should be empty for null input.");
    }


    @Test
    public void testGetListOfIds_WithValidJson() throws URISyntaxException, IOException, InterruptedException {
        // Arrange: Prepare the test data (valid JSON containing IDs)
        String json = "[{\"id\":\"123\",\"name\":\"John\"},{\"id\":\"456\",\"name\":\"Alice\"}]";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] ids = dataMagic.getListOfIds(json);

        // Assert: Verify the expected outcome
        String[] expectedIds = {"123", "456"};
        Assertions.assertArrayEquals(expectedIds, ids, "Extracting IDs from JSON failed.");
    }

    @Test
    public void testGetListOfIds_WithInvalidJson() throws URISyntaxException, IOException, InterruptedException {
        // Arrange: Prepare the test data (invalid JSON)
        String json = "This is not valid JSON";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] ids = dataMagic.getListOfIds(json);

        // Assert: Verify the expected outcome
        String[] expectedIds = {};
        Assertions.assertArrayEquals(expectedIds, ids, "Should return an empty array for invalid JSON.");
    }

    @Test
    public void testGetListOfIds_WithNoIds() throws URISyntaxException, IOException, InterruptedException {
        // Arrange: Prepare the test data (JSON without any IDs)
        String json = "[{\"name\":\"John\"},{\"name\":\"Alice\"}]";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] ids = dataMagic.getListOfIds(json);

        // Assert: Verify the expected outcome
        String[] expectedIds = {};
        Assertions.assertArrayEquals(expectedIds, ids, "Should return an empty array when no IDs are found.");
    }
    @Test
    public void testGetListOfMessageNames_WithValidJson() {
        // Arrange: Prepare the test data (valid JSON containing file names)
        String json = "[{\"id\":\"BSMG2344gt-9uyw6-hwy1\",\"to\":\"operations%grp1@cyberthentic.com\",\"from\":\"operations%grp1@cyberthentic.com\",\"body\":\"Happy August Pemberton\",\"time\":\"21:21:07.167881700\",\"fileName\":\"AGNT8935i-jhsgw738-kus42v91-nbs7329op-ip09we23/0000000-COUR2682afb3-b049-41de-b1ad-075870cb8787-DOMN10-134te508875-4545-eg0234a-003-1691108738667-307328.json\"}]";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] fileNames = dataMagic.getListOfMessageNames(json);

        // Assert: Verify the expected outcome
        String[] expectedFileNames = {"AGNT8935i-jhsgw738-kus42v91-nbs7329op-ip09we23/0000000-COUR2682afb3-b049-41de-b1ad-075870cb8787-DOMN10-134te508875-4545-eg0234a-003-1691108738667-307328.json"};
        Assertions.assertArrayEquals(expectedFileNames, fileNames, "Extracting file names from JSON failed.");
    }

    @Test
    public void testGetListOfMessageNames_WithInvalidJson() {
        // Arrange: Prepare the test data (invalid JSON)
        String json = "This is not valid JSON";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] fileNames = dataMagic.getListOfMessageNames(json);

        // Assert: Verify the expected outcome
        String[] expectedFileNames = {};
        Assertions.assertArrayEquals(expectedFileNames, fileNames, "Should return an empty array for invalid JSON.");
    }

    @Test
    public void testGetListOfMessageNames_WithNoFileNames() {
        // Arrange: Prepare the test data (JSON without any file names)
        String json = "[{\"id\":\"BSMG2344gt-9uyw6-hwy1\",\"to\":\"operations%grp1@cyberthentic.com\",\"from\":\"operations%grp1@cyberthentic.com\",\"body\":\"Happy August Pemberton\",\"time\":\"21:21:07.167881700\"}]";

        // Act: Call the function being tested
        DataMagic dataMagic = new DataMagic();
        String[] fileNames = dataMagic.getListOfMessageNames(json);

        // Assert: Verify the expected outcome
        String[] expectedFileNames = {};
        Assertions.assertArrayEquals(expectedFileNames, fileNames, "Should return an empty array when no file names are found.");
    }



}
