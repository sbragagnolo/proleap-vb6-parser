/*
 * Copyright (C) 2017, Ulrich Wolffgang <ulrich.wolffgang@proleap.io>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.impl;

import org.antlr.v4.runtime.CommonTokenStream;

import io.proleap.vb6.VisualBasic6Parser.ModuleContext;
import io.proleap.vb6.asg.metamodel.Program;
import io.proleap.vb6.asg.metamodel.StandardModule;
import io.proleap.vb6.asg.visitor.VBASGVisitor;

public class StandardModuleImpl extends ModuleImpl implements StandardModule {

	public StandardModuleImpl(final String name, final Program program, final CommonTokenStream tokens,
			final ModuleContext ctx) {
		super(name, program, tokens, ctx);

		program.registerStandardModule(this);
	}

	@Override
	public void acceptVisitor(VBASGVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
		
	}

}
