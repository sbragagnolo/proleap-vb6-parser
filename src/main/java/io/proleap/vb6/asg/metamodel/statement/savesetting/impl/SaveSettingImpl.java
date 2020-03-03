/*
 * Copyright (C) 2017, Ulrich Wolffgang <ulrich.wolffgang@proleap.io>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.statement.savesetting.impl;

import io.proleap.vb6.VisualBasic6Parser.SaveSettingStmtContext;
import io.proleap.vb6.asg.metamodel.Module;
import io.proleap.vb6.asg.metamodel.Scope;
import io.proleap.vb6.asg.metamodel.impl.ScopedElementImpl;
import io.proleap.vb6.asg.metamodel.statement.StatementType;
import io.proleap.vb6.asg.metamodel.statement.StatementTypeEnum;
import io.proleap.vb6.asg.metamodel.statement.savesetting.SaveSetting;
import io.proleap.vb6.asg.visitor.VBASGVisitor;

public class SaveSettingImpl extends ScopedElementImpl implements SaveSetting {

	protected final SaveSettingStmtContext ctx;

	protected final StatementType statementType = StatementTypeEnum.SAVE_SETTING;

	public SaveSettingImpl(final Module module, final Scope scope, final SaveSettingStmtContext ctx) {
		super(module.getProgram(), module, scope, ctx);

		this.ctx = ctx;
	}

	@Override
	public SaveSettingStmtContext getCtx() {
		return ctx;
	}

	@Override
	public StatementType getStatementType() {
		return statementType;
	}

	@Override
	public void acceptVisitor(VBASGVisitor visitor) {
		// TODO Auto-generated method stub
			visitor.visit(this);
		
	}

}
