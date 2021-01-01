package org.tvtower.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper;
import org.eclipse.xtext.ui.editor.hyperlinking.XtextHyperlink;
import org.tvtower.bmx.ImportStatement;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

public class BmxHyperlinkHelper extends HyperlinkHelper {

	@Inject
	private Provider<XtextHyperlink> hyperlinkProvider;
	
	@Override
	public IHyperlink[] createHyperlinksByOffset(XtextResource resource, int offset, boolean createMultipleHyperlinks) {
		List<IHyperlink> hyperlinks = new ArrayList<IHyperlink>();
		// Get default hyperlinks
		IHyperlink[] defaultHyperlinks = super.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks);
		if (defaultHyperlinks != null) {
			hyperlinks.addAll(Arrays.asList(defaultHyperlinks));
		}
		ILeafNode node = NodeModelUtils.findLeafNodeAtOffset(resource.getParseResult().getRootNode(), offset);
		if (node != null) {
			EObject object = NodeModelUtils.findActualSemanticObjectFor(node);
			if(object instanceof ImportStatement) {
				String importUri = ((ImportStatement) object).getImportURI();
				if(!Strings.isNullOrEmpty(importUri)) {
					int nodeOffset = node.getOffset();
					int nodeLength = node.getLength();
					URI uriToOpen= URI.createURI(importUri).resolve(resource.getURI());
					XtextHyperlink hyperlink = hyperlinkProvider.get();
					hyperlink.setHyperlinkRegion(new Region(nodeOffset, nodeLength));
					hyperlink.setHyperlinkText("Open imported file");
					hyperlink.setURI(uriToOpen);
					hyperlinks.add(hyperlink);
				}
			}
		}

		if (hyperlinks.isEmpty()) { // Must return null instead of empty array if there are no hyperlinks
			return null;
		} else {
			return Iterables.toArray(hyperlinks, IHyperlink.class);
		}
	}
}
