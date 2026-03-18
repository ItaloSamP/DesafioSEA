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
 * The extended model interface for the Comment service. Represents a row in the &quot;SEA_Comment&quot; database table, with each column mapped to a property of this class.
 *
 * @author Carlos
 * @see CommentModel
 * @generated
 */
@ImplementationClassName("br.com.seatecnologia.todolist.model.impl.CommentImpl")
@ProviderType
public interface Comment extends CommentModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>br.com.seatecnologia.todolist.model.impl.CommentImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Comment, Long> COMMENT_ID_ACCESSOR =
		new Accessor<Comment, Long>() {

			@Override
			public Long get(Comment comment) {
				return comment.getCommentId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<Comment> getTypeClass() {
				return Comment.class;
			}

		};

}