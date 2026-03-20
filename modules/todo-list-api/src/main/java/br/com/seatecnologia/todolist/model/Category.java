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
 * The extended model interface for the Category service. Represents a row in the &quot;SEA_Category&quot; database table, with each column mapped to a property of this class.
 *
 * @author Carlos
 * @see CategoryModel
 * @generated
 */
@ImplementationClassName(
	"br.com.seatecnologia.todolist.model.impl.CategoryImpl"
)
@ProviderType
public interface Category extends CategoryModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>br.com.seatecnologia.todolist.model.impl.CategoryImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Category, Long> CATEGORY_ID_ACCESSOR =
		new Accessor<Category, Long>() {

			@Override
			public Long get(Category category) {
				return category.getCategoryId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<Category> getTypeClass() {
				return Category.class;
			}

		};

}