/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
package br.com.seatecnologia.todolist.exception;

import com.liferay.portal.kernel.exception.NoSuchModelException;

/**
 * @author Carlos
 */
public class NoSuchCategoryException extends NoSuchModelException {

	public NoSuchCategoryException() {
	}

	public NoSuchCategoryException(String msg) {
		super(msg);
	}

	public NoSuchCategoryException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public NoSuchCategoryException(Throwable throwable) {
		super(throwable);
	}

}