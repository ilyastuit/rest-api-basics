package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GiftCertificateDTOMapper {

    GiftCertificate giftCertificateDTOToGiftCertificate(GiftCertificateDTO giftCertificateDTO);
    GiftCertificateDTO giftCertificateToGiftCertificateDTO(GiftCertificate giftCertificate);

    List<GiftCertificateDTO> giftCertificateListToGiftCertificateDTOList(List<GiftCertificate> giftCertificateList);
    List<GiftCertificate> giftCertificateDTOListToGiftCertificateList(List<GiftCertificateDTO> dtoList);

    TagDTO tagToTagDTO(Tag tag);
    Tag tagDTOToTag(TagDTO dto);
}
