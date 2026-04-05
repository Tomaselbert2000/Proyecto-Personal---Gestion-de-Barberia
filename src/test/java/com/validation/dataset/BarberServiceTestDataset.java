package com.validation.dataset;

import com.barbershop.enums.BarberServiceCategory;

public class BarberServiceTestDataset {

    public static final String SERVICE_NAME = "Corte de pelo americano";
    public static final Double PRICE = 12000.0;
    public static final BarberServiceCategory CATEGORY = BarberServiceCategory.CORTE_Y_BARBA;
    public static final String INTERNAL_NOTES = "El servicio debe tomar como máximo 45 minutos.";

    public static final String NAME_TOO_SHORT = "Abc";
    public static final String NAME_TOO_LONG = "SESQzPBuQGZYdrdPQMTbiKunPkghfJwhdyYCnZYXcfSBZGCBfkNRUhevffzxFFuPJTEHCYaNCdHgZtTjBVKQGwTfenVpSqbpDtfeAKMwvvjCKBGbzJbaDRri";
    public static final String INVALID_NAME = "N0mbe3 Inv@lido!";
    public static final String INTERNAL_NOTES_TOO_LONG = "eyPmDG9jQQSwQBxp3706LwLyDQ6BgTinrBkgPhD6MJTU3uHBEHu7yZkQwPb4nJS50jSc7QwjgxpEGGWktr0gaRuA2K5FNqAMZ9pNDgdGW21PxammQHdBeRrr2HJGJn2P4uMiPW4DJSBDdUMTvwarkAEV79wvE0g1mM9vVGcrV26XcRiNPtArJBH9A2mFn0VH9znZqXE6j3CcYgPpZvRuJxudG6m5dtShcnStemHFkUp5bFa7ZaqhwYEhTMtk91CYC";
    public static final Double INVALID_PRICE = -12000.0;
}
