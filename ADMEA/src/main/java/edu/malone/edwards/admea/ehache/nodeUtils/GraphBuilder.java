package edu.malone.edwards.admea.ehache.nodeUtils;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.malone.edwards.admea.nodeUtils;
//
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.geom.AffineTransform;
//import java.awt.image.BufferedImage;
//import java.awt.image.IndexColorModel;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Random;
//import javax.imageio.ImageIO;
//import javax.imageio.stream.FileImageOutputStream;
//import javax.imageio.stream.ImageOutputStream;
//import edu.malone.edwards.admea.utils.GifSequenceWriter;
//import org.gephi.graph.api.DirectedGraph;
//import org.gephi.graph.api.Edge;
//import org.gephi.graph.api.GraphController;
//import org.gephi.graph.api.GraphModel;
//import org.gephi.graph.api.Node;
//import org.gephi.io.exporter.api.ExportController;
//import org.gephi.io.exporter.preview.PNGExporter;
//import org.gephi.io.exporter.spi.GraphExporter;
//import org.gephi.preview.api.PreviewController;
//import org.gephi.preview.api.PreviewModel;
//import org.gephi.preview.api.PreviewProperty;
//import org.gephi.preview.types.EdgeColor;
//import org.gephi.project.api.ProjectController;
//import org.gephi.project.api.Workspace;
//import org.openide.util.Exceptions;
//import org.openide.util.Lookup;
//
///**
// *
// * @author cjedwards1
// */
//public class GraphBuilder
//{
//    private final ProjectController pc;
//    private Workspace workspace;
//    
//    private GraphBuilder()
//    {
//        pc = Lookup.getDefault().lookup(ProjectController.class);
//    }
//    
//    public static GraphBuilder getInstance()
//    {
//        return GraphBuilderHolder.INSTANCE;
//    }
//    
//    private static class GraphBuilderHolder
//    {
//        private static final GraphBuilder INSTANCE = new GraphBuilder();
//    }
//    
//    public synchronized void build(String filePath) throws Exception
//    {
//        pc.newProject();
//        workspace = pc.getCurrentWorkspace();
//        
//        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
//        PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
//
//        if(!graphModel.getNodeTable().hasColumn("probability"))
//        {
//            graphModel.getNodeTable().addColumn("probability", Double.class);
//            graphModel.getNodeTable().addColumn("n", Integer.class);
//            graphModel.getNodeTable().addColumn("k", Integer.class);
//            graphModel.getNodeTable().addColumn("score", Integer.class);
//        }
//
//        buildNodes(graphModel);
//
//        //Layout for X seconds
////        System.out.println("Layout time: " + ((directedGraph.getNodes().toArray().length / 100) * 3));
////        AutoLayout autoLayout = new AutoLayout((directedGraph.getNodes().toArray().length / 100) * 3, TimeUnit.SECONDS);
////        autoLayout.setGraphModel(graphModel);
////
////        ForceAtlas2 force = new ForceAtlas2(null);
////        force.setThreadsCount(1);
////        force.setScalingRatio(20.0);
////        force.setGravity(0.5);
////
////        NoverlapLayout noOverlap = new NoverlapLayout(null);
////        noOverlap.setMargin(40.0);
////
////        autoLayout.addLayout(force, 0.7f);
////        autoLayout.addLayout(noOverlap, 0.3f);
////        System.out.println("Layout started");
////        autoLayout.execute();
////        System.out.println("Layout finished.");
//
//        //Preview
//        model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
//        model.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.TRUE);
//         model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
//        model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, 0.3f);
//        model.getProperties().putValue(PreviewProperty.NODE_BORDER_WIDTH, 0.2f);
//        model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));
//        model.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, true);
//        model.getProperties().putValue(PreviewProperty.VISIBILITY_RATIO, 0.5f);
//
//        // export gefx
//        if(filePath != null && !filePath.equals("")) 
//        {
//            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HHmm");
//            ExportController ec = Lookup.getDefault().lookup(ExportController.class);
//            PNGExporter exporter = (PNGExporter) ec.getExporter("png");
//            exporter.setHeight(3072);
//            exporter.setWidth(3072);
//            exporter.setWorkspace(workspace);
//            ec.exportFile(new File(filePath + "\\" + sdf.format(new Date(System.currentTimeMillis())) + ".png"), exporter);
//            
//            // get GEXF Exporter
//            GraphExporter gexfExporter = (GraphExporter) ec.getExporter("gexf");
//            // Only exports the visible (filtered) graph
////            gexfExporter.setExportVisible(true);
//            gexfExporter.setWorkspace(workspace);
//            ec.exportFile(new File(filePath + "\\current.gexf"), gexfExporter);
//        }
//    }
//    
//    public void buildGif(String[] files)
//    {   
//        try 
//        {   
//            // grab the output image type from the first image in the sequence
//            BufferedImage firstImage = ImageIO.read(new File(files[0]));
//            firstImage = makeCompatible(firstImage);
//            
//            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
//            
//            // create a gif sequence with the type of the first image, 1 second
//            // between frames, which loops continuously
//            File gifFile = new File("C:\\Server Files\\WestNileSpread\\" + sdf.format(new Date(System.currentTimeMillis())) + ".gif");
//            try (ImageOutputStream output = new FileImageOutputStream(gifFile))
//            {
//                // create a gif sequence with the type of the first image, 1 second
//                // between frames, which loops continuously
//                GifSequenceWriter writer =
//                        new GifSequenceWriter(output, firstImage.getType(), 2000, true);
//                
//                // write out the first image to our sequence...
//                writer.writeToSequence(firstImage);
//                for(int i = 1; i < files.length; i++)
//                {
//                    BufferedImage nextImage = ImageIO.read(new File(files[i]));
//                    nextImage = makeCompatible(nextImage);
//                    writer.writeToSequence(nextImage);
//                }
//                
//                writer.close();
//            }
//            catch(Exception ex)
//            {
//                ex.printStackTrace();
//            }
//        }
//        catch(IOException ex)
//        {
//            Exceptions.printStackTrace(ex);
//        }
//    }
//    
//    private void buildNodes(GraphModel graphModel)
//    {
//        edu.malone.edwards.admea.nodeUtils.Node[] nodes = NODE_LIST.values().toArray(new edu.malone.edwards.admea.nodeUtils.Node[0]);
//        DirectedGraph directedGraph = graphModel.getDirectedGraph();
//        Random random = new Random();
//        
//        for(edu.malone.edwards.admea.nodeUtils.Node node : nodes)
//        {
//            Node graphNode = graphModel.factory().newNode("n" + node.getNodeId());
//            graphNode.setLabel("n" + node.getNodeId());
//            graphNode.setAttribute("probability", node.calculateProbability());
//            graphNode.setAttribute("n", node.getN());
//            graphNode.setAttribute("k", node.getK());
//            graphNode.setAttribute("score", node.getScore());
//            graphNode.setX(random.nextFloat()* 1000);
//            graphNode.setY(random.nextFloat() * 1000);
//            graphNode.setSize(node.getScore());
//            directedGraph.addNode(graphNode);
//        }
//        
//        HashMap<Node, Node> connection = new HashMap();
//        
//        for(Node node : directedGraph.getNodes().toArray())
//        {
//            for(long child : Nodes.getNode(Long.parseLong(node.getId().toString().substring(1))).children)
//            {
//                Node node2 = directedGraph.getNode("n" + child);
//                if(connection.get(node) == node2 || connection.get(node2) != node)
//                    break;
//                
//                Edge edge = graphModel.factory().newEdge(node, node2);
//                edge.setWeight(1.0);
//                directedGraph.addEdge(edge); 
//                connection.put(node, node2);
//            }
//        }
//    }
//    
//    private BufferedImage makeCompatible(BufferedImage image) 
//    {
//        int w = image.getWidth();
//        int h = image.getHeight();
//        int[] colors = new int[]{0, 95, 135, 175, 215, 255};
//        
//        //Colors length to the power of 3 for each color in rgb.
//        byte[] red = new byte[colors.length * colors.length * colors.length];
//        byte[] green = new byte[colors.length * colors.length * colors.length];
//        byte[] blue = new byte[colors.length * colors.length * colors.length];
//        
//        int numColors = 0;
//        for(int r : colors)
//        {
//            for(int g : colors)
//            {
//                for(int b : colors)
//                {
//                    Color temp = new Color(r, g, b);
//                    red[numColors] = (byte) temp.getRed();
//                    green[numColors] = (byte) temp.getGreen();
//                    blue[numColors] = (byte) temp.getBlue();
//                    numColors++;
////                    System.out.println(numColors);
//                }
//            }
//        }
//
//        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(8, numColors,
//                red,
//                green,
//                blue,
//                0));
//        Graphics2D g = result.createGraphics();
//        g.drawRenderedImage(image, new AffineTransform()); //or some other drawImage function
//        g.dispose();
//
//        return result;
//    }   
//}
