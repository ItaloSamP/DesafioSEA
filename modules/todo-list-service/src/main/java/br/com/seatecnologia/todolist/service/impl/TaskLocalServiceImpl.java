package br.com.seatecnologia.todolist.service.impl;

import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.service.base.TaskLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import java.util.Date;
import java.util.List;

/**
 * Esta classe centraliza as regras de negócio das Tarefas.
 */
public class TaskLocalServiceImpl extends TaskLocalServiceBaseImpl {

    /**
     * Cria uma nova tarefa sem categoria (compatibilidade retroativa).
     */
    public Task addTask(long userId, long groupId, String title, String description, Date dueDate, long imageId) throws PortalException {
        return addTask(userId, groupId, title, description, dueDate, imageId, 0);
    }

    /**
     * Cria uma nova tarefa vinculada a uma categoria.
     */
    public Task addTask(long userId, long groupId, String title, String description, Date dueDate, long imageId, long categoryId) throws PortalException {

        long taskId = counterLocalService.increment(Task.class.getName());
        Task task = taskPersistence.create(taskId);

        User user = UserLocalServiceUtil.getUser(userId);

        task.setGroupId(groupId);
        task.setCompanyId(user.getCompanyId());
        task.setUserId(userId);
        task.setUserName(user.getFullName());

        Date now = new Date();
        task.setCreateDate(now);
        task.setModifiedDate(now);

        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setImageId(imageId);
        task.setCategoryId(categoryId);

        task.setIsCompleted(false);
        task.setIsDeleted(false);

        return taskPersistence.update(task);
    }

    /**
     * Atualiza os dados de uma tarefa (compatibilidade retroativa, sem categoria).
     */
    public Task updateTask(long taskId, String title, String description, Date dueDate, long imageId) throws PortalException {
        Task task = taskPersistence.findByPrimaryKey(taskId);
        return updateTask(taskId, title, description, dueDate, imageId, task.getCategoryId());
    }

    /**
     * Atualiza os dados de uma tarefa, incluindo a categoria.
     */
    public Task updateTask(long taskId, String title, String description, Date dueDate, long imageId, long categoryId) throws PortalException {
        Task task = taskPersistence.findByPrimaryKey(taskId);

        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setImageId(imageId);
        task.setCategoryId(categoryId);
        task.setModifiedDate(new Date());

        return taskPersistence.update(task);
    }

    /**
     * Método de exclusão lógica (Soft Delete).
     * Em vez de apagar do banco definitivamente, mudamos a flag isDeleted para verdadeiro (true).
     */
    public Task softDeleteTask(long taskId) throws PortalException {
        // Busca a tarefa existente pelo seu ID
        Task task = taskPersistence.findByPrimaryKey(taskId);

        task.setIsDeleted(true); // Marca como deletada para o histórico
        task.setModifiedDate(new Date()); // Atualiza a data de modificação

        return taskPersistence.update(task);
    }

    /**
     * Busca apenas as tarefas ATIVAS do usuário.
     * Este método vai devolver uma coleção (várias tarefas em lista), e não apenas uma.
     */
    public List<Task> getActiveTasksByUserId(long groupId, long userId) {
        // Passamos 'false' no último parâmetro porque queremos apenas as tarefas onde isDeleted seja FALSO.
        return taskPersistence.findByUserActiveTasks(groupId, userId, false);
    }

    /**
     * Busca o HISTÓRICO de tarefas do usuário.
     */
    public List<Task> getHistoryTasksByUserId(long groupId, long userId) {
        // Retorna tudo do usuário (deletadas ou não) para compor a visão de histórico/calendário
        return taskPersistence.findByUserHistoryTasks(groupId, userId);
    }

    /**
     * Altera o status da tarefa entre concluída e pendente.
     */
    public Task toggleTaskStatus(long taskId) throws PortalException {
        Task task = taskPersistence.findByPrimaryKey(taskId);

        // Se a tarefa estiver concluída (true), o '!' transforma em false. Se estiver pendente (false), vira true.
        task.setIsCompleted(!task.getIsCompleted());
        task.setModifiedDate(new Date());

        return taskPersistence.update(task);
    }
}