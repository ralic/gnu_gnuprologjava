/* GNU Prolog for Java
 * Copyright (C) 1997-1999  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text of license can be also found
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package gnu.prolog.vm.interpreter.instruction;

import gnu.prolog.term.JavaObjectTerm;
import gnu.prolog.vm.BacktrackInfo;
import gnu.prolog.vm.PrologException;
import gnu.prolog.vm.interpreter.ExecutionState;
import gnu.prolog.vm.interpreter.ExecutionState.EXRC;

/** cut instruction */
public class ICut extends Instruction
{
	/**
	 * index in environment where cut position is kept, cut position is kept as
	 * JavaObjectTerm containing BacktrackInfo until which all should be be popped
	 */
	public int environmentIndex;

	/**
	 * a constructor
	 * 
	 * @param environmentIndex
	 */
	public ICut(int environmentIndex)
	{
		this.environmentIndex = environmentIndex;
	}

	/** convert instruction to string */
	@Override
	public String toString()
	{
		return codePosition + ": cut " + environmentIndex;
	}

	/**
	 * execute call instruction within specified sate
	 * 
	 * @param state
	 *          state within which instruction will be executed
	 * @return instruction to caller how to execute next instruction
	 * @throws PrologException
	 *           if code is throwing prolog exception
	 */
	@Override
	public EXRC execute(ExecutionState state, BacktrackInfo bi) throws PrologException
	{
		JavaObjectTerm term = (JavaObjectTerm) state.getEnvironment(environmentIndex);
		BacktrackInfo cutparent = (BacktrackInfo) term.value;
		while (state.peekBacktrackInfo() != cutparent)
		{
			state.popBacktrackInfo();
		}
		return ExecutionState.EXRC.NEXT;
	}
}
