package com.test_constant;

import com.enums.BarberServiceCategory;

import java.time.LocalDateTime;

public final class BarberServiceTestConstants {

    private BarberServiceTestConstants() {
    }

    public static final class CreationValidData {

        public static final String BARBER_SERVICE_NAME = "Corte de pelo americano";
        public static final Double BARBER_SERVICE_PRICE = 12000.0;
        public static final BarberServiceCategory BARBER_SERVICE_CATEGORY = BarberServiceCategory.CORTE_Y_BARBA;
        public static final String INTERNAL_NOTES = "El servicio debe tomar como máximo 45 minutos.";
    }

    public static final class UpdateValidData {

        public static final String NEW_BARBER_SERVICE_NAME = "Corte de pelo americano completo";
        public static final Double NEW_PRICE = 15000.0;
        public static final BarberServiceCategory NEW_CATEGORY = BarberServiceCategory.CORTE;
        public static final String NEW_INTERNAL_NOTES = "El servicio debe tomar como máximo 60 minutos";
    }

    public static final class InvalidData {

        public static final String NAME_TOO_SHORT = "Abc";
        public static final String NAME_TOO_LONG = "SESQzPBuQGZYdrdPQMTbiKunPkghfJwhdyYCnZYXcfSBZGCBfkNRUhevffzxFFuPJTEHCYaNCdHgZtTjBVKQGwTfenVpSqbpDtfeAKMwvvjCKBGbzJbaDRri";
        public static final String INVALID_NAME = "N0mbe3 Inv@lido!";
        public static final String INTERNAL_NOTES_TOO_LONG = "eyPmDG9jQQSwQBxp3706LwLyDQ6BgTinrBkgPhD6MJTU3uHBEHu7yZkQwPb4nJS50jSc7QwjgxpEGGWktr0gaRuA2K5FNqAMZ9pNDgdGW21PxammQHdBeRrr2HJGJn2P4uMiPW4DJSBDdUMTvwarkAEV79wvE0g1mM9vVGcrV26XcRiNPtArJBH9A2mFn0VH9znZqXE6j3CcYgPpZvRuJxudG6m5dtShcnStemHFkUp5bFa7ZaqhwYEhTMtk91CYC";
        public static final Double INVALID_PRICE = -12000.0;
    }

    public static final class MapperTestData {
        public static final Long BARBER_SERVICE_ID = 1L;
        public static final LocalDateTime BARBER_SERVICE_REGISTRATION_TIMESTAMP = LocalDateTime.of(2026, 1, 1, 12, 30);
        public static final LocalDateTime BARBER_SERVICE_MODIFICATION_TIMESTAMP = null;
        public static final String BARBER_SERVICE_NAME_WITH_SPACES = "        Corte de pelo americano      ";
        public static final String LOWERCASE_NAME = "corte de pelo americano";
        public static final String INTERNAL_NOTES_WITH_SPACES = "      El servicio debe tomar como máximo 45 minutos.      ";
        public static final String EMPTY_INTERNAL_NOTES = "";
    }
}
