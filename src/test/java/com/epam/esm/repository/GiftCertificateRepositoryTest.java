package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import com.epam.esm.service.tag.TagService;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.epam.esm.repository.TagBuilder.*;
import static com.epam.esm.repository.GiftCertificateBuilder.*;

public class GiftCertificateRepositoryTest {

    private static GiftCertificateRepository giftCertificateRepository;
    private static Flyway flyway;

    private final GiftCertificateBuilder builder = new GiftCertificateBuilder();
    private final TagBuilder tagBuilder = new TagBuilder();

    public final static int CERTIFICATE_ID_WITH_TAG = 1;
    public final static int CERTIFICATE_ID_WITHOUT_TAG = 6;
    public final static int TAG_ID_WITH_CERTIFICATE = 1;
    public final static int TAG_ID_WITHOUT_CERTIFICATE = 10;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

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

        TagRepository tagRepository = new TagRepository(namedParameterJdbcTemplate, jdbcTemplate, transactionTemplate);
        TagService tagService = new TagService(tagRepository);
        giftCertificateRepository = new GiftCertificateRepository(jdbcTemplate, namedParameterJdbcTemplate, transactionTemplate, tagService);

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
    void successSave() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());

        assertEquals(ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.save(builder.getPreparedParams()));

        assertEquals(ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.findById(ALL_CERTIFICATES_COUNT + 1).get(0).getId());
    }

    @Test
    void successSaveWithNotExistTag() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());

        List<Tag> notExistTags = new ArrayList<>();
        notExistTags.add(tagBuilder.withName(NOT_EXIST_TAG_NAME).build());
        assertEquals(ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.save(builder.withTags(notExistTags).getPreparedParams()));

        assertEquals(ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.findById(ALL_CERTIFICATES_COUNT + 1).get(0).getId());
    }

    @Test
    void successUpdate() {
        assertEquals(DEFAULT_CERTIFICATE_ID, giftCertificateRepository.update(DEFAULT_CERTIFICATE_ID, builder.getPreparedParams()));

        GiftCertificate originalCertificate = builder.withId(DEFAULT_CERTIFICATE_ID).build();
        assertEquals(1, giftCertificateRepository.findById(DEFAULT_CERTIFICATE_ID).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findById(DEFAULT_CERTIFICATE_ID).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
    }

    @Test
    void successFindById() {
        GiftCertificate originalCertificate = builder.withId(EXIST_CERTIFICATE_ID).build();
        int originalCertificateId = originalCertificate.getId();

        assertEquals(1, giftCertificateRepository.findById(originalCertificateId).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findById(originalCertificateId).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
    }

    @Test
    void successFindByIdWithTags() {
        GiftCertificate originalCertificate = builder.withId(EXIST_CERTIFICATE_ID).build();
        int originalCertificateId = originalCertificate.getId();

        assertEquals(1, giftCertificateRepository.findByIdWithTags(originalCertificateId).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findByIdWithTags(originalCertificateId).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindById() {
        assertEquals(0, giftCertificateRepository.findById(NOT_EXIST_CERTIFICATE_ID).size());
    }

    @Test
    void successFindAll() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());
    }

    @Test
    void successFindAllWithTags() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTags().size());
    }

    @Test
    void successFindAllWithTagsByTagName() {
        Tag originalTag = tagBuilder.withId(EXIST_TAG_ID).withName(EXIST_TAG_NAME).build();
        assertEquals(1, giftCertificateRepository.findAllWithTagsByTagName(originalTag.getName()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllWithTagsByTagName(originalTag.getName()).get(0);

        Tag fetchedTag = fetchedCertificate.getTags()
                .stream()
                .filter((tag) -> tag.getName().equals(originalTag.getName()))
                .findFirst().get();

        assertEquals(originalTag.getId(), fetchedTag.getId());
    }

    @Test
    void emptyFindAllWithTagsByTagName() {
        Tag originalTag = tagBuilder.withName(NOT_EXIST_TAG_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllWithTagsByTagName(originalTag.getName()).size());
    }

    @Test
    void successFindAllByName() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllByName() {
        GiftCertificate originalCertificate = builder.withName(NOT_EXIST_CERTIFICATE_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName()).size());
    }

    @Test
    void successFindAllByDescription() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllByDescription() {
        GiftCertificate originalCertificate = builder.withDescription(NOT_EXIST_CERTIFICATE_DESCRIPTION).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription()).size());
    }

    @Test
    void successFindAllWithTagsByName() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getName()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getName()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllAllWithTagsByName() {
        GiftCertificate originalCertificate = builder.withName(NOT_EXIST_CERTIFICATE_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getName()).size());
    }

    @Test
    void successFindAllAllWithTagsByDescription() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getDescription()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getDescription()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllAllWithTagsByDescription() {
        GiftCertificate originalCertificate = builder.withDescription(NOT_EXIST_CERTIFICATE_DESCRIPTION).build();
        assertEquals(0, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getDescription()).size());
    }

    @Test
    void successDeleteById() {
        GiftCertificate originalCertificate = builder.build();
        giftCertificateRepository.deleteById(originalCertificate.getId());
        assertEquals(ALL_CERTIFICATES_COUNT - 1, giftCertificateRepository.findAll().size());
        assertEquals(0, giftCertificateRepository.findById(originalCertificate.getId()).size());
    }

    @Test
    void failDeleteById() {
        GiftCertificate originalCertificate = builder.withId(NOT_EXIST_CERTIFICATE_ID).build();
        giftCertificateRepository.deleteById(originalCertificate.getId());
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());
        assertEquals(0, giftCertificateRepository.findById(originalCertificate.getId()).size());
    }

    @Test
    void successFindAllWithTagsOrderByDateASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDate(ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDate(ORDER_BY_ASC);

        assertEquals(ORDER_BY_DATE_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByDateDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDate(ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDate(ORDER_BY_DESC);

        assertEquals(ORDER_BY_DATE_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDate(ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDate(ORDER_BY_ASC);

        assertEquals(ORDER_BY_DATE_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDate(ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDate(ORDER_BY_DESC);

        assertEquals(ORDER_BY_DATE_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByNameASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByName(ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByName(ORDER_BY_ASC);

        assertEquals(ORDER_BY_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_NAME_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByNameDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByName(ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByName(ORDER_BY_DESC);

        assertEquals(ORDER_BY_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_NAME_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByNameASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByName(ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByName(ORDER_BY_ASC);

        assertEquals(ORDER_BY_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_NAME_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByNameDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByName(ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByName(ORDER_BY_DESC);

        assertEquals(ORDER_BY_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_NAME_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByDateASCAndByNameASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDateAndByName(ORDER_BY_ASC, ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDateAndByName(ORDER_BY_ASC, ORDER_BY_ASC);

        assertEquals(ORDER_BY_DATE_AND_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_AND_NAME_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByDateDESCAndByNameDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDateAndByName(ORDER_BY_DESC, ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDateAndByName(ORDER_BY_DESC, ORDER_BY_DESC);

        assertEquals(ORDER_BY_DATE_AND_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_AND_NAME_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateASCAndByNameASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDateAndByName(ORDER_BY_ASC, ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDateAndByName(ORDER_BY_ASC, ORDER_BY_ASC);

        assertEquals(ORDER_BY_DATE_AND_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_AND_NAME_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateDESCAndByNameDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDateAndByName(ORDER_BY_DESC, ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDateAndByName(ORDER_BY_DESC, ORDER_BY_DESC);

        assertEquals(ORDER_BY_DATE_AND_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_AND_NAME_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(SEARCH_TEXT, ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(SEARCH_TEXT, ORDER_BY_ASC);

        assertEquals(ORDER_BY_DATE_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(SEARCH_TEXT, ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(SEARCH_TEXT, ORDER_BY_DESC);

        assertEquals(ORDER_BY_DATE_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateASCAndByNameASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(SEARCH_TEXT, ORDER_BY_ASC, ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(SEARCH_TEXT, ORDER_BY_ASC, ORDER_BY_ASC);

        assertEquals(ORDER_BY_DATE_AND_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_AND_NAME_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateDESCAndByNameDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(SEARCH_TEXT, ORDER_BY_DESC, ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(SEARCH_TEXT, ORDER_BY_DESC, ORDER_BY_DESC);

        assertEquals(ORDER_BY_DATE_AND_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_DATE_AND_NAME_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByNameASC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByName(SEARCH_TEXT, ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByName(SEARCH_TEXT, ORDER_BY_ASC);

        assertEquals(ORDER_BY_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_NAME_ASC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByNameDESC() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByName(SEARCH_TEXT, ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByName(SEARCH_TEXT, ORDER_BY_DESC);

        assertEquals(ORDER_BY_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(ORDER_BY_NAME_DESC_LAST_ID, originalCertificate.get(ALL_CERTIFICATES_COUNT - 1).getId());
    }

    private void assertCertificates(GiftCertificate originalCertificate, GiftCertificate fetchedCertificate) {
        assertEquals(originalCertificate.getId(), fetchedCertificate.getId());
        assertEquals(originalCertificate.getName(), fetchedCertificate.getName());
        assertEquals(originalCertificate.getDescription(), fetchedCertificate.getDescription());
        assertEquals(originalCertificate.getPrice(), fetchedCertificate.getPrice());
        assertEquals(originalCertificate.getDuration(), fetchedCertificate.getDuration());
        assertEquals(originalCertificate.getCreateDate(), fetchedCertificate.getCreateDate());
        //assertEquals(originalCertificate.getLastUpdateDate(), fetchedCertificate.getLastUpdateDate());
    }

    private void assertTags(GiftCertificate originalCertificate, GiftCertificate fetchedCertificate) {
        List<Tag> originalTags = originalCertificate.getTags();
        List<Tag> fetchedTags = fetchedCertificate.getTags();
        assertEquals(originalTags.size(), fetchedTags.size());

        for (int i = 0; i < originalTags.size(); i++) {
            Tag originalTag = originalTags.get(i);
            Tag fetchedTag = fetchedTags.get(i);
            assertEquals(originalTag.getId(), fetchedTag.getId());
            assertEquals(originalTag.getName(), fetchedTag.getName());
        }
    }
}
