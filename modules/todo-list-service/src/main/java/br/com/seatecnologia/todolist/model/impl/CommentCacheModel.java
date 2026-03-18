/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.model.impl;

import br.com.seatecnologia.todolist.model.Comment;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing Comment in entity cache.
 *
 * @author Carlos
 * @generated
 */
public class CommentCacheModel implements CacheModel<Comment>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CommentCacheModel)) {
			return false;
		}

		CommentCacheModel commentCacheModel = (CommentCacheModel)object;

		if (commentId == commentCacheModel.commentId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, commentId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", commentId=");
		sb.append(commentId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", taskId=");
		sb.append(taskId);
		sb.append(", text=");
		sb.append(text);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Comment toEntityModel() {
		CommentImpl commentImpl = new CommentImpl();

		if (uuid == null) {
			commentImpl.setUuid("");
		}
		else {
			commentImpl.setUuid(uuid);
		}

		commentImpl.setCommentId(commentId);
		commentImpl.setGroupId(groupId);
		commentImpl.setCompanyId(companyId);
		commentImpl.setUserId(userId);

		if (userName == null) {
			commentImpl.setUserName("");
		}
		else {
			commentImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			commentImpl.setCreateDate(null);
		}
		else {
			commentImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			commentImpl.setModifiedDate(null);
		}
		else {
			commentImpl.setModifiedDate(new Date(modifiedDate));
		}

		commentImpl.setTaskId(taskId);

		if (text == null) {
			commentImpl.setText("");
		}
		else {
			commentImpl.setText(text);
		}

		commentImpl.resetOriginalValues();

		return commentImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		commentId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		taskId = objectInput.readLong();
		text = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(commentId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(taskId);

		if (text == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(text);
		}
	}

	public String uuid;
	public long commentId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long taskId;
	public String text;

}