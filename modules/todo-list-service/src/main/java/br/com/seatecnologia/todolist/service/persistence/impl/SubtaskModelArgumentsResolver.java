/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.service.persistence.impl;

import br.com.seatecnologia.todolist.model.SubtaskTable;
import br.com.seatecnologia.todolist.model.impl.SubtaskImpl;
import br.com.seatecnologia.todolist.model.impl.SubtaskModelImpl;

import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The arguments resolver class for retrieving value from Subtask.
 *
 * @author Carlos
 * @generated
 */
@OSGiBeanProperties(
	property = {
		"class.name=br.com.seatecnologia.todolist.model.impl.SubtaskImpl",
		"table.name=SEA_Subtask"
	},
	service = ArgumentsResolver.class
)
public class SubtaskModelArgumentsResolver implements ArgumentsResolver {

	@Override
	public Object[] getArguments(
		FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
		boolean original) {

		String[] columnNames = finderPath.getColumnNames();

		if ((columnNames == null) || (columnNames.length == 0)) {
			if (baseModel.isNew()) {
				return new Object[0];
			}

			return null;
		}

		SubtaskModelImpl subtaskModelImpl = (SubtaskModelImpl)baseModel;

		long columnBitmask = subtaskModelImpl.getColumnBitmask();

		if (!checkColumn || (columnBitmask == 0)) {
			return _getValue(subtaskModelImpl, columnNames, original);
		}

		Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
			finderPath);

		if (finderPathColumnBitmask == null) {
			finderPathColumnBitmask = 0L;

			for (String columnName : columnNames) {
				finderPathColumnBitmask |= subtaskModelImpl.getColumnBitmask(
					columnName);
			}

			_finderPathColumnBitmasksCache.put(
				finderPath, finderPathColumnBitmask);
		}

		if ((columnBitmask & finderPathColumnBitmask) != 0) {
			return _getValue(subtaskModelImpl, columnNames, original);
		}

		return null;
	}

	@Override
	public String getClassName() {
		return SubtaskImpl.class.getName();
	}

	@Override
	public String getTableName() {
		return SubtaskTable.INSTANCE.getTableName();
	}

	private static Object[] _getValue(
		SubtaskModelImpl subtaskModelImpl, String[] columnNames,
		boolean original) {

		Object[] arguments = new Object[columnNames.length];

		for (int i = 0; i < arguments.length; i++) {
			String columnName = columnNames[i];

			if (original) {
				arguments[i] = subtaskModelImpl.getColumnOriginalValue(
					columnName);
			}
			else {
				arguments[i] = subtaskModelImpl.getColumnValue(columnName);
			}
		}

		return arguments;
	}

	private static final Map<FinderPath, Long> _finderPathColumnBitmasksCache =
		new ConcurrentHashMap<>();

}