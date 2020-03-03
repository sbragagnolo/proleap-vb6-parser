/*
 * Copyright (C) 2017, Ulrich Wolffgang <ulrich.wolffgang@proleap.io>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.call.impl;

import org.antlr.v4.runtime.ParserRuleContext;

import io.proleap.vb6.asg.metamodel.Module;
import io.proleap.vb6.asg.metamodel.Scope;
import io.proleap.vb6.asg.metamodel.call.ConstantCall;
import io.proleap.vb6.asg.metamodel.statement.constant.Constant;
import io.proleap.vb6.asg.metamodel.type.Type;
import io.proleap.vb6.asg.visitor.VBASGVisitor;

public class ConstantCallImpl extends CallImpl implements ConstantCall {

	protected Constant constant;

	public ConstantCallImpl(final String name, final Constant constant, final Module module, final Scope scope,
			final ParserRuleContext ctx) {
		super(name, module, scope, ctx);

		this.constant = constant;
	}

	@Override
	public CallType getCallType() {
		return CallType.CONSTANT_CALL;
	}

	@Override
	public Constant getConstant() {
		return constant;
	}

	@Override
	public Type getType() {
		final Type result;

		if (constant != null) {
			result = constant.getType();
		} else {
			result = null;
		}

		return result;
	}

	@Override
	public String toString() {
		return super.toString() + ", constant=[" + constant + "]";
	}

	@Override
	public void acceptVisitor(VBASGVisitor visitor) {
		// TODO Auto-generated method stub
			visitor.visit(this);
		
	}
}
