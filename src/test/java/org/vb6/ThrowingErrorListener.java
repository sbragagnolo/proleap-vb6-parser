/*
Copyright (C) 2014 u.wol@wwu.de

This file is part of vb6grammar.

vb6grammar is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

vb6grammar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with vb6grammar. If not, see <http://www.gnu.org/licenses/>.
 */

package org.vb6;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

public class ThrowingErrorListener extends BaseErrorListener {

	public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

	@Override
	public void syntaxError(@NotNull final Recognizer<?, ?> recognizer, @Nullable final Object offendingSymbol,
			final int line, final int charPositionInLine, @NotNull final String msg,
			@Nullable final RecognitionException e) {
		throw new RuntimeException("syntax error in line " + line + ":" + charPositionInLine + " " + msg);
	}
}