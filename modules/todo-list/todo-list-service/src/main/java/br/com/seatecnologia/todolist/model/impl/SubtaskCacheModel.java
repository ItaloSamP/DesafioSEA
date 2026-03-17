/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.model.impl;

import br.com.seatecnologia.todolist.model.Subtask;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing Subtask in entity cache.
 *
 * @author Carlos
 * @generated
 */
public class SubtaskCacheModel implements CacheModel<Subtask>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SubtaskCacheModel)) {
			return false;
		}

		SubtaskCacheModel subtaskCacheModel = (SubtaskCacheModel)object;

		if (subtaskId == subtaskCacheModel.subtaskId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, subtaskId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(15);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", subtaskId=");
		sb.append(subtaskId);
		sb.append(", taskId=");
		sb.append(taskId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", title=");
		sb.append(title);
		sb.append(", isCompleted=");
		sb.append(isCompleted);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Subtask toEntityModel() {
		SubtaskImpl subtaskImpl = new SubtaskImpl();

		if (uuid == null) {
			subtaskImpl.setUuid("");
		}
		else {
			subtaskImpl.setUuid(uuid);
		}

		subtaskImpl.setSubtaskId(subtaskId);
		subtaskImpl.setTaskId(taskId);

		if (createDate == Long.MIN_VALUE) {
			subtaskImpl.setCreateDate(null);
		}
		else {
			subtaskImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			subtaskImpl.setModifiedDate(null);
		}
		else {
			subtaskImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (title == null) {
			subtaskImpl.setTitle("");
		}
		else {
			subtaskImpl.setTitle(title);
		}

		subtaskImpl.setIsCompleted(isCompleted);

		subtaskImpl.resetOriginalValues();

		return subtaskImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		subtaskId = objectInput.readLong();

		taskId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		title = objectInput.readUTF();

		isCompleted = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(subtaskId);

		objectOutput.writeLong(taskId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (title == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(title);
		}

		objectOutput.writeBoolean(isCompleted);
	}

	public String uuid;
	public long subtaskId;
	public long taskId;
	public long createDate;
	public long modifiedDate;
	public String title;
	public boolean isCompleted;

}