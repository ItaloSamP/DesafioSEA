/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.service;

import br.com.seatecnologia.todolist.model.Task;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the local service interface for Task. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Carlos
 * @see TaskLocalServiceUtil
 * @generated
 */
@OSGiBeanProperties(
	property = {"model.class.name=br.com.seatecnologia.todolist.model.Task"}
)
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface TaskLocalService
	extends BaseLocalService, PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>br.com.seatecnologia.todolist.service.impl.TaskLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the task local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link TaskLocalServiceUtil} if injection and service tracking are not available.
	 */

	/**
	 * Método para criar uma nova tarefa.
	 */
	public Task addTask(
			long userId, long groupId, String title, String description,
			Date dueDate, long imageId)
		throws PortalException;

	/**
	 * Método para criar uma nova tarefa vinculada a uma categoria.
	 */
	public Task addTask(
			long userId, long groupId, String title, String description,
			Date dueDate, long imageId, long categoryId)
		throws PortalException;

	/**
	 * Adds the task to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param task the task
	 * @return the task that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	public Task addTask(Task task);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	 * Creates a new task with the primary key. Does not add the task to the database.
	 *
	 * @param taskId the primary key for the new task
	 * @return the new task
	 */
	@Transactional(enabled = false)
	public Task createTask(long taskId);

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	/**
	 * Deletes the task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param taskId the primary key of the task
	 * @return the task that was removed
	 * @throws PortalException if a task with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	public Task deleteTask(long taskId) throws PortalException;

	/**
	 * Deletes the task from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param task the task
	 * @return the task that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	public Task deleteTask(Task task);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> T dslQuery(DSLQuery dslQuery);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int dslQueryCount(DSLQuery dslQuery);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DynamicQuery dynamicQuery();

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery);

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>br.com.seatecnologia.todolist.model.impl.TaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end);

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>br.com.seatecnologia.todolist.model.impl.TaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(DynamicQuery dynamicQuery);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Task fetchTask(long taskId);

	/**
	 * Returns the task matching the UUID and group.
	 *
	 * @param uuid the task's UUID
	 * @param groupId the primary key of the group
	 * @return the matching task, or <code>null</code> if a matching task could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Task fetchTaskByUuidAndGroupId(String uuid, long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	/**
	 * Busca apenas as tarefas ATIVAS do usuário.
	 * Este método vai devolver uma coleção (várias tarefas em lista), e não apenas uma.
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Task> getActiveTasksByUserId(long groupId, long userId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext);

	/**
	 * Busca o HISTÓRICO de tarefas do usuário.
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Task> getHistoryTasksByUserId(long groupId, long userId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	/**
	 * @throws PortalException
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	 * Returns the task with the primary key.
	 *
	 * @param taskId the primary key of the task
	 * @return the task
	 * @throws PortalException if a task with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Task getTask(long taskId) throws PortalException;

	/**
	 * Returns the task matching the UUID and group.
	 *
	 * @param uuid the task's UUID
	 * @param groupId the primary key of the group
	 * @return the matching task
	 * @throws PortalException if a matching task could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Task getTaskByUuidAndGroupId(String uuid, long groupId)
		throws PortalException;

	/**
	 * Returns a range of all the tasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>br.com.seatecnologia.todolist.model.impl.TaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of tasks
	 * @param end the upper bound of the range of tasks (not inclusive)
	 * @return the range of tasks
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Task> getTasks(int start, int end);

	/**
	 * Returns all the tasks matching the UUID and company.
	 *
	 * @param uuid the UUID of the tasks
	 * @param companyId the primary key of the company
	 * @return the matching tasks, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Task> getTasksByUuidAndCompanyId(String uuid, long companyId);

	/**
	 * Returns a range of tasks matching the UUID and company.
	 *
	 * @param uuid the UUID of the tasks
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of tasks
	 * @param end the upper bound of the range of tasks (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching tasks, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Task> getTasksByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<Task> orderByComparator);

	/**
	 * Returns the number of tasks.
	 *
	 * @return the number of tasks
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getTasksCount();

	/**
	 * Método de exclusão lógica (Soft Delete).
	 * Em vez de apagar do banco definitivamente, mudamos a flag isDeleted para verdadeiro (true).
	 */
	public Task softDeleteTask(long taskId) throws PortalException;

	/**
	 * Altera o status da tarefa entre concluída e pendente.
	 */
	public Task toggleTaskStatus(long taskId) throws PortalException;

	/**
	 * Método para atualizar os dados de uma tarefa existente;
	 */
	public Task updateTask(
			long taskId, String title, String description, Date dueDate,
			long imageId)
		throws PortalException;

	/**
	 * Método para atualizar os dados de uma tarefa existente, incluindo categoria.
	 */
	public Task updateTask(
			long taskId, String title, String description, Date dueDate,
			long imageId, long categoryId)
		throws PortalException;

	/**
	 * Updates the task in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param task the task
	 * @return the task that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	public Task updateTask(Task task);

}