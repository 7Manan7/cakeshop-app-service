package com.cakeshop.app.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Component
public class DynamicQueryUtil {

	private final EntityManager entityManager;

	public DynamicQueryUtil(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Map<String, Object>> fetchSelectiveData(DataFetchingEnvironment environment, String entityName) {
		List<String> requestedFields = environment.getSelectionSet().getFields().stream().map(field -> field.getName())
				.collect(Collectors.toList());

		if (requestedFields.isEmpty()) {
			return Collections.emptyList();
		}

		String queryStr = "SELECT "
				+ String.join(", ", requestedFields.stream().map(field -> "e." + field).collect(Collectors.toList()))
				+ " FROM " + entityName + " e";

		Query query = entityManager.createQuery(queryStr);

		List<?> queryResults = query.getResultList();
		return mapQueryResults(queryResults, requestedFields);
	}

	private List<Map<String, Object>> mapQueryResults(List<?> queryResults, List<String> fields) {
		List<Map<String, Object>> mappedResults = new ArrayList<>();

		for (Object result : queryResults) {
			if (result instanceof Object[]) {
				Map<String, Object> map = new HashMap<>();
				Object[] row = (Object[]) result;

				for (int i = 0; i < fields.size(); i++) {
					map.put(fields.get(i), row[i]);
				}
				mappedResults.add(map);
			}
		}

		return mappedResults;
	}
}
