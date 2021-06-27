package com.epam.esm.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.exceptions.TagNameAlreadyExistException;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.FileReader;
import java.util.Properties;

public class TagRepositoryTest {

    private static TagRepository tagRepository;
    private static Flyway flyway;

    public final static int ALL_TAGS_COUNT = 10;
    public final static int NOT_EXIST_TAG_ID = 0;
    public final static String EXIST_TAG_NAME = "android";
    public final static String NOT_EXIST_TAG_NAME = "new tag";
    public final static int CERTIFICATE_ID_WITH_TAG = 1;
    public final static int CERTIFICATE_ID_WITHOUT_TAG = 6;
    public final static int TAG_ID_WITH_CERTIFICATE = 1;
    public final static int TAG_ID_WITHOUT_CERTIFICATE = 10;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        Properties props = new Properties();
        FileReader file = new FileReader(TagRepositoryTest.class.getResource("/application-test.properties").getFile());
        props.load(file);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(props.getProperty("datasource.driver"));
        dataSource.setUrl(props.getProperty("datasource.url"));
        dataSource.setUsername(props.getProperty("datasource.username"));
        dataSource.setPassword(props.getProperty("datasource.password"));

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        PlatformTransactionManager transactionManager = new JdbcTransactionManager(dataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        tagRepository = new TagRepository(namedParameterJdbcTemplate, jdbcTemplate, transactionTemplate);

        Configuration configuration = new FluentConfiguration()
                .dataSource(dataSource)
                .configuration(props);
        flyway = new Flyway(configuration);
    }

    @BeforeEach
    public void flyWayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successFindOne() {
        assertEquals(1, tagRepository.findOne(1).size());
        assertEquals(1, tagRepository.findOne(1).get(0).getId());
    }

    @Test
    void emptyFindOne() {
        assertEquals(0, tagRepository.findOne(NOT_EXIST_TAG_ID).size());
    }

    @Test
    void successFindAll() {
        assertEquals(ALL_TAGS_COUNT, tagRepository.findAll().size());
    }

    @Test
    void successSave() throws TagNameAlreadyExistException {
        assertEquals(ALL_TAGS_COUNT + 1, tagRepository.save(new Tag(0, NOT_EXIST_TAG_NAME)));

        assertEquals(ALL_TAGS_COUNT + 1, tagRepository.findAll().size());
        assertEquals(ALL_TAGS_COUNT + 1, tagRepository.findOne(ALL_TAGS_COUNT + 1).get(0).getId());
        assertEquals(NOT_EXIST_TAG_NAME, tagRepository.findOne(ALL_TAGS_COUNT + 1).get(0).getName());
    }

    @Test
    void failSave() {
        assertThrows(TagNameAlreadyExistException.class, () -> {
            tagRepository.save(new Tag(0, EXIST_TAG_NAME));
        });
    }

    @Test
    void successFindByName() {
        assertEquals(EXIST_TAG_NAME, tagRepository.findByName(EXIST_TAG_NAME).get(0).getName());
    }

    @Test
    void emptyFindByName() {
        assertEquals(0, tagRepository.findByName(NOT_EXIST_TAG_NAME).size());
    }

    @Test
    void successFindAssignedTagToCertificate() {
        assertEquals(TAG_ID_WITH_CERTIFICATE, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITH_TAG, TAG_ID_WITH_CERTIFICATE).get(0).getId());
    }

    @Test
    void emptyFindAssignedTagToCertificate() {
        assertEquals(0, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITHOUT_TAG, TAG_ID_WITHOUT_CERTIFICATE).size());
    }

    @Test
    void successAssignTagToGiftCertificate() {
        tagRepository.assignTagToGiftCertificate(CERTIFICATE_ID_WITHOUT_TAG, TAG_ID_WITHOUT_CERTIFICATE);
        assertEquals(TAG_ID_WITHOUT_CERTIFICATE, tagRepository.findByGiftCertificateId(CERTIFICATE_ID_WITHOUT_TAG).get(0).getId());
    }

    @Test
    void successFindByGiftCertificateId() {
        assertEquals(CERTIFICATE_ID_WITH_TAG, tagRepository.findByGiftCertificateId(CERTIFICATE_ID_WITH_TAG).get(0).getId());
    }

    @Test
    void emptyFindByGiftCertificateId() {
        assertEquals(0, tagRepository.findByGiftCertificateId(CERTIFICATE_ID_WITHOUT_TAG).size());
    }

    @Test
    void successDeleteById() {
        assertEquals(1, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITH_TAG, TAG_ID_WITH_CERTIFICATE).size());
        assertEquals(1, tagRepository.findOne(TAG_ID_WITH_CERTIFICATE).size());
        tagRepository.deleteById(1);
        assertEquals(0, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITH_TAG, TAG_ID_WITH_CERTIFICATE).size());
        assertEquals(0, tagRepository.findOne(TAG_ID_WITH_CERTIFICATE).size());
    }
}
