/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;SEA_Subtask&quot; database table.
 *
 * @author Carlos
 * @see Subtask
 * @generated
 */
public class SubtaskTable extends BaseTable<SubtaskTable> {

	public static final SubtaskTable INSTANCE = new SubtaskTable();

	public final Column<SubtaskTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<SubtaskTable, Long> subtaskId = createColumn(
		"subtaskId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<SubtaskTable, Long> taskId = createColumn(
		"taskId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<SubtaskTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<SubtaskTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<SubtaskTable, String> title = createColumn(
		"title", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<SubtaskTable, Boolean> isCompleted = createColumn(
		"isCompleted", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);

	private SubtaskTable() {
		super("SEA_Subtask", SubtaskTable::new);
	}

}