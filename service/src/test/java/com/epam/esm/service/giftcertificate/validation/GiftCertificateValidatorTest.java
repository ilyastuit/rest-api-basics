package com.epam.esm.service.giftcertificate.validation;

import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.tag.validation.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GiftCertificateValidatorTest {

    @Mock
    private TagService tagService;

    @BeforeEach
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidationWithValidProperties() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);
        GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator(tagValidator);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("tag");
        List<TagDTO> tags = Collections.singletonList(tagDTO);

        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("name");
        certificateDTO.setDescription("description");
        certificateDTO.setPrice(new BigDecimal("100"));
        certificateDTO.setDuration(100);
        certificateDTO.setCreateDate(LocalDateTime.now());
        certificateDTO.setLastUpdateDate(LocalDateTime.now());
        certificateDTO.setTags(tags);

        Errors errors = new BeanPropertyBindingResult(certificateDTO, "certificateDTO");
        giftCertificateValidator.validate(certificateDTO, errors);

        assertFalse(errors.hasErrors());
        assertTrue(giftCertificateValidator.supports(certificateDTO.getClass()));
    }

    @Test
    public void testValidationWithNegativePrice() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);
        GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator(tagValidator);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("tag");
        List<TagDTO> tags = Collections.singletonList(tagDTO);

        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("name");
        certificateDTO.setDescription("description");
        certificateDTO.setPrice(new BigDecimal("-100"));
        certificateDTO.setDuration(100);
        certificateDTO.setCreateDate(LocalDateTime.now());
        certificateDTO.setLastUpdateDate(LocalDateTime.now());
        certificateDTO.setTags(tags);

        Errors errors = new BeanPropertyBindingResult(certificateDTO, "certificateDTO");
        giftCertificateValidator.validate(certificateDTO, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("price"));
    }

    @Test
    public void testValidationWithEmptyPrice() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);
        GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator(tagValidator);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("tag");
        List<TagDTO> tags = Collections.singletonList(tagDTO);

        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("name");
        certificateDTO.setDescription("description");
        certificateDTO.setPrice(null);
        certificateDTO.setDuration(100);
        certificateDTO.setCreateDate(LocalDateTime.now());
        certificateDTO.setLastUpdateDate(LocalDateTime.now());
        certificateDTO.setTags(tags);

        Errors errors = new BeanPropertyBindingResult(certificateDTO, "certificateDTO");
        giftCertificateValidator.validate(certificateDTO, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("price"));
    }

    @Test
    public void testValidationWithNegativeDuration() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);
        GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator(tagValidator);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("tag");
        List<TagDTO> tags = Collections.singletonList(tagDTO);

        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("name");
        certificateDTO.setDescription("description");
        certificateDTO.setPrice(new BigDecimal("100"));
        certificateDTO.setDuration(-100);
        certificateDTO.setCreateDate(LocalDateTime.now());
        certificateDTO.setLastUpdateDate(LocalDateTime.now());
        certificateDTO.setTags(tags);

        Errors errors = new BeanPropertyBindingResult(certificateDTO, "certificateDTO");
        giftCertificateValidator.validate(certificateDTO, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("duration"));
    }

    @Test
    public void testValidationWithEmptyDuration() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);
        GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator(tagValidator);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("tag");
        List<TagDTO> tags = Collections.singletonList(tagDTO);

        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("name");
        certificateDTO.setDescription("description");
        certificateDTO.setPrice(new BigDecimal("100"));
        certificateDTO.setDuration(null);
        certificateDTO.setCreateDate(LocalDateTime.now());
        certificateDTO.setLastUpdateDate(LocalDateTime.now());
        certificateDTO.setTags(tags);

        Errors errors = new BeanPropertyBindingResult(certificateDTO, "certificateDTO");
        giftCertificateValidator.validate(certificateDTO, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("duration"));
    }


    @Test
    public void testValidationWithEmptyNameTag() {
        assertNotNull(tagService);
        when(tagService.isExistByName("")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);
        GiftCertificateValidator validatorUnderTest = new GiftCertificateValidator(tagValidator);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("");
        List<TagDTO> tags = Collections.singletonList(tagDTO);

        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("name");
        certificateDTO.setDescription("description");
        certificateDTO.setPrice(new BigDecimal("100"));
        certificateDTO.setDuration(100);
        certificateDTO.setCreateDate(LocalDateTime.now());
        certificateDTO.setLastUpdateDate(LocalDateTime.now());
        certificateDTO.setTags(tags);

        Errors errors = new BeanPropertyBindingResult(certificateDTO, "certificateDTO");
        validatorUnderTest.validate(certificateDTO, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("tags[0].name"));
    }

    @Test
    public void testValidationWithEmptyName() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);
        GiftCertificateValidator validatorUnderTest = new GiftCertificateValidator(tagValidator);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("tag");
        List<TagDTO> tags = Collections.singletonList(tagDTO);

        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setName("");
        certificateDTO.setDescription("description");
        certificateDTO.setPrice(new BigDecimal("100"));
        certificateDTO.setDuration(100);
        certificateDTO.setCreateDate(LocalDateTime.now());
        certificateDTO.setLastUpdateDate(LocalDateTime.now());
        certificateDTO.setTags(tags);

        Errors errors = new BeanPropertyBindingResult(certificateDTO, "certificateDTO");
        validatorUnderTest.validate(certificateDTO, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("name"));
    }

}
