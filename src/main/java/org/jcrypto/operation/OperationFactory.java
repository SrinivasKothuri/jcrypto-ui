package org.jcrypto.operation;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValue;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jcrypto.annotation.Op;
import org.jcrypto.model.Name;
import org.jcrypto.model.NamesProvider;
import org.jcrypto.model.Value;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class OperationFactory {

	private static final Map<String, Operation> CACHED_OPS = new ConcurrentHashMap<>();
	private static final Map<String, Name[]> CACHED_OP_TYPES = new ConcurrentHashMap<>();
	private static final AtomicBoolean initialized = new AtomicBoolean(false);

	public static final void init() {
		try {
			String opAnnotation = Op.class.getName();
			try (ScanResult scanResult = new ClassGraph().verbose().enableClassInfo().enableAnnotationInfo()
								 .acceptPackages(OperationFactory.class.getPackage().getName()).scan()) {
				for (ClassInfo opAnnotatedClass : scanResult.getClassesWithAnnotation(opAnnotation)) {
					AnnotationInfo opAnnotationInfo = opAnnotatedClass.getAnnotationInfo(opAnnotation);
					List<AnnotationParameterValue> opParamValues = opAnnotationInfo.getParameterValues();
					String opId = (String) opParamValues.get(0).getValue();
					if (StringUtils.isBlank(opId))
						continue;
					Class<?> opClass = opAnnotatedClass.loadClass();
					CACHED_OPS.put(opId, (Operation) opClass.newInstance());
					CACHED_OP_TYPES.put(opId, NamesProvider.fetchNamesForOperation(opId));
				}
			}
			initialized.set(true);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}

	private OperationFactory() {
		// no-op
	}

	private static Optional<Operation> getOp(String opId) {
		if (CACHED_OPS.containsKey(opId))
			return Optional.of(CACHED_OPS.get(opId));
		return Optional.empty();
	}

	public static Operation prepareOperation(String id, Map<String, String> uiParams)
			throws InvocationTargetException, IllegalAccessException {
		Optional<Operation> opOp = getOp(id);
		if (!opOp.isPresent())
			throw new IllegalArgumentException("No operation found with ID: " + id);

		Map<String, Value> paramValMap = new HashMap<>();
		uiParams.entrySet().forEach(entry -> paramValMap.put(entry.getKey(), new Value(entry.getValue())));
		Operation operation = opOp.get();
		BeanUtils.populate(operation, buildTypeMap(id, paramValMap));
		return operation;
	}

	private static Map<String, Object> buildTypeMap(String opId, Map<String, Value> paramValMap) {
		Name[] names = CACHED_OP_TYPES.get(opId);
		Map<String, Object> params = new HashMap<>();
		for (Name name: names) {
			if (!paramValMap.containsKey(name.getUiId()))
				continue;
			Value value = paramValMap.get(name.getUiId());
			Object typedVal;
			switch (name.getType()) {
				case INTEGER:
					typedVal = value.asNumber().intValue(); break;
				case DATETIME:
					typedVal = value.asDate(); break;
				case STRING:
				default:
					typedVal = value.asString(); break;
			}
			params.put(name.getName(), typedVal);
		}
		return params;
	}
}
