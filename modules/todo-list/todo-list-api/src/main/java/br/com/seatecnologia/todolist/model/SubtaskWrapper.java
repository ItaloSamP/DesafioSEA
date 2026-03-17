/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Subtask}.
 * </p>
 *
 * @author Carlos
 * @see Subtask
 * @generated
 */
public class SubtaskWrapper
	extends BaseModelWrapper<Subtask>
	implements ModelWrapper<Subtask>, Subtask {

	public SubtaskWrapper(Subtask subtask) {
		super(subtask);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("subtaskId", getSubtaskId());
		attributes.put("taskId", getTaskId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("title", getTitle());
		attributes.put("isCompleted", isIsCompleted());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long subtaskId = (Long)attributes.get("subtaskId");

		if (subtaskId != null) {
			setSubtaskId(subtaskId);
		}

		Long taskId = (Long)attributes.get("taskId");

		if (taskId != null) {
			setTaskId(taskId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String title = (String)attributes.get("title");

		if (title != null) {
			setTitle(title);
		}

		Boolean isCompleted = (Boolean)attributes.get("isCompleted");

		if (isCompleted != null) {
			setIsCompleted(isCompleted);
		}
	}

	@Override
	public Subtask cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the create date of this subtask.
	 *
	 * @return the create date of this subtask
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the is completed of this subtask.
	 *
	 * @return the is completed of this subtask
	 */
	@Override
	public boolean getIsCompleted() {
		return model.getIsCompleted();
	}

	/**
	 * Returns the modified date of this subtask.
	 *
	 * @return the modified date of this subtask
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the primary key of this subtask.
	 *
	 * @return the primary key of this subtask
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the subtask ID of this subtask.
	 *
	 * @return the subtask ID of this subtask
	 */
	@Override
	public long getSubtaskId() {
		return model.getSubtaskId();
	}

	/**
	 * Returns the task ID of this subtask.
	 *
	 * @return the task ID of this subtask
	 */
	@Override
	public long getTaskId() {
		return model.getTaskId();
	}

	/**
	 * Returns the title of this subtask.
	 *
	 * @return the title of this subtask
	 */
	@Override
	public String getTitle() {
		return model.getTitle();
	}

	/**
	 * Returns the uuid of this subtask.
	 *
	 * @return the uuid of this subtask
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns <code>true</code> if this subtask is is completed.
	 *
	 * @return <code>true</code> if this subtask is is completed; <code>false</code> otherwise
	 */
	@Override
	public boolean isIsCompleted() {
		return model.isIsCompleted();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the create date of this subtask.
	 *
	 * @param createDate the create date of this subtask
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets whether this subtask is is completed.
	 *
	 * @param isCompleted the is completed of this subtask
	 */
	@Override
	public void setIsCompleted(boolean isCompleted) {
		model.setIsCompleted(isCompleted);
	}

	/**
	 * Sets the modified date of this subtask.
	 *
	 * @param modifiedDate the modified date of this subtask
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the primary key of this subtask.
	 *
	 * @param primaryKey the primary key of this subtask
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the subtask ID of this subtask.
	 *
	 * @param subtaskId the subtask ID of this subtask
	 */
	@Override
	public void setSubtaskId(long subtaskId) {
		model.setSubtaskId(subtaskId);
	}

	/**
	 * Sets the task ID of this subtask.
	 *
	 * @param taskId the task ID of this subtask
	 */
	@Override
	public void setTaskId(long taskId) {
		model.setTaskId(taskId);
	}

	/**
	 * Sets the title of this subtask.
	 *
	 * @param title the title of this subtask
	 */
	@Override
	public void setTitle(String title) {
		model.setTitle(title);
	}

	/**
	 * Sets the uuid of this subtask.
	 *
	 * @param uuid the uuid of this subtask
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	public String toXmlString() {
		return model.toXmlString();
	}

	@Override
	protected SubtaskWrapper wrap(Subtask subtask) {
		return new SubtaskWrapper(subtask);
	}

}