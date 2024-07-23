package aatest3.service;

import aatest3.model.TransactionRecord;
import aatest3.util.FileSpecLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private FileSpecLoader fileSpecLoader;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void parseFileWithInvalidContentThrowsException() {
        MultipartFile file = new MockMultipartFile("file", "invalid.csv", "text/csv", "invalid content".getBytes(StandardCharsets.UTF_8));
        when(fileSpecLoader.getFields()).thenReturn(Collections.emptyList()); // Simplified for example

        assertThrows(RuntimeException.class, () -> transactionService.parseFile(file));
    }

    @Test
    void aggregateDataWithEmptyRecordsReturnsEmptyMap() {
        List<TransactionRecord> records = Collections.emptyList();

        Map<String, Map<String, Integer>> aggregatedData = transactionService.aggregateData(records);

        assertTrue(aggregatedData.isEmpty());
    }

    @Test
    void generateCsvWithAggregatedDataReturnsNonEmptyByteArray() throws Exception {
        Map<String, Map<String, Integer>> aggregatedData = Map.of(
                "ABC", Map.of("Product1", 10, "Product2", 20)
        );

        byte[] csvOutput = transactionService.generateCsv(aggregatedData);

        assertNotNull(csvOutput);
        assertTrue(csvOutput.length > 0);
    }

    @Test
    void generateCsvWithEmptyAggregatedDataReturnsHeaderOnlyByteArray() throws Exception {
        Map<String, Map<String, Integer>> aggregatedData = Collections.emptyMap();

        byte[] csvOutput = transactionService.generateCsv(aggregatedData);

        assertNotNull(csvOutput);
        String csvContent = new String(csvOutput, StandardCharsets.UTF_8);
        assertTrue(csvContent.contains("\"Client_Information\",\"Product_Information\",\"Total_Transaction_Amount\""));
    }
}