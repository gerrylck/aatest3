package aatest3.service;

import aatest3.exception.InvalidTransactionRecordException;
import aatest3.model.TransactionRecord;
import aatest3.util.FileSpecLoader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    public List<TransactionRecord> parseFile(MultipartFile file) throws Exception {
        URI resourceUri = getClass().getClassLoader().getResource("file-spec.json").toURI();
        Path fileSpecPath = Paths.get(resourceUri);

        FileSpecLoader specLoader = new FileSpecLoader();
        specLoader.load(fileSpecPath.toString());
        List<FileSpecLoader.FieldSpec> specs = specLoader.getFields();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines()
                    .map(line -> {
                        try {
                            return new TransactionRecord(line, specs);
                        } catch (InvalidTransactionRecordException e) {
                            System.out.println("Error processing line: '" + line + "' Error message: '" + e.getMessage());
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    public Map<String, Map<String, Integer>> aggregateData(List<TransactionRecord> records) {
        Map<String, Map<String, Integer>> aggregation = new HashMap<>();
        for (TransactionRecord record : records) {
            String clientInfo = record.getClientInformation();
            String productInfo = record.getProductInformation();
            int netAmount = record.getNetTransactionAmount();

            aggregation.putIfAbsent(clientInfo, new HashMap<>());
            aggregation.get(clientInfo).merge(productInfo, netAmount, Integer::sum);
        }
        return aggregation;
    }

    public byte[] generateCsv(Map<String, Map<String, Integer>> aggregation) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream))) {
            writer.writeNext(new String[]{"Client_Information", "Product_Information", "Total_Transaction_Amount"});
            for (Map.Entry<String, Map<String, Integer>> clientEntry : aggregation.entrySet()) {
                String clientInfo = clientEntry.getKey();
                for (Map.Entry<String, Integer> productEntry : clientEntry.getValue().entrySet()) {
                    String productInfo = productEntry.getKey();
                    String totalTransactionAmount = productEntry.getValue().toString();
                    writer.writeNext(new String[]{clientInfo, productInfo, totalTransactionAmount});
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
