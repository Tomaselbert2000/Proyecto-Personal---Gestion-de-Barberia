package com.service.helper;

import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.*;

/**
 * This class provides utility methods for testing services that interact with a JPA repository.
 */
public class ServiceTestVerificationHelper {

    public static <T, R extends JpaRepository<T, Long>> void verifyEntitySaved(R repository) {
        // Verifies that the entity was saved to the repository
        verify(repository).save(any());
    }

    public static <T, R extends JpaRepository<T, Long>> void verifyEntityNotSaved(R repository) {
        // Verifies that the entity was not saved to the repository
        verify(repository, never()).save(any());
    }

    public static <T, R extends JpaRepository<T, Long>> void verifyEntityDeletion(R repository, T entity) {
        // Verifies that the entity was deleted from the repository
        verify(repository, times(1)).delete(entity);
    }

    public static <T, R extends JpaRepository<T, Long>> void verifyEntityNotDeleted(R repository, T entity) {
        // Verifies that the entity was not deleted from the repository
        verify(repository, never()).delete(entity);
    }

    /**
     * Adds tabs at the beginning and end of a given string.
     *
     * @param input The string to be modified
     * @return The modified string with tabs added
     */
    public static String addTabs(String input) {
        return "\t" + input + "\t";
    }

    /**
     * Returns the input string in lowercase.
     *
     * @param input The string to convert to lowercase
     * @return The lowercase version of the input string
     */
    public static String returnAsLowercase(String input) {
        return input.toLowerCase();
    }
}
