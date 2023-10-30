package com.ssafy.realrealfinal.imagems.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.IOException;

// TimelapseUtil 클래스를 선언. 이 클래스는 GIF 이미지를 생성하는 데 사용됨.
@RequiredArgsConstructor
@Component
public class TimelapseUtil {

    // 멤버 변수 선언. writer는 이미지를 쓰는 데 사용되고, params는 쓰기 매개 변수, metadata는 이미지 메타데이터를 포함.
    protected ImageWriter writer;
    protected ImageWriteParam params;
    protected IIOMetadata metadata;

    // 생성자. GIF 이미지의 설정을 초기화하는 데 사용됨.
    public TimelapseUtil(ImageOutputStream out, int imageType, int delay, boolean loop)
        throws IOException {
        // 이미지 작성자를 얻어 writer에 할당.
        writer = ImageIO.getImageWritersBySuffix("gif").next();
        // writer로부터 기본 쓰기 매개 변수를 얻어 params에 할당.
        params = writer.getDefaultWriteParam();

        // 이미지 유형에 따라 이미지 유형 지정자를 생성하고 메타데이터를 설정.
        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(
            imageType);
        metadata = writer.getDefaultImageMetadata(imageTypeSpecifier, params);

        // 메타데이터를 설정.
        configureRootMetadata(delay, loop);

        // writer에 출력 스트림 설정.
        writer.setOutput(out);
        // 쓰기 시퀀스 준비.
        writer.prepareWriteSequence(null);
    }

    // 메타데이터를 구성하는 메소드.
    private void configureRootMetadata(int delay, boolean loop) throws IIOInvalidTreeException {
        // 네이티브 메타데이터 형식 이름을 얻음.
        String metaFormatName = metadata.getNativeMetadataFormatName();
        // 메타데이터 트리를 얻어 root에 할당.
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);

        // 다양한 노드 설정.
        // 여기서는 GIF 이미지의 다양한 특성, 예를 들어, 딜레이 타임, 주석 등을 설정.
        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by: https://memorynotfound.com");

        IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");

        int loopContinuously = loop ? 0 : 1;
        child.setUserObject(new byte[]{0x1, (byte) (loopContinuously & 0xFF),
            (byte) ((loopContinuously >> 8) & 0xFF)});
        appExtensionsNode.appendChild(child);
        metadata.setFromTree(metaFormatName, root);
    }

    // 특정 노드를 얻거나 생성하는 메소드.
    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return (node);
    }

    // 이미지 시퀀스를 쓰는 메소드. 이미지와 관련된 메타데이터를 사용하여 이미지를 씀.
    public void writeToSequence(RenderedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), params);
    }

    // 쓰기 시퀀스를 종료하는 메소드. 모든 리소스를 정리하고 writer를 닫음.
    public void close() throws IOException {
        writer.endWriteSequence();
    }

}