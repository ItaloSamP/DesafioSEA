package br.com.seatecnologia.todolist.service.impl;

import br.com.seatecnologia.todolist.model.Subtask;
import br.com.seatecnologia.todolist.service.persistence.SubtaskPersistence;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubtaskLocalServiceImplTest {

    private SubtaskLocalServiceImpl subtaskLocalService;
    private SubtaskPersistence      subtaskPersistence;
    private CounterLocalService     counterLocalService;

    @Before
    public void setUp() throws Exception {
        subtaskLocalService = new SubtaskLocalServiceImpl();
        subtaskPersistence  = mock(SubtaskPersistence.class);
        counterLocalService = mock(CounterLocalService.class);

        injectField(subtaskLocalService, "subtaskPersistence",  subtaskPersistence);
        injectField(subtaskLocalService, "counterLocalService", counterLocalService);
    }

    // ---------------------------------------------------------------------------
    // addSubtask_deveCriarComStatusPendente
    // ---------------------------------------------------------------------------

    @Test
    public void addSubtask_deveCriarComStatusPendente() {
        long taskId    = 10L;
        long subtaskId = 200L;

        Subtask mockSubtask = mock(Subtask.class);

        when(counterLocalService.increment(Subtask.class.getName())).thenReturn(subtaskId);
        when(subtaskPersistence.create(subtaskId)).thenReturn(mockSubtask);
        when(subtaskPersistence.update(mockSubtask)).thenReturn(mockSubtask);

        Subtask resultado = subtaskLocalService.addSubtask(taskId, "Nova Subtarefa");

        assertNotNull(resultado);
        verify(mockSubtask).setIsCompleted(false);
        verify(mockSubtask).setTaskId(taskId);
        verify(mockSubtask).setTitle("Nova Subtarefa");
        verify(mockSubtask).setCreateDate(any(Date.class));
        verify(mockSubtask).setModifiedDate(any(Date.class));
    }

    // ---------------------------------------------------------------------------
    // toggleSubtaskStatus_deveInverterStatus
    // ---------------------------------------------------------------------------

    @Test
    public void toggleSubtaskStatus_deveInverterStatus() throws PortalException {
        long subtaskId  = 5L;
        Subtask mockSubtask = mock(Subtask.class);

        // Começa como pendente (false) → deve virar concluída (true)
        when(mockSubtask.getIsCompleted()).thenReturn(false);
        when(subtaskPersistence.findByPrimaryKey(subtaskId)).thenReturn(mockSubtask);
        when(subtaskPersistence.update(mockSubtask)).thenReturn(mockSubtask);

        subtaskLocalService.toggleSubtaskStatus(subtaskId);

        verify(mockSubtask).setIsCompleted(true);
        verify(mockSubtask).setModifiedDate(any(Date.class));
    }

    // ---------------------------------------------------------------------------
    // Utilitário: injeta campo protegido via reflexão percorrendo a hierarquia
    // ---------------------------------------------------------------------------

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(
            "Campo '" + fieldName + "' não encontrado na hierarquia de " + target.getClass().getName()
        );
    }
}
