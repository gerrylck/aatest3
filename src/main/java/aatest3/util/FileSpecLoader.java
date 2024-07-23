package aatest3.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileSpecLoader {
    private List<FieldSpec> fields;

    public static class FieldSpec {
        public String name;
        public int start;
        public int end;
    }

    public void load(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, List<FieldSpec>> spec = objectMapper.readValue(new File(filePath), new TypeReference<>() {});
        fields = spec.get("fields");
    }

    public List<FieldSpec> getFields() {
        return fields;
    }
}
