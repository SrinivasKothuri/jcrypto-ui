package org.jcrypto.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class NamesProvider {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static Name[] fetchNamesForOperation(String opId) throws IOException {
		String[] keys = StringUtils.split(opId, '.');
		JsonNode jsonNode = MAPPER.readTree(source());
		ArrayNode nameNodes = (ArrayNode) jsonNode.get(keys[0]).get(keys[1]);
		Name[] names = new Name[nameNodes.size()];
		for (int i = 0; i < nameNodes.size(); ++i) {
			JsonNode nameNode = nameNodes.get(i);
			names[i] = new Name(nameNode.get("id").textValue(), nameNode.get("name").textValue(),
					nameNode.get("label").textValue(), Name.Type.valueOf(nameNode.get("type").textValue()));
		}
		return names;
	}

	private static InputStream source() throws FileNotFoundException {
		return new FileInputStream(new File(System.getProperty("user.dir"), "names.json"));
	}

	public static Map<String, String> parseInputs(String jsonPayload) throws JsonProcessingException {
		return MAPPER.readValue(jsonPayload, Map.class);
	}
}
