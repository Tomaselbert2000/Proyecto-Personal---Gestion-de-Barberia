package com.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuración de seguridad para el cifrado de contraseñas en la aplicación.
 *
 * <p>Esta clase define los beans necesarios para habilitar el cifrado seguro
 * de contraseñas utilizando {@link BCryptPasswordEncoder}. El componente se
 * integra automáticamente con Spring Security para proteger credenciales
 * durante el proceso de autenticación y almacenamiento en bases de datos.
 *
 * <p>El uso de BCrypt garantiza:
 * <ul>
 *   <li>Cifrado asimétrico resistente a ataques de fuerza bruta</li>
 *   <li>Salting automático para evitar ataques de diccionario</li>
 *   <li>Coste computacional ajustable que dificulta ataques paralelos</li>
 * </ul>
 *
 * <p>El bean se registra automáticamente en el contexto Spring y está disponible
 * para inyección en servicios de autenticación, repositorios de usuarios y
 * controladores de registro/login.
 */
@Configuration
public class SecurityConfig {

    /**
     * Proporciona una instancia de BCryptPasswordEncoder para cifrado de contraseñas.
     *
     * <p>El metodo crea y registra un bean de {@link BCryptPasswordEncoder} que
     * será utilizado por Spring Security para:
     * <ul>
     *   <li>Cifrar contraseñas antes de almacenarlas en la base de datos</li>
     *   <li>Verificar contraseñas durante el proceso de autenticación</li>
     *   <li>Proteger credenciales contra ataques de fuerza bruta y rainbow tables</li>
     * </ul>
     *
     * <p>La instancia se configura con parámetros predeterminados que ofrecen
     * un equilibrio entre seguridad y rendimiento. El algoritmo utiliza
     * hashing con sal automático, lo que garantiza que cada contraseña tenga
     * una representación única incluso si los usuarios comparten la misma credencial.
     *
     * <p>El bean es inyectado automáticamente en componentes que requieren
     * cifrado de contraseñas mediante constructor injection o @Autowired.
     *
     * @return una instancia configurada de BCryptPasswordEncoder lista para
     * ser utilizada en operaciones de autenticación y almacenamiento
     * de credenciales.
     */
    @Bean
    public BCryptPasswordEncoder provideBCryptPasswordEncoderInstance() {

        return new BCryptPasswordEncoder();
    }
}