package Utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.Map;

public class JsonUtils {
    private JsonNode getValue (String json, String key, boolean debug) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
        JsonNode value = null;
        while (fieldsIterator.hasNext()) {
            Map.Entry<String,JsonNode> field = fieldsIterator.next();
            if (debug)
                System.out.println("Key: " + field.getKey() + " Value: " + field.getValue());
            if (field.getKey().equalsIgnoreCase(key)) {
                //System.out.println(field.getValue().textValue());
                if (field.getValue().isArray())
                    value = field.getValue().get(0);
                else {
                    value = field.getValue();
                }
            }
        }
        return value;
    }

    public String getStringValue(String json, String key, boolean debug) {
        JsonNode response = getValue(json, key, debug);
        if (response != null)
            return response.toString();
        else
            return null;
    }

    public String getTextValue(String json, String key, boolean debug) {
        JsonNode response = getValue(json, key, debug);
        if (response != null)
            return response.textValue();
        else
            return null;
    }
}
