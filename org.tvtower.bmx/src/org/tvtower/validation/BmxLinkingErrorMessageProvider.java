package org.tvtower.validation;

import org.eclipse.xtext.diagnostics.DiagnosticMessage;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider;

public class BmxLinkingErrorMessageProvider extends LinkingDiagnosticMessageProvider {

	//turn errors into warnings - linking is not perfect anyway
	@Override
	public DiagnosticMessage getUnresolvedProxyMessage(ILinkingDiagnosticContext context) {
		String linkText=context.getLinkText();
		Severity severity=Severity.WARNING;
		if(linkText!=null) {
			switch (linkText.toLowerCase()) {
			case "int":
			case "object":
			case "string":
			case "tstringbuilder":
			case "long":
			case "float":
			case "double":
			case "byte":
			case "tmap":
			case "tobjectmap":
			case "tstringmap":
			case "tintmap":
			case "tnodeenumerator":
			case "tnode":
			case "tlink":
			case "tlist":
			case "tobjectlist":
			case "short":
			case "tchannel":
			case "taudiosample":
			case "tsound":
			case "tbank":
			case "tstream":
			case "tthread":
			case "tmutex":
			case "timage":
			case "scolor8":
			case "srect":
			case "srecti":
			case "sspriteatlasrect":
			case "svec2i":
			case "svec2f":
			case "tpixmap":
				return null;
				//Ignore/Info not yet supported - override org.eclipse.xtext.linking.lazy.LazyLinkingResource.getDiagnosticList(DiagnosticMessage) line 426
//				severity=Severity.WARNING;
//				break;

			default:
				break;
			}
		}
		DiagnosticMessage m = super.getUnresolvedProxyMessage(context);
		return new DiagnosticMessage(m.getMessage(), severity, m.getIssueCode(), m.getIssueData());
	}
}
