package org.tvtower;

import java.lang.reflect.Field;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognizerSharedState;

public class LexerOverrider {

	private static final int CHANNEL = BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
	private int compilerOptionRule;

	public LexerOverrider(Class<CustomBmxLexer> lexer) {
		this.compilerOptionRule= getRuleIndex(lexer, "RULE_COMPILER_OPTION");
	}

	private int getRuleIndex(Class<?> lexerClass, String ruleName) {
		try {
			Field field = lexerClass.getField(ruleName);
			Object value = field.get(lexerClass);
			return (Integer) value;
		} catch (SecurityException e) {
			throw getException(ruleName, e);
		} catch (NoSuchFieldException e) {
			throw getException(ruleName, e);
		} catch (IllegalArgumentException e) {
			throw getException(ruleName, e);
		} catch (IllegalAccessException e) {
			throw getException(ruleName, e);
		}
	}

	private RuntimeException getException(String rule, Throwable cause) {
		throw new RuntimeException(
				"cannot determine the index of rule " + rule, cause);
	}

	public boolean override(CharStream input, RecognizerSharedState state) {
		return overrideCompilerOptionForFirstAPI(input, state);
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
//					System.out.println(segment);
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
		if(lower.contains("end")) {
			return false;
		}
		int secondLineStart = lower.indexOf('\n');
		if(secondLineStart>0) {
			String secondLine = lower.substring(secondLineStart).trim();
//			System.out.println(secondLine);
			return secondLine.startsWith("function ")||secondLine.startsWith("method ");
		}
		return false;
		
	}
}
