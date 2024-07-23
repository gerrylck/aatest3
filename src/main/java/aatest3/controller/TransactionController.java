package aatest3.controller;

import aatest3.model.TransactionRecord;
import aatest3.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/process")
    public ResponseEntity<byte[]> processTransactions(@RequestParam("file") MultipartFile file) throws Exception {
        List<TransactionRecord> records = transactionService.parseFile(file);
        Map<String, Map<String, Integer>> aggregatedData = transactionService.aggregateData(records);
        byte[] csvOutput = transactionService.generateCsv(aggregatedData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Output.csv");

        return new ResponseEntity<>(csvOutput, headers, HttpStatus.OK);
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
