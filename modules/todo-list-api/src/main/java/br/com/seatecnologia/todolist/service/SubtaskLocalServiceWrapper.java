/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.service;

import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * Provides a wrapper for {@link SubtaskLocalService}.
 *
 * @author Carlos
 * @see SubtaskLocalService
 * @generated
 */
public class SubtaskLocalServiceWrapper
	implements ServiceWrapper<SubtaskLocalService>, SubtaskLocalService {

	public SubtaskLocalServiceWrapper() {
		this(null);
	}

	public SubtaskLocalServiceWrapper(SubtaskLocalService subtaskLocalService) {
		_subtaskLocalService = subtaskLocalService;
	}

	/**
	 * Adiciona uma nova subtarefa vinculada a uma tarefa principal.
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask addSubtask(
		long taskId, String title) {

		return _subtaskLocalService.addSubtask(taskId, title);
	}

	/**
	 * Adds the subtask to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubtaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param subtask the subtask
	 * @return the subtask that was added
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask addSubtask(
		br.com.seatecnologia.todolist.model.Subtask subtask) {

		return _subtaskLocalService.addSubtask(subtask);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _subtaskLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new subtask with the primary key. Does not add the subtask to the database.
	 *
	 * @param subtaskId the primary key for the new subtask
	 * @return the new subtask
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask createSubtask(
		long subtaskId) {

		return _subtaskLocalService.createSubtask(subtaskId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _subtaskLocalService.deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the subtask with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubtaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param subtaskId the primary key of the subtask
	 * @return the subtask that was removed
	 * @throws PortalException if a subtask with the primary key could not be found
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask deleteSubtask(
			long subtaskId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _subtaskLocalService.deleteSubtask(subtaskId);
	}

	/**
	 * Deletes the subtask from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubtaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param subtask the subtask
	 * @return the subtask that was removed
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask deleteSubtask(
		br.com.seatecnologia.todolist.model.Subtask subtask) {

		return _subtaskLocalService.deleteSubtask(subtask);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _subtaskLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _subtaskLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _subtaskLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _subtaskLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>br.com.seatecnologia.todolist.model.impl.SubtaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _subtaskLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>br.com.seatecnologia.todolist.model.impl.SubtaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _subtaskLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _subtaskLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _subtaskLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public br.com.seatecnologia.todolist.model.Subtask fetchSubtask(
		long subtaskId) {

		return _subtaskLocalService.fetchSubtask(subtaskId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _subtaskLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _subtaskLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _subtaskLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _subtaskLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the subtask with the primary key.
	 *
	 * @param subtaskId the primary key of the subtask
	 * @return the subtask
	 * @throws PortalException if a subtask with the primary key could not be found
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask getSubtask(
			long subtaskId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _subtaskLocalService.getSubtask(subtaskId);
	}

	/**
	 * Returns a range of all the subtasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>br.com.seatecnologia.todolist.model.impl.SubtaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of subtasks
	 * @param end the upper bound of the range of subtasks (not inclusive)
	 * @return the range of subtasks
	 */
	@Override
	public java.util.List<br.com.seatecnologia.todolist.model.Subtask>
		getSubtasks(int start, int end) {

		return _subtaskLocalService.getSubtasks(start, end);
	}

	/**
	 * Retorna a sublista completa de uma tarefa específica.
	 */
	@Override
	public java.util.List<br.com.seatecnologia.todolist.model.Subtask>
		getSubtasksByTaskId(long taskId) {

		return _subtaskLocalService.getSubtasksByTaskId(taskId);
	}

	/**
	 * Returns the number of subtasks.
	 *
	 * @return the number of subtasks
	 */
	@Override
	public int getSubtasksCount() {
		return _subtaskLocalService.getSubtasksCount();
	}

	/**
	 * Alterna o status da subtarefa.
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask toggleSubtaskStatus(
			long subtaskId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _subtaskLocalService.toggleSubtaskStatus(subtaskId);
	}

	/**
	 * Updates the subtask in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubtaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param subtask the subtask
	 * @return the subtask that was updated
	 */
	@Override
	public br.com.seatecnologia.todolist.model.Subtask updateSubtask(
		br.com.seatecnologia.todolist.model.Subtask subtask) {

		return _subtaskLocalService.updateSubtask(subtask);
	}

	@Override
	public BasePersistence<?> getBasePersistence() {
		return _subtaskLocalService.getBasePersistence();
	}

	@Override
	public SubtaskLocalService getWrappedService() {
		return _subtaskLocalService;
	}

	@Override
	public void setWrappedService(SubtaskLocalService subtaskLocalService) {
		_subtaskLocalService = subtaskLocalService;
	}

	private SubtaskLocalService _subtaskLocalService;

}