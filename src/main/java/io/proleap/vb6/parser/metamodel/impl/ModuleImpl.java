/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.vb6.parser.metamodel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.proleap.vb6.VisualBasic6Parser.ArgContext;
import io.proleap.vb6.VisualBasic6Parser.AttributeStmtContext;
import io.proleap.vb6.VisualBasic6Parser.DeclareStmtContext;
import io.proleap.vb6.VisualBasic6Parser.DeftypeStmtContext;
import io.proleap.vb6.VisualBasic6Parser.EnumerationStmtContext;
import io.proleap.vb6.VisualBasic6Parser.FunctionStmtContext;
import io.proleap.vb6.VisualBasic6Parser.LetterrangeContext;
import io.proleap.vb6.VisualBasic6Parser.ModuleConfigElementContext;
import io.proleap.vb6.VisualBasic6Parser.ModuleContext;
import io.proleap.vb6.VisualBasic6Parser.ModuleHeaderContext;
import io.proleap.vb6.VisualBasic6Parser.OptionBaseStmtContext;
import io.proleap.vb6.VisualBasic6Parser.OptionCompareStmtContext;
import io.proleap.vb6.VisualBasic6Parser.OptionExplicitStmtContext;
import io.proleap.vb6.VisualBasic6Parser.OptionPrivateModuleStmtContext;
import io.proleap.vb6.VisualBasic6Parser.PropertyGetStmtContext;
import io.proleap.vb6.VisualBasic6Parser.PropertyLetStmtContext;
import io.proleap.vb6.VisualBasic6Parser.PropertySetStmtContext;
import io.proleap.vb6.VisualBasic6Parser.SubStmtContext;
import io.proleap.vb6.VisualBasic6Parser.TypeStmtContext;
import io.proleap.vb6.VisualBasic6Parser.TypeStmt_ElementContext;
import io.proleap.vb6.parser.applicationcontext.VbParserContext;
import io.proleap.vb6.parser.metamodel.Attribute;
import io.proleap.vb6.parser.metamodel.DefType;
import io.proleap.vb6.parser.metamodel.Enumeration;
import io.proleap.vb6.parser.metamodel.EnumerationConstant;
import io.proleap.vb6.parser.metamodel.Function;
import io.proleap.vb6.parser.metamodel.Literal;
import io.proleap.vb6.parser.metamodel.Module;
import io.proleap.vb6.parser.metamodel.ModuleConfigElement;
import io.proleap.vb6.parser.metamodel.ProcedureDeclaration;
import io.proleap.vb6.parser.metamodel.Program;
import io.proleap.vb6.parser.metamodel.PropertyGet;
import io.proleap.vb6.parser.metamodel.PropertyLet;
import io.proleap.vb6.parser.metamodel.PropertySet;
import io.proleap.vb6.parser.metamodel.Sub;
import io.proleap.vb6.parser.metamodel.TypeElement;
import io.proleap.vb6.parser.metamodel.VbBaseType;
import io.proleap.vb6.parser.metamodel.oop.ScopedElement;
import io.proleap.vb6.parser.metamodel.oop.Type;
import io.proleap.vb6.parser.util.StringUtils;

public abstract class ModuleImpl extends VbScopeImpl implements Module {

	public final static String NEW_ENUM = "NewEnum";

	protected Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	protected ModuleContext ctx;

	protected List<DefType> defTypes = new ArrayList<DefType>();

	protected Map<String, Enumeration> enumerations = new HashMap<String, Enumeration>();

	protected Map<String, Function> functions = new HashMap<String, Function>();

	protected boolean isCollection;

	protected List<ModuleConfigElement> moduleConfigElements = new ArrayList<ModuleConfigElement>();

	protected final String name;

	protected Integer optionBase;

	protected OptionCompare optionCompare;

	protected Boolean optionExplicit;

	protected Boolean optionPrivateModule;

	protected final Program program;

	protected Map<String, PropertyGet> propertyGets = new HashMap<String, PropertyGet>();

	protected Map<String, PropertyLet> propertyLets = new HashMap<String, PropertyLet>();

	protected Map<String, PropertySet> propertySets = new HashMap<String, PropertySet>();

	protected Map<String, Sub> subs = new HashMap<String, Sub>();

	protected final Map<String, Type> types = new HashMap<String, Type>();

	protected Double version;

	public ModuleImpl(final String name, final Program program, final ModuleContext ctx) {
		super(null, program, ctx);

		this.name = name;
		this.program = program;
		this.ctx = ctx;
		module = this;

		registerASGElement(this);
	}

	@Override
	public Attribute addAttribute(final AttributeStmtContext ctx) {
		Attribute result = (Attribute) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final Type type = determineType(ctx);

			result = new AttributeImpl(name, type, this, this, ctx);

			storeScopedElement(result);
			attributes.put(name, result);

			final Literal literal = addLiteral(ctx.literal(0));
			result.setValue(literal);
		}

		return result;
	}

	@Override
	public ProcedureDeclaration addDeclaration(final DeclareStmtContext ctx) {
		ProcedureDeclaration result = (ProcedureDeclaration) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			result = new ProcedureDeclarationImpl(name, this, this, ctx);

			storeScopedElement(result);

			if (ctx.argList() != null) {
				for (final ArgContext argCtx : ctx.argList().arg()) {
					result.addArg(argCtx);
				}
			}
		}

		return result;
	}

	@Override
	public DefType addDefType(final DeftypeStmtContext ctx) {
		DefType result = (DefType) getSemanticGraphElement(ctx);

		if (result == null) {
			final VbBaseType vbType;

			if (ctx.DEFBOOL() != null) {
				vbType = VbBaseType.BOOLEAN;
			} else if (ctx.DEFBYTE() != null) {
				vbType = VbBaseType.BYTE;
			} else if (ctx.DEFCUR() != null) {
				vbType = VbBaseType.CURRENCY;
			} else if (ctx.DEFDATE() != null) {
				vbType = VbBaseType.DATE;
			} else if (ctx.DEFDBL() != null) {
				vbType = VbBaseType.DOUBLE;
			} else if (ctx.DEFDEC() != null) {
				vbType = VbBaseType.DOUBLE;
			} else if (ctx.DEFINT() != null) {
				vbType = VbBaseType.INTEGER;
			} else if (ctx.DEFLNG() != null) {
				vbType = VbBaseType.LONG;
			} else if (ctx.DEFOBJ() != null) {
				vbType = VbBaseType.VARIANT;
			} else if (ctx.DEFSNG() != null) {
				vbType = VbBaseType.SINGLE;
			} else if (ctx.DEFSTR() != null) {
				vbType = VbBaseType.STRING;
			} else if (ctx.DEFVAR() != null) {
				vbType = VbBaseType.VARIANT;
			} else {
				throw new RuntimeException("unknown deftype " + ctx);
			}

			result = new DefTypeImpl(vbType);

			for (final LetterrangeContext letterRange : ctx.letterrange()) {
				final String lower = letterRange.certainIdentifier(0).getText();

				final String upper;

				if (letterRange.certainIdentifier().size() > 1) {
					upper = letterRange.certainIdentifier(1).getText();
				} else {
					upper = null;
				}

				result.addLetterRange(lower, upper);
			}

			defTypes.add(result);
		}

		return result;
	}

	@Override
	public Enumeration addEnumeration(final EnumerationStmtContext ctx) {
		Enumeration result = (Enumeration) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			result = new EnumerationImpl(name, this, ctx);

			storeScopedElement(result);
			enumerations.put(name, result);
			VbParserContext.getInstance().getTypeRegistry().registerType(result);
		}

		return result;
	}

	@Override
	public Function addFunction(final FunctionStmtContext ctx) {
		Function result = (Function) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final Type type = determineType(ctx.asTypeClause());
			result = new FunctionImpl(name, type, this, ctx);

			storeScopedElement(result);
			functions.put(name, result);

			if (ctx.argList() != null) {
				for (final ArgContext argCtx : ctx.argList().arg()) {
					result.addArg(argCtx);
				}
			}

			/*
			 * check, whether the return type is an array
			 */
			if (ctx.asTypeClause() != null && ctx.asTypeClause().type() != null) {
				final boolean isArray = ctx.asTypeClause().type().LPAREN() != null
						&& ctx.asTypeClause().type().RPAREN() != null;

				result.setDeclaredAsArray(isArray);
			}

			hasNewEnum(name);
		}

		return result;
	}

	@Override
	public ModuleConfigElement addModuleConfigElement(final ModuleConfigElementContext ctx) {
		ModuleConfigElement result = (ModuleConfigElement) getSemanticGraphElement(ctx);

		if (result == null) {

			final String name = determineName(ctx);

			result = new ModuleConfigElementImpl(name, this, this, ctx);
			moduleConfigElements.add(result);

			storeScopedElement(result);
		}

		return result;
	}

	@Override
	public void addModuleHeader(final ModuleHeaderContext ctx) {
		final String versionString = ctx.DOUBLELITERAL().getText();
		version = StringUtils.parseDouble(versionString);
	}

	@Override
	public void addOptionBase(final OptionBaseStmtContext ctx) {
		final String optionBaseString = ctx.INTEGERLITERAL().getText();
		optionBase = StringUtils.parseInteger(optionBaseString);
	}

	@Override
	public void addOptionCompare(final OptionCompareStmtContext ctx) {
		final OptionCompare result;

		if (ctx.BINARY() != null) {
			result = OptionCompare.BINARY;
		} else if (ctx.TEXT() != null) {
			result = OptionCompare.TEXT;
		} else {
			result = null;
		}

		optionCompare = result;
	}

	@Override
	public void addOptionExplicit(final OptionExplicitStmtContext ctx) {
		optionExplicit = ctx.OPTION_EXPLICIT() != null;
	}

	@Override
	public void addOptionPrivateModule(final OptionPrivateModuleStmtContext ctx) {
		optionPrivateModule = ctx.OPTION_PRIVATE_MODULE() != null;
	}

	@Override
	public PropertyGet addPropertyGet(final PropertyGetStmtContext ctx) {
		PropertyGet result = (PropertyGet) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final Type type = determineType(ctx.asTypeClause());
			result = new PropertyGetImpl(name, type, this, ctx);

			storeScopedElement(result);
			propertyGets.put(name, result);

			if (ctx.argList() != null) {
				for (final ArgContext argCtx : ctx.argList().arg()) {
					result.addArg(argCtx);
				}
			}

			/*
			 * check, whether the return type is an array
			 */
			if (ctx.asTypeClause() != null && ctx.asTypeClause().type() != null) {
				final boolean isArray = ctx.asTypeClause().type().LPAREN() != null
						&& ctx.asTypeClause().type().RPAREN() != null;

				result.setDeclaredAsArray(isArray);
			}

			hasNewEnum(name);
		}

		return result;
	}

	@Override
	public PropertyLet addPropertyLet(final PropertyLetStmtContext ctx) {
		PropertyLet result = (PropertyLet) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			result = new PropertyLetImpl(name, this, ctx);

			storeScopedElement(result);
			propertyLets.put(name, result);

			if (ctx.argList() != null) {
				for (final ArgContext argCtx : ctx.argList().arg()) {
					result.addArg(argCtx);
				}
			}
		}

		return result;
	}

	@Override
	public PropertySet addPropertySet(final PropertySetStmtContext ctx) {
		PropertySet result = (PropertySet) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			result = new PropertySetImpl(name, this, ctx);

			storeScopedElement(result);
			propertySets.put(name, result);

			if (ctx.argList() != null) {
				for (final ArgContext argCtx : ctx.argList().arg()) {
					result.addArg(argCtx);
				}
			}
		}

		return result;
	}

	@Override
	public Sub addSub(final SubStmtContext ctx) {
		Sub result = (Sub) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			result = new SubImpl(name, this, ctx);

			storeScopedElement(result);
			subs.put(name, result);

			if (ctx.argList() != null) {
				for (final ArgContext argCtx : ctx.argList().arg()) {
					result.addArg(argCtx);
				}
			}
		}

		return result;
	}

	@Override
	public io.proleap.vb6.parser.metamodel.Type addType(final TypeStmtContext ctx) {
		io.proleap.vb6.parser.metamodel.Type result = (io.proleap.vb6.parser.metamodel.Type) getSemanticGraphElement(
				ctx);

		if (result == null) {
			final String name = determineName(ctx);
			result = new TypeImpl(name, this, ctx);

			for (final TypeStmt_ElementContext typeElementCtx : ctx.typeStmt_Element()) {
				final TypeElement typeElement = addTypeElement(typeElementCtx);
				result.addTypeElement(typeElement);
			}

			storeScopedElement(result);
			types.put(name, result);
		}

		return result;
	}

	@Override
	public TypeElement addTypeElement(final TypeStmt_ElementContext ctx) {
		TypeElement result = (TypeElement) getSemanticGraphElement(ctx);

		if (result == null) {
			final String name = determineName(ctx);
			final Type type = determineType(ctx);

			result = new TypeElementImpl(name, type, module, ctx);

			final boolean isArray = ctx.LPAREN() != null && ctx.RPAREN() != null;
			final boolean isStaticArray = isArray && ctx.subscripts() != null;

			result.setDeclaredAsArray(isArray);
			result.setDeclaredAsStaticArray(isStaticArray);

			storeScopedElement(result);
		}

		return result;
	}

	@Override
	public ModuleContext getCtx() {
		return ctx;
	}

	@Override
	public List<DefType> getDefTypes() {
		return defTypes;
	}

	@Override
	public EnumerationConstant getEnumerationConstant(final String name) {
		for (final Enumeration enumeration : enumerations.values()) {
			if (enumeration.getEnumerationConstant(name) != null) {
				return enumeration.getEnumerationConstant(name);
			}
		}

		return null;
	}

	@Override
	public Map<String, Enumeration> getEnumerations() {
		return enumerations;
	}

	@Override
	public Function getFunction(final String name) {
		return functions.get(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Program getProgram() {
		return program;
	}

	@Override
	public PropertyGet getPropertyGet(final String name) {
		return propertyGets.get(name);
	}

	@Override
	public PropertyLet getPropertyLet(final String name) {
		return propertyLets.get(name);
	}

	@Override
	public PropertySet getPropertySet(final String name) {
		return propertySets.get(name);
	}

	@Override
	public List<ScopedElement> getScopedElementsInScope(final String name) {
		final List<ScopedElement> result;

		final EnumerationConstant enumerationConstant = getEnumerationConstant(name);

		if (enumerationConstant != null) {
			result = new ArrayList<ScopedElement>();
			result.add(enumerationConstant);
		} else {
			result = super.getScopedElementsInScope(name);
		}

		return result;
	}

	@Override
	public Sub getSub(final String name) {
		return subs.get(name);
	}

	@Override
	public Double getVersion() {
		return version;
	}

	private void hasNewEnum(final String name) {
		if (NEW_ENUM.equals(name)) {
			isCollection = true;
		}
	}

	@Override
	public boolean isCollection() {
		return isCollection;
	}

	@Override
	public boolean isModuleWithMetaData() {
		return version != null || optionExplicit != null || optionPrivateModule != null || optionBase != null
				|| optionCompare != null;
	}

	@Override
	public String toString() {
		return name;
	}

}