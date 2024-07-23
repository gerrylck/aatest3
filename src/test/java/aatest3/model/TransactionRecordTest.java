package aatest3.model;

import aatest3.exception.InvalidTransactionRecordException;
import aatest3.util.FileSpecLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TransactionRecordTest {

    @Test
    void creatingTransactionRecordFromValidLineShouldSucceed2() throws Exception {
        List<FileSpecLoader.FieldSpec> specs = new ArrayList<>();
        // Manually create and set FieldSpec instances
        for (String[] specInfo : new String[][]{
                {"recordCode", "0", "3"},
                {"clientType", "3", "5"},
                {"quantityLong", "5", "8"},
                {"quantityShort", "8", "11"},
                {"filler", "11", "14"} // Assuming filler is optional
        }) {
            FileSpecLoader.FieldSpec spec = new FileSpecLoader.FieldSpec();
            setField(spec, "name", specInfo[0]);
            setField(spec, "start", Integer.parseInt(specInfo[1]));
            setField(spec, "end", Integer.parseInt(specInfo[2]));
            specs.add(spec);
        }

        String validLine = "ABC01005004   ";
        TransactionRecord record = new TransactionRecord(validLine, specs);
        Assertions.assertEquals("ABC", record.getRecordCode());
        Assertions.assertEquals("01", record.getClientType());
        Assertions.assertEquals(5, record.getQuantityLong());
        Assertions.assertEquals(4, record.getQuantityShort());
    }

    // Helper method to set field values using reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void creatingTransactionRecordFromShortLineShouldThrowException() throws Exception {
        List<FileSpecLoader.FieldSpec> specs = new ArrayList<>();
        // Manually create and set FieldSpec instances
        for (String[] specInfo : new String[][]{
                {"recordCode", "0", "3"},
                {"clientType", "3", "5"},
                {"quantityLong", "5", "8"},
                {"quantityShort", "8", "11"}
        }) {
            FileSpecLoader.FieldSpec spec = new FileSpecLoader.FieldSpec();
            setField(spec, "name", specInfo[0]);
            setField(spec, "start", Integer.parseInt(specInfo[1]));
            setField(spec, "end", Integer.parseInt(specInfo[2]));
            specs.add(spec);
        }

        String shortLine = "ABC01";
        Assertions.assertThrows(InvalidTransactionRecordException.class, () -> new TransactionRecord(shortLine, specs));
    }

    @Test
    void creatingTransactionRecordWithInvalidQuantityFormatShouldThrowException() throws Exception {
        List<FileSpecLoader.FieldSpec> specs = new ArrayList<>();
        // Manually create and set FieldSpec instances
        for (String[] specInfo : new String[][]{
                {"quantityLong", "0", "3"},
                {"quantityShort", "3", "6"}
        }) {
            FileSpecLoader.FieldSpec spec = new FileSpecLoader.FieldSpec();
            setField(spec, "name", specInfo[0]);
            setField(spec, "start", Integer.parseInt(specInfo[1]));
            setField(spec, "end", Integer.parseInt(specInfo[2]));
            specs.add(spec);
        }
        String invalidQuantityLine = "AABBB";
        Assertions.assertThrows(InvalidTransactionRecordException.class, () -> new TransactionRecord(invalidQuantityLine, specs));
    }

    @Test
    void getClientInformationShouldReturnConcatenatedClientDetails() throws Exception {
        List<FileSpecLoader.FieldSpec> specs = new ArrayList<>();
        // Manually create and set FieldSpec instances
        for (String[] specInfo : new String[][]{
                {"clientType", "0", "2"},
                {"clientNumber", "2", "5"},
                {"accountNumber", "5", "8"},
                {"subAccountNumber", "8", "11"},
                {"filler", "11", "14"}
        }) {
            FileSpecLoader.FieldSpec spec = new FileSpecLoader.FieldSpec();
            setField(spec, "name", specInfo[0]);
            setField(spec, "start", Integer.parseInt(specInfo[1]));
            setField(spec, "end", Integer.parseInt(specInfo[2]));
            specs.add(spec);
        }
        String clientLine = "AB123456789   ";
        TransactionRecord record = new TransactionRecord(clientLine, specs);
        Assertions.assertEquals("AB_123_456_789", record.getClientInformation());
    }

    @Test
    void getNetTransactionAmountShouldReturnCorrectNetAmount() throws Exception {
        List<FileSpecLoader.FieldSpec> specs = new ArrayList<>();
        // Manually create and set FieldSpec instances
        for (String[] specInfo : new String[][]{
                {"quantityLong", "0", "3"},
                {"quantityShort", "3", "6"},
                {"filler", "6", "9"}
        }) {
            FileSpecLoader.FieldSpec spec = new FileSpecLoader.FieldSpec();
            setField(spec, "name", specInfo[0]);
            setField(spec, "start", Integer.parseInt(specInfo[1]));
            setField(spec, "end", Integer.parseInt(specInfo[2]));
            specs.add(spec);
        }
        String quantityLine = "010005   ";
        TransactionRecord record = new TransactionRecord(quantityLine, specs);
        Assertions.assertEquals(5, record.getNetTransactionAmount());
    }
}