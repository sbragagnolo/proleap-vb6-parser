/*
 * Copyright (C) 2017, Ulrich Wolffgang <ulrich.wolffgang@proleap.io>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.type.impl;

import io.proleap.vb6.asg.metamodel.type.ComplexType;
import io.proleap.vb6.asg.visitor.VBASGVisitor;

public class ComplexTypeImpl extends TypeImpl implements ComplexType {

	public ComplexTypeImpl(final String name, final boolean isCollection) {
		super(name, isCollection);
	}

	@Override
	public void acceptVisitor(VBASGVisitor visitor) {
		// TODO Auto-generated method stub
			visitor.visit(this);
		
	}
}
