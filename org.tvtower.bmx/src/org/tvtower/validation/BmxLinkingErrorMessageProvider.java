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
			case "short":
			case "byte":
			case "int":
			case "uint":
			case "int128":
			case "long":
			case "ulong":
			case "float":
			case "float64":
			case "float128":
			case "double":
			case "double128":
			case "object":
			case "size_t":
			case "string":
			case "lparam":
			case "wparam":
				return null;
				//Ignore/Info not yet supported - override org.eclipse.xtext.linking.lazy.LazyLinkingResource.getDiagnosticList(DiagnosticMessage) line 426
//				severity=Severity.WARNING;
//				break;

			default:
				if(linkText.length()==1) {
					//probably generics
					return null;
				}
				break;
			}
		}
		DiagnosticMessage m = super.getUnresolvedProxyMessage(context);
		return new DiagnosticMessage(m.getMessage(), severity, m.getIssueCode(), m.getIssueData());
	}
}
