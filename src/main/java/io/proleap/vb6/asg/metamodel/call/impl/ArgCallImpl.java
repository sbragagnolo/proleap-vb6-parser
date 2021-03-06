/*
 * Copyright (C) 2017, Ulrich Wolffgang <ulrich.wolffgang@proleap.io>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.call.impl;

import org.antlr.v4.runtime.ParserRuleContext;

import io.proleap.vb6.asg.metamodel.Arg;
import io.proleap.vb6.asg.metamodel.Module;
import io.proleap.vb6.asg.metamodel.Scope;
import io.proleap.vb6.asg.metamodel.call.ArgCall;
import io.proleap.vb6.asg.metamodel.type.Type;
import io.proleap.vb6.asg.visitor.VBASGVisitor;

public class ArgCallImpl extends CallImpl implements ArgCall {

	protected Arg arg;

	public ArgCallImpl(final String name, final Arg arg, final Module module, final Scope scope, final ParserRuleContext ctx) {
		super(name, module, scope, ctx);

		this.arg = arg;
	}

	@Override
	public Arg getArg() {
		return arg;
	}

	@Override
	public CallType getCallType() {
		return CallType.ARG_CALL;
	}

	@Override
	public Type getType() {
		final Type result;

		if (arg != null) {
			result = arg.getType();
		} else {
			result = null;
		}

		return result;
	}

	@Override
	public String toString() {
		return super.toString() + ", arg=[" + arg + "]";
	}

	@Override
	public void acceptVisitor(VBASGVisitor visitor) {
		// TODO Auto-generated method stub
			visitor.visit(this);
		
	}
}
