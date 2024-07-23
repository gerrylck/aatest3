package aatest3.controller;

import aatest3.model.TransactionRecord;
import aatest3.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController();
        transactionController.setTransactionService(transactionService);
    }

    @Test
    void processTransactionsWithValidFileReturnsSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "content".getBytes());
        List<TransactionRecord> records = Arrays.asList(new TransactionRecord());
        Map<String, Map<String, Integer>> aggregatedData = new HashMap<>();
        byte[] csvOutput = "result".getBytes();

        when(transactionService.parseFile(file)).thenReturn(records);
        when(transactionService.aggregateData(records)).thenReturn(aggregatedData);
        when(transactionService.generateCsv(aggregatedData)).thenReturn(csvOutput);

        ResponseEntity<byte[]> response = transactionController.processTransactions(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Output.csv", response.getHeaders().getContentDisposition().getFilename());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals(csvOutput.length, response.getBody().length);
    }

}