/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the Task service. Represents a row in the &quot;SEA_Task&quot; database table, with each column mapped to a property of this class.
 *
 * @author Carlos
 * @see TaskModel
 * @generated
 */
@ImplementationClassName("br.com.seatecnologia.todolist.model.impl.TaskImpl")
@ProviderType
public interface Task extends PersistedModel, TaskModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>br.com.seatecnologia.todolist.model.impl.TaskImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Task, Long> TASK_ID_ACCESSOR =
		new Accessor<Task, Long>() {

			@Override
			public Long get(Task task) {
				return task.getTaskId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<Task> getTypeClass() {
				return Task.class;
			}

		};

}