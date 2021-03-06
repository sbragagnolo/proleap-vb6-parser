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
import io.proleap.vb6.asg.metamodel.api.ApiEnumerationConstant;
import io.proleap.vb6.asg.metamodel.call.ApiEnumerationConstantCall;
import io.proleap.vb6.asg.metamodel.type.Type;
import io.proleap.vb6.asg.visitor.VBASGVisitor;

public class ApiEnumerationConstantCallImpl extends CallImpl implements ApiEnumerationConstantCall {

	protected ApiEnumerationConstant apiEnumerationConstant;

	protected boolean standalone;

	public ApiEnumerationConstantCallImpl(final String name, final ApiEnumerationConstant apiEnumerationConstant,
			final Module module, final Scope scope, final ParserRuleContext ctx) {
		super(name, module, scope, ctx);

		this.apiEnumerationConstant = apiEnumerationConstant;
	}

	@Override
	public ApiEnumerationConstant getApiEnumerationConstant() {
		return apiEnumerationConstant;
	}

	@Override
	public CallType getCallType() {
		return CallType.API_ENUMERATION_CONSTANT_CALL;
	}

	@Override
	public Type getType() {
		final Type result;

		if (apiEnumerationConstant != null) {
			result = apiEnumerationConstant.getType();
		} else {
			result = null;
		}

		return result;
	}

	@Override
	public boolean isStandaloneCall() {
		return standalone;
	}

	@Override
	public void setStandaloneCall(final boolean standalone) {
		this.standalone = standalone;
	}

	@Override
	public String toString() {
		return super.toString() + ", apiEnumerationConstant=[" + apiEnumerationConstant + "]";
	}

	@Override
	public void acceptVisitor(VBASGVisitor visitor) {
		// TODO Auto-generated method stub
			visitor.visit(this);
		
	}
}
