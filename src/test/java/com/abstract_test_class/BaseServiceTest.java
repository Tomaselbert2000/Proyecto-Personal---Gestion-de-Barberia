package com.abstract_test_class;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest<E, R extends JpaRepository<E, Long>> {

    @BeforeEach
    protected void init() {

    }

    protected abstract R getPrimaryRepository();

    protected void verifyThatEntityWasSaved() {

        verify(getPrimaryRepository()).save(any());
    }

    protected void verifyThatEntityWasNotSaved() {

        verify(getPrimaryRepository(), never()).save(any());
    }

    protected void verifyThatEntityWasDeleted(E entity) {

        verify(getPrimaryRepository(), times(1)).delete(entity);
    }

    protected void verifyThatEntityWasNotDeleted(E entity) {

        verify(getPrimaryRepository(), never()).delete(entity);
    }

    protected <T extends E> void verifyThatEntityWasCaptured(R repository, ArgumentCaptor<T> captor) {

        verify(repository).save(captor.capture());
    }
}
