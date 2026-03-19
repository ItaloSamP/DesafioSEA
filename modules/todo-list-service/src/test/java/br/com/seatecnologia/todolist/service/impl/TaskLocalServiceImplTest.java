package br.com.seatecnologia.todolist.service.impl;

import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.service.persistence.TaskPersistence;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskLocalServiceImplTest {

    private TaskLocalServiceImpl taskLocalService;
    private TaskPersistence taskPersistence;
    private CounterLocalService counterLocalService;

    @Before
    public void setUp() throws Exception {
        taskLocalService    = new TaskLocalServiceImpl();
        taskPersistence     = mock(TaskPersistence.class);
        counterLocalService = mock(CounterLocalService.class);

        injectField(taskLocalService, "taskPersistence",     taskPersistence);
        injectField(taskLocalService, "counterLocalService", counterLocalService);
    }

    // ---------------------------------------------------------------------------
    // addTask_deveCriarTarefaComStatusPendente
    // ---------------------------------------------------------------------------

    @Test
    public void addTask_deveCriarTarefaComStatusPendente() throws Exception {
        long userId  = 1L;
        long groupId = 10L;
        long taskId  = 100L;

        Task mockTask = mock(Task.class);
        User mockUser = mock(User.class);

        when(counterLocalService.increment(Task.class.getName())).thenReturn(taskId);
        when(taskPersistence.create(taskId)).thenReturn(mockTask);
        when(mockUser.getCompanyId()).thenReturn(1L);
        when(mockUser.getFullName()).thenReturn("Usuário Teste");
        when(taskPersistence.update(mockTask)).thenReturn(mockTask);

        try (MockedStatic<UserLocalServiceUtil> mocked = mockStatic(UserLocalServiceUtil.class)) {
            mocked.when(() -> UserLocalServiceUtil.getUser(userId)).thenReturn(mockUser);

            Task resultado = taskLocalService.addTask(
                userId, groupId, "Tarefa Teste", "Descrição", new Date(), 0L
            );

            assertNotNull(resultado);
            verify(mockTask).setIsCompleted(false);
            verify(mockTask).setIsDeleted(false);
        }
    }

    // ---------------------------------------------------------------------------
    // addTask_deveLancarExcecaoSeUsuarioNaoExiste
    // ---------------------------------------------------------------------------

    @Test(expected = PortalException.class)
    public void addTask_deveLancarExcecaoSeUsuarioNaoExiste() throws Exception {
        long userId  = 99L;
        long groupId = 10L;
        long taskId  = 101L;

        Task mockTask = mock(Task.class);

        when(counterLocalService.increment(Task.class.getName())).thenReturn(taskId);
        when(taskPersistence.create(taskId)).thenReturn(mockTask);

        try (MockedStatic<UserLocalServiceUtil> mocked = mockStatic(UserLocalServiceUtil.class)) {
            mocked.when(() -> UserLocalServiceUtil.getUser(userId))
                  .thenThrow(new PortalException("Usuário não encontrado"));

            taskLocalService.addTask(userId, groupId, "Tarefa", "Desc", new Date(), 0L);
        }
    }

    // ---------------------------------------------------------------------------
    // toggleTaskStatus_deveInverterStatusConcluida
    // ---------------------------------------------------------------------------

    @Test
    public void toggleTaskStatus_deveInverterStatusConcluida() throws Exception {
        long taskId   = 1L;
        Task mockTask = mock(Task.class);

        when(mockTask.getIsCompleted()).thenReturn(true);
        when(taskPersistence.findByPrimaryKey(taskId)).thenReturn(mockTask);
        when(taskPersistence.update(mockTask)).thenReturn(mockTask);

        taskLocalService.toggleTaskStatus(taskId);

        // Tarefa estava concluída (true) → deve virar pendente (false)
        verify(mockTask).setIsCompleted(false);
    }

    // ---------------------------------------------------------------------------
    // toggleTaskStatus_deveInverterStatusPendente
    // ---------------------------------------------------------------------------

    @Test
    public void toggleTaskStatus_deveInverterStatusPendente() throws Exception {
        long taskId   = 2L;
        Task mockTask = mock(Task.class);

        when(mockTask.getIsCompleted()).thenReturn(false);
        when(taskPersistence.findByPrimaryKey(taskId)).thenReturn(mockTask);
        when(taskPersistence.update(mockTask)).thenReturn(mockTask);

        taskLocalService.toggleTaskStatus(taskId);

        // Tarefa estava pendente (false) → deve virar concluída (true)
        verify(mockTask).setIsCompleted(true);
    }

    // ---------------------------------------------------------------------------
    // softDeleteTask_deveMarcaIsDeletedTrue
    // ---------------------------------------------------------------------------

    @Test
    public void softDeleteTask_deveMarcaIsDeletedTrue() throws Exception {
        long taskId   = 3L;
        Task mockTask = mock(Task.class);

        when(taskPersistence.findByPrimaryKey(taskId)).thenReturn(mockTask);
        when(taskPersistence.update(mockTask)).thenReturn(mockTask);

        taskLocalService.softDeleteTask(taskId);

        verify(mockTask).setIsDeleted(true);
        verify(mockTask).setModifiedDate(any(Date.class));
    }

    // ---------------------------------------------------------------------------
    // updateTask_deveAtualizarCamposCorretamente
    // ---------------------------------------------------------------------------

    @Test
    public void updateTask_deveAtualizarCamposCorretamente() throws Exception {
        long taskId   = 4L;
        Task mockTask = mock(Task.class);

        when(mockTask.getCategoryId()).thenReturn(0L);
        when(taskPersistence.findByPrimaryKey(taskId)).thenReturn(mockTask);
        when(taskPersistence.update(mockTask)).thenReturn(mockTask);

        String novoTitulo = "Título Atualizado";
        String novaDesc   = "Nova descrição";
        Date   novaDueDate = new Date();

        taskLocalService.updateTask(taskId, novoTitulo, novaDesc, novaDueDate, 0L);

        verify(mockTask).setTitle(novoTitulo);
        verify(mockTask).setDescription(novaDesc);
        verify(mockTask).setDueDate(novaDueDate);
        verify(mockTask).setModifiedDate(any(Date.class));
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
