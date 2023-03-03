package com.chatting;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class DatabaseInitializer {

	private static final String TRUNCATE_QUERY = "TRUNCATE TABLE %s";
	private static final String ALTER_AUTO_INCREMENT_QUERY = "ALTER TABLE %s AUTO_INCREMENT = 1";

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DataSource dataSource;

	private final List<String> tableNames = new ArrayList<>();

	@PostConstruct
	public void init() {
		DatabaseMetaData metaData = null;
		try {
			metaData = dataSource.getConnection().getMetaData();
		} catch (SQLException e) {
			throw new RuntimeException("커넥션을 획득하는데 실패했습니다.");
		}
		try (ResultSet tables = metaData.getTables(null, null, null, new String[] {"TABLE"})) {
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				tableNames.add(tableName);
			}
		} catch (Exception e) {
			throw new RuntimeException("테이블 이름을 불러오는데 실패했습니다.");
		}
	}

	@Transactional
	public void truncateTables() {
		tableNames
			.forEach(tableName -> {
				em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
				em.createNativeQuery(String.format(TRUNCATE_QUERY, tableName)).executeUpdate();
				em.createNativeQuery(String.format(ALTER_AUTO_INCREMENT_QUERY, tableName)).executeUpdate();
				em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
			});
	}
}
