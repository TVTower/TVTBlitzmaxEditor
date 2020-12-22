package org.tvtower;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.tvtower.parser.antlr.lexer.InternalBmxLexer;

//lexer overriding adapted from the code I wrote for Xturtle (https://github.com/AKSW/Xturtle/blob/develop/de.itemis.tooling.xturtle/src/de/itemis/tooling/xturtle/CustomXturtleLexer.java)
public class CustomBmxLexer extends InternalBmxLexer {

	LexerOverrider overrider = new LexerOverrider(CustomBmxLexer.class);

	public CustomBmxLexer() {
		super();
	}

	public CustomBmxLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}

	public CustomBmxLexer(CharStream input, RecognizerSharedState state) {
		super(input, state);

	}

	@Override
	public void mTokens() throws RecognitionException {
		if (overrider.override(input, state)) {
			// done
		} else {
			super.mTokens();
		}
	}
}
