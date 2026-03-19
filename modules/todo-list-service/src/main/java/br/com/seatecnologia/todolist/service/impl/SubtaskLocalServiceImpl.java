package br.com.seatecnologia.todolist.service.impl;

import br.com.seatecnologia.todolist.model.Subtask;
import br.com.seatecnologia.todolist.service.base.SubtaskLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.Date;
import java.util.List;

/**
 * Lógica de negócio para as Subtarefas (itens das sublistas).
 */
public class SubtaskLocalServiceImpl extends SubtaskLocalServiceBaseImpl {

    /**
     * Adiciona uma nova subtarefa vinculada a uma tarefa principal.
     */
    public Subtask addSubtask(long taskId, String title) {
        // O Liferay gerencia a geração de IDs únicos através do CounterLocalService
        long subtaskId = counterLocalService.increment(Subtask.class.getName());
        Subtask subtask = subtaskPersistence.create(subtaskId);

        Date now = new Date();
        subtask.setCreateDate(now);
        subtask.setModifiedDate(now);

        // Relacionamento com a Task principal
        subtask.setTaskId(taskId);
        
        subtask.setTitle(title);
        subtask.setIsCompleted(false);

        return subtaskPersistence.update(subtask);
    }

    /**
     * Retorna a sublista completa de uma tarefa específica.
     */
    public List<Subtask> getSubtasksByTaskId(long taskId) {
        // Utiliza o Finder gerado pelo service.xml para buscar por foreign key
        return subtaskPersistence.findByTaskSubtasks(taskId);
    }

    /**
     * Alterna o status da subtarefa.
     */
    public Subtask toggleSubtaskStatus(long subtaskId) throws PortalException {
        Subtask subtask = subtaskPersistence.findByPrimaryKey(subtaskId);

        subtask.setIsCompleted(!subtask.getIsCompleted());
        subtask.setModifiedDate(new Date());

        return subtaskPersistence.update(subtask);
    }

    /**
     * Remove uma subtarefa pelo ID e retorna o objeto removido.
     * O ownership check (verificar se o usuário é dono da task pai) é feito
     * no ActionCommand antes de chamar este método.
     */
    public Subtask deleteSubtask(long subtaskId) throws PortalException {
        return subtaskPersistence.remove(subtaskId);
    }

}