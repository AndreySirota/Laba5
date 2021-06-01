package bsu.rfe.java.group8.lab5.Sirota;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {
    private ArrayList<Double[]> graphicsData;
    private ArrayList<Double[]> originalData;
    private int selectedMarker = -1;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scaleX;
    private double scaleY;

    private double[][] viewport = new double[2][2];
    private ArrayList<double[][]> undoHistory = new ArrayList(5);
    private boolean showAxis = true;
    private boolean showMarkers = true;

    private Font axisFont;
    private Font labelsFont;

    private BasicStroke axisStroke;
    private BasicStroke graphicsStroke;
    private BasicStroke markerStroke;
    private BasicStroke selectionStroke;
    private static DecimalFormat formatter=(DecimalFormat)NumberFormat.getInstance();

    private boolean ShowAxis = true;
    private boolean ShowMarkers = true;
    private boolean scaleMode = false;
    private boolean changeMode = false;
    private double[] originalPoint = new double[2];
    private Rectangle2D.Double selectionRect = new Rectangle2D.Double();

    public GraphicsDisplay() {
        this.setBackground(Color.white);
        graphicsStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] {30, 10, 20, 10, 10, 10, 20, 10}, 0.0f);
        this.axisStroke = new BasicStroke(2.0F, 0, 0, 10.0F, (float[])null, 0.0F);
        this.markerStroke = new BasicStroke(4.0F, 0, 0, 10.0F, (float[])null, 0.0F);
        this.selectionStroke = new BasicStroke(1.0F, 0, 0, 10.0F, new float[]{10.0F, 10.0F}, 0.0F);
        this.axisFont = new Font("Serif", 1, 36);
        this.labelsFont = new Font("Serif", 0, 20);
        formatter.setMaximumFractionDigits(5);
        this.addMouseListener(new GraphicsDisplay.MouseHandler());
        this.addMouseMotionListener(new GraphicsDisplay.MouseMotionHandler());
    }
    public void showGraphics(ArrayList<Double[]> graphicsData)	{
        this.graphicsData = graphicsData;
        this.originalData = new ArrayList(graphicsData.size());
        for (Double[] point : graphicsData) {
            Double[] newPoint = new Double[2];
            newPoint[0] = new Double(point[0].doubleValue());
            newPoint[1] = new Double(point[1].doubleValue());
            this.originalData.add(newPoint);
        }
        this.minX = ((Double[])graphicsData.get(0))[0].doubleValue();
        this.maxX = ((Double[])graphicsData.get(graphicsData.size() - 1))[0].doubleValue();
        this.minY = ((Double[])graphicsData.get(0))[1].doubleValue();
        this.maxY = this.minY;

        for (int i = 1; i < graphicsData.size(); i++) {
            if (((Double[])graphicsData.get(i))[1].doubleValue() < this.minY) {
                this.minY = ((Double[])graphicsData.get(i))[1].doubleValue();
            }
            if (((Double[])graphicsData.get(i))[1].doubleValue() > this.maxY) {
                this.maxY = ((Double[])graphicsData.get(i))[1].doubleValue();
            }
        }
        zoomToRegion(minX, maxY, maxX, minY);
    }