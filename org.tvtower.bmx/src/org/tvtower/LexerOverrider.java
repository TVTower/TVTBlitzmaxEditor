package org.tvtower;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognizerSharedState;

public class LexerOverrider {

	private static final int CHANNEL = BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
	private int compilerOptionRule=CustomBmxLexer.RULE_COMPILER_OPTION;
	private int exitRule=CustomBmxLexer.RULE_EXIT;

	public boolean override(CharStream input, RecognizerSharedState state) {
		return overrideCompilerOptionForFirstAPI(input, state) || overrideEnd(input, state);
	}

	//try to handle the case that a compiler option exchanges only the api declaration of a function or method
	//i.e. signature but no "end function"
	//however, one signature must remain (last one ending only with '?'
	private boolean overrideCompilerOptionForFirstAPI(CharStream input, RecognizerSharedState state) {
		boolean notLastOption=input.LA(1)=='?' && !isNewLine(input.LA(2));
		if(notLastOption) {
			int currentIndex = input.index();
			int la=2;
			int current=0;
			int stopIndex=currentIndex;
			//find end of option
			do {
				current=input.LA(la++);
				stopIndex++;
			} while (current!='?' && current!=-1);
			int lastLookahead=input.LA(la);
			notLastOption=lastLookahead != -1 && !isNewLine(lastLookahead);
			if(notLastOption) {
				String segment = input.substring(currentIndex, stopIndex);
				if(isSignature(segment)) {
					//if a signature was recognized consume the segment - overriding the regular
					//compiler option grammar definition
					while(la-- > 2) {
						input.consume();
					}
//					System.out.println("*"+String.valueOf((char)input.LA(1))+"*");
					stateOK(state, compilerOptionRule);
					return true;
				}
			}
		}
		return false;
	}

	private void stateOK(RecognizerSharedState state, int rule) {
		state.type = rule;
		state.channel = CHANNEL;
		state.failed = false;
		state.backtracking = 0;
	}

	private boolean isNewLine(int lookAheadCharacter) {
		return lookAheadCharacter=='\r'||lookAheadCharacter=='\n';
	}

	private boolean isSignature(String segment) {
		String lower = segment.toLowerCase();
		if(lower.matches("\\?(\\w+\\s+)+(\\r?\\n)end \\w+(\r?\n)\\?")) {
			//special case in gtkcommon.bmx - highlighting still inconsistent
			//end statement of signature
			return true;
		}
		if(lower.matches("\send")) {
			return false;
		}
		int secondLineStart = lower.indexOf('\n');
		if(secondLineStart>0) {
			String secondLine = lower.substring(secondLineStart).trim();
//			System.out.println(secondLine);
			return secondLine.startsWith("function ") ||secondLine.startsWith("method ")||secondLine.startsWith("struct ")||secondLine.startsWith("type ");
		}
		return false;
	}

	//exit-end may be followed by white space or comment...
	private boolean overrideEnd(CharStream input, RecognizerSharedState state) {
		if((input.LA(1)=='E'||input.LA(1)=='e')
				&&
				(input.LA(2)=='N'||input.LA(2)=='n')
				&&
				(input.LA(3)=='D'||input.LA(3)=='d')
				) {
			int index=4;
			boolean stop=false;
			do {
				int lookaheadChar=input.LA(index);
				if(lookaheadChar==' '||lookaheadChar=='\t') {
					index++;
				}else {
					stop=true;
				}
			}while(!stop);
			int charToCheck=input.LA(index);
			//next non-space character after end must be new line, file end or comment
			if(isNewLine(charToCheck)||charToCheck==-1||charToCheck=='\'') {
				input.consume();
				input.consume();
				input.consume();
				stateOK(state, exitRule);
				return true;
			}
		}
		return false;
	}
}
