// package com.memorynotfound.image;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.IOException;

public class GifWriter {

    protected ImageWriter imgWriter;
    protected ImageWriteParam imgParams;
    protected IIOMetadata metadata;

    public GifWriter(ImageOutputStream out, int imageType) throws IOException {
        imgWriter = ImageIO.getImageWritersBySuffix("gif").next();
        imgParams = imgWriter.getDefaultWriteParam();

        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
        metadata = imgWriter.getDefaultImageMetadata(imageTypeSpecifier, imgParams);

        configureRootMetadata();

        imgWriter.setOutput(out);
        imgWriter.prepareWriteSequence(null);
    }

    private void configureRootMetadata() throws IIOInvalidTreeException {
        String metaFormatName = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        // graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        // graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        // graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(1000 / 10));
        // graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");

        child.setUserObject(new byte[]{ 0x1, (byte) (0xFF), (byte) (0xFF)});
        appExtensionsNode.appendChild(child);
        metadata.setFromTree(metaFormatName, root);
    }

    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++){
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }

    public void writeToSequence(RenderedImage img) throws IOException {
        imgWriter.writeToSequence(new IIOImage(img, null, metadata), imgParams);
    }

    public void close() throws IOException {
        imgWriter.endWriteSequence();
    }

}