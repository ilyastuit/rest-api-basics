package com.epam.esm.service.exceptions;

public class GiftCertificateSearchParameterNotProvidedException extends Exception {

    public GiftCertificateSearchParameterNotProvidedException() {
        super("Please provide the search parameter <q> or <tag>");
    }

}
