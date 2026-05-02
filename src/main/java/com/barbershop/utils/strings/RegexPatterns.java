package com.barbershop.utils.strings;

public class RegexPatterns {

    private RegexPatterns() {
        throw new UnsupportedOperationException("No es posible instanciar clases de tipo Utilidad");
    }

    public static String returnLoremIpsum() {

        return "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem" +
                "placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus" +
                "fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada lacinia integer nunc posuere ut hendrerit" +
                "semper vel class aptent taciti sociosqu ad litora torquent per conubia nostra inceptos himenaeos orci varius natoque" +
                "penatibus et magnis dis parturient montes nascetur ridiculus mus donec rhoncus eros lobortis nulla molestie mattis" +
                "scelerisque maximus eget fermentum odio phasellus non purus est efficitur laoreet mauris pharetra vestibulum" +
                "fusce dictum risus blandit quis suspendisse aliquet nisi sodales consequat magna ante condimentum neque at luctus" +
                "nibh finibus facilisis dapibus etiam interdum tortor ligula congue sollicitudin erat viverra ac tincidunt nam porta" +
                "elementum a enim euismod quam justo lectus commodo augue arcu dignissim velit aliquam imperdiet mollis nullam volutpat" +
                "porttitor ullamcorper rutrum gravida.";
    }

    public static final String NAME_REGEX = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";
    public static final String NATIONAL_ID_CARD_NUMBER_REGEX = "^\\d+$";
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    public static final String PHONE_REGEX = "^\\+?[0-9+]+$";
    public static final String DECIMAL_REGEX = "^\\d*\\.?\\d*$";
}