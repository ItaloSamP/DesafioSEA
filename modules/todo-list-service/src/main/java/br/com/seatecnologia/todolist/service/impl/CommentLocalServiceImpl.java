/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package br.com.seatecnologia.todolist.service.impl;

import br.com.seatecnologia.todolist.model.Comment;
import br.com.seatecnologia.todolist.service.base.CommentLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import java.util.Date;
import java.util.List;

/**
 * Lógica de negócio para os Comentários de uma tarefa.
 *
 * @author Carlos
 */
public class CommentLocalServiceImpl extends CommentLocalServiceBaseImpl {

    /**
     * Adiciona um comentário a uma tarefa.
     * Segue o mesmo padrão do TaskLocalServiceImpl: gera ID, busca o User
     * para pegar companyId e userName, seta todos os campos e persiste.
     */
    public Comment addComment(long userId, long groupId, long taskId, String text)
        throws PortalException {

        long commentId = counterLocalService.increment(Comment.class.getName());
        Comment comment = commentPersistence.create(commentId);

        User user = UserLocalServiceUtil.getUser(userId);

        comment.setGroupId(groupId);
        comment.setCompanyId(user.getCompanyId());
        comment.setUserId(userId);
        comment.setUserName(user.getFullName());

        Date now = new Date();
        comment.setCreateDate(now);
        comment.setModifiedDate(now);

        comment.setTaskId(taskId);
        comment.setText(text);

        return commentPersistence.update(comment);
    }

    /**
     * Retorna todos os comentários de uma tarefa, ordenados por data de criação
     * (a ordenação é definida no finder do service.xml via order-by-col).
     */
    public List<Comment> getCommentsByTaskId(long taskId) {
        return commentPersistence.findByTaskComments(taskId);
    }

    /**
     * Atualiza o texto de um comentário existente.
     * O ownership check é feito no ActionCommand, não aqui.
     */
    public Comment updateComment(long commentId, String text) throws PortalException {
        Comment comment = commentPersistence.findByPrimaryKey(commentId);
        comment.setText(text);
        comment.setModifiedDate(new Date());
        return commentPersistence.update(comment);
    }

    /**
     * Remove um comentário pelo ID e retorna o objeto removido.
     * O ownership check é feito no ActionCommand, não aqui.
     */
    public Comment deleteComment(long commentId) throws PortalException {
        return commentPersistence.remove(commentId);
    }
}