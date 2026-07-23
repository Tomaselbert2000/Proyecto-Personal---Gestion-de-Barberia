package com.utils.password_generator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

/**
 * Generador de contraseñas seguras que garantiza la inclusión de caracteres obligatorios
 * de cada tipo (mayúsculas, minúsculas, números y símbolos especiales).
 *
 * <p>Esta clase implementa una estrategia de generación que asegura:
 * <ul>
 *   <li>Al menos un carácter de cada categoría requerida</li>
 *   <li>Distribución aleatoria de los caracteres restantes</li>
 *   <li>Mezcla completa de todos los caracteres para evitar patrones predecibles</li>
 * </ul>
 *
 * <p>Utiliza {@link SecureRandom} para garantizar la aleatoriedad criptográfica,
 * esencial para proteger contra ataques de fuerza bruta y predicción de patrones.
 */
public final class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|";
    private static final String NUMBERS = "0123456789";
    private static final int MIN_MANDATORY_CHARACTERS = 4;

    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + SPECIAL_CHARS + NUMBERS;

    private PasswordGenerator() {
    }

    /**
     * Genera una contraseña segura con la longitud especificada.
     *
     * <p>La implementación garantiza que la contraseña contenga al menos un carácter
     * de cada tipo obligatorio (mayúscula, minúscula, número y símbolo especial),
     * independientemente de la longitud solicitada. Los caracteres restantes se
     * seleccionan aleatoriamente del conjunto completo de caracteres permitidos.
     *
     * <p>El proceso de generación sigue estos pasos:
     * <ol>
     *   <li>Selecciona un carácter obligatorio de cada categoría</li>
     *   <li>Completa la contraseña con caracteres aleatorios adicionales</li>
     *   <li>Mezcla todos los caracteres para eliminar patrones predecibles</li>
     * </ol>
     *
     * @param length longitud mínima de 4 caracteres. Valores inferiores lanzarán
     *               {@link IllegalArgumentException}.
     * @return una contraseña segura con la longitud especificada, garantizando
     * inclusión de todos los tipos de caracteres obligatorios.
     * @throws IllegalArgumentException si la longitud solicitada es menor a 4,
     *                                  ya que se requieren al menos un carácter
     *                                  de cada categoría obligatoria.
     */
    public static String generatePassword(int length) {

        if (length < MIN_MANDATORY_CHARACTERS) throw new IllegalArgumentException();

        SecureRandom secureRandomInstance = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        char uppercase = UPPERCASE.charAt(secureRandomInstance.nextInt(UPPERCASE.length()));
        char lowercase = LOWERCASE.charAt(secureRandomInstance.nextInt(LOWERCASE.length()));
        char special = SPECIAL_CHARS.charAt(secureRandomInstance.nextInt(SPECIAL_CHARS.length()));
        char number = NUMBERS.charAt(secureRandomInstance.nextInt(NUMBERS.length()));

        List<Character> mandatoryChars = List.of(uppercase, lowercase, special, number);

        List<Character> passwordCharacters = new ArrayList<>(mandatoryChars);

        for (int i = 0; i < length - mandatoryChars.size(); i++) {

            char randomCharacter = ALL_CHARS.charAt(secureRandomInstance.nextInt(ALL_CHARS.length()));

            passwordCharacters.add(randomCharacter);
        }

        shuffle(passwordCharacters);

        for (char character : passwordCharacters) {

            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }
}