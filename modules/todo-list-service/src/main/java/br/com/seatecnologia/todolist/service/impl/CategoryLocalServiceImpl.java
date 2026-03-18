/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.service.impl;

import br.com.seatecnologia.todolist.model.Category;
import br.com.seatecnologia.todolist.service.base.CategoryLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import java.util.Date;
import java.util.List;

/**
 * Centraliza as regras de negócio de Categorias.
 *
 * @author Carlos
 */
public class CategoryLocalServiceImpl extends CategoryLocalServiceBaseImpl {

    /**
     * Cria uma nova categoria para o usuário no grupo.
     */
    public Category addCategory(long userId, long groupId, String name)
        throws PortalException {

        long categoryId = counterLocalService.increment(Category.class.getName());
        Category category = categoryPersistence.create(categoryId);

        User user = UserLocalServiceUtil.getUser(userId);

        category.setGroupId(groupId);
        category.setCompanyId(user.getCompanyId());
        category.setUserId(userId);
        category.setUserName(user.getFullName());

        Date now = new Date();
        category.setCreateDate(now);
        category.setModifiedDate(now);

        category.setName(name);

        return categoryPersistence.update(category);
    }

    /**
     * Busca todas as categorias do usuário no grupo.
     */
    public List<Category> getCategoriesByUserId(long groupId, long userId) {
        return categoryPersistence.findByUserCategories(groupId, userId);
    }

    /**
     * Renomeia uma categoria existente.
     */
    public Category updateCategory(long categoryId, String name)
        throws PortalException {

        Category category = categoryPersistence.findByPrimaryKey(categoryId);

        category.setName(name);
        category.setModifiedDate(new Date());

        return categoryPersistence.update(category);
    }

    /**
     * Remove uma categoria. Tarefas vinculadas ficam com categoryId = 0 (sem categoria).
     */
    public Category deleteCategory(long categoryId) throws PortalException {
        return categoryPersistence.remove(categoryId);
    }
}