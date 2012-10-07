package com.singly.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.TextNode;

public class JsonUtils {

  public static boolean looksLikeJson(String content) {
    return StringUtils.trim(content).startsWith("{");
  }

  public static JsonNode parseJson(String json) {

    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(new StringReader(json), JsonNode.class);
    }
    catch (Exception e) {
      return null;
    }
  }

  public static Map<String, Object> parseJsonToMap(String json) {

    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(new StringReader(json), HashMap.class);
    }
    catch (Exception e) {
      return null;
    }
  }

  public static String getStringValue(JsonNode parent, String name) {
    JsonNode node = parent.get(name);
    if (node != null) {
      String value = node.getTextValue();
      return StringUtils.equals(value, "null") ? null : value;
    }
    return null;
  }

  public static List<JsonNode> getListValue(JsonNode parent, String name) {
    List<JsonNode> nodes = new ArrayList<JsonNode>();
    JsonNode node = parent.get(name);
    if (node != null) {
      if (node.isArray()) {
        Iterator values = node.getElements();
        if (values != null) {
          while (values.hasNext()) {
            nodes.add((JsonNode)values.next());
          }
        }
      }
      else {
        nodes.add(node);
      }
    }
    return nodes;
  }

  public static List<String> getListStringValue(JsonNode parent, String name) {
    List<String> nodesVals = new ArrayList<String>();
    JsonNode node = parent.get(name);
    if (node != null) {
      if (node.isArray()) {
        Iterator values = node.getElements();
        if (values != null) {
          while (values.hasNext()) {
            JsonNode valueNode = (JsonNode)values.next();
            if (valueNode != null) {
              String value = valueNode.getTextValue();
              if (StringUtils.isNotBlank(value)) {
                nodesVals.add(value);
              }
            }
          }
        }
      }
      else {
        String value = node.getTextValue();
        if (StringUtils.isNotBlank(value)) {
          nodesVals.add(value);
        }
      }
    }
    return nodesVals;
  }

  public static boolean getBooleanValue(JsonNode parent, String name,
    boolean defaultValue) {
    JsonNode node = parent.get(name);
    if (node != null) {
      return node.getBooleanValue();
    }
    return defaultValue;
  }

  public static int getIntValue(JsonNode parent, String name, int defaultValue) {
    JsonNode node = parent.get(name);
    if (node != null) {
      return node.getIntValue();
    }
    return defaultValue;
  }

  public static long getLongValue(JsonNode parent, String name,
    long defaultValue) {
    JsonNode node = parent.get(name);
    if (node != null) {
      return node.getLongValue();
    }
    return defaultValue;
  }

  public static double getDoubleValue(JsonNode parent, String name,
    double defaultValue) {
    JsonNode node = parent.get(name);
    if (node != null) {
      return node.getDoubleValue();
    }
    return defaultValue;
  }

  public static String serializeToJson(Object object) {

    try {

      StringWriter sw = new StringWriter(); // serialize
      ObjectMapper mapper = new ObjectMapper();
      MappingJsonFactory jsonFactory = new MappingJsonFactory();
      JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
      mapper.writeValue(jsonGenerator, object);
      sw.close();

      return sw.toString();
    }
    catch (Exception e) {
      return null;
    }
  }

  public static Object deserializeFromJson(String json, Class valueType) {

    try {
      ObjectMapper mapper = new ObjectMapper();
      MappingJsonFactory jsonFactory = new MappingJsonFactory();
      JsonParser jsonParser = jsonFactory.createJsonParser(json);
      return mapper.readValue(jsonParser, valueType);
    }
    catch (Exception e) {
      return null;
    }
  }

  public static List<String> getFieldNames(JsonNode parent) {
    Iterator<String> fieldNameIt = parent.getFieldNames();
    List<String> fieldNames = new ArrayList<String>();
    while (fieldNameIt.hasNext()) {
      String fieldName = fieldNameIt.next();
      fieldNames.add(fieldName);
    }
    return fieldNames;
  }

  public static List<String> getStringValues(JsonNode node) {

    List<String> strVals = new ArrayList<String>();
    if (node instanceof TextNode) {
      String val = ((TextNode)node).getTextValue();
      if (StringUtils.isNotBlank(val)) {
        strVals.add(val);
      }
    }
    else if (node instanceof ArrayNode) {
      for (JsonNode curNode : (ArrayNode)node) {
        if (curNode instanceof TextNode) {
          String val = ((TextNode)curNode).getTextValue();
          if (StringUtils.isNotBlank(val)) {
            strVals.add(val);
          }
        }
      }
    }
    return strVals;
  }
}
