package hclustering;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Digraph extends JPanel implements MouseMotionListener, MouseListener, ActionListener
{

    private static final int HEIGHT = 800;
    private static final int WIDTH = 1200;
    public static double userInput;
    private int barb; //size of an arrow edge
    private double phi; //angle of an arrow edge
    private Integer moveVertex; //the Vertex the user is moving on the GUI
    private Integer selectedVertex; // the Vertex selected
    public final static int CIRCLEDIAMETER = 20; //Diameter of the Vertexs
    // This is the adjacency list representation of the digraph
    // The Vertexs are denoted here as Integers
    // Each Vertex is associated with a list of Integers, which indicates its out-neighbours
    private HashMap<Integer, List<Integer>> data;
    //
    private HashMap<Integer, Vertex> VertexList;
    // The collection of Vertex in the graph
    // This set is the key set of data
    private Set<Integer> VertexSet;
    // The textfield used for user to specify commands
    private JTextField tf;
    
    
    private LoadGML gml;
    private List<Community> communityList;
    private boolean isClustered = false;
    private final Color[] COLOR_ARRAY =
    {
        Color.green,
        Color.pink,
        Color.red,
        Color.cyan,
        Color.yellow,
        Color.magenta,
        Color.orange,
        new Color(127, 0, 127)
    };

    // The Constructor
    public Digraph()
    {


        data = new HashMap<Integer, List<Integer>>();
        VertexList = new HashMap<Integer, Vertex>();
        VertexSet = data.keySet();

        JPanel panel = new JPanel();
        barb = 10;                   // barb length
        phi = Math.PI / 12;             // 30 degrees barb angle
        setBackground(Color.white);
        addMouseMotionListener(this);
        addMouseListener(this);
        tf = new JTextField();
        tf.addActionListener(this);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(tf, BorderLayout.SOUTH);
        moveVertex = -1; 				// Initial values of moveVertex is -1
        selectedVertex = -1;			// Initial values of moveVertex is -1

    }

    /**
     * The method loads a digraph stored in the file fileName in adjacency list
     * representation The top line of the file contains the number n of Vertexs
     * in the graph The Vertexs in the graph are then given the indices
     * 0,...,n-1
     *
     */
    public void load(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            int numVertexs = Integer.parseInt(br.readLine());
            int pos = 0;
            String output;
            for (int i = 0; i < numVertexs; i++)
            {
                add(i);
            }

            for (int i = 0; i < numVertexs; i++)
            {
                output = br.readLine();
                if (output != null)
                {
                    StringTokenizer token = new StringTokenizer(output);
                    while (token.hasMoreTokens())
                    {
                        addEdge(i, Integer.parseInt(token.nextToken()));
                    }
                }
            }
            br.close();

        }
        catch (FileNotFoundException e)
        {
            System.out.println("File can't be found.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * The method adds a Vertex to the digraph, labeled by the int value Vertex
     *
     */
    public void add(int Vertex)
    {
        // If the label Vertex is already in the digraph, do nothing and return
        if (data.containsKey((Integer) Vertex))
        {
            return;
        }
        // Create a new linked list
        List<Integer> list = new LinkedList<Integer>();
        // Add a new entry to the adjacency list
        data.put((Integer) Vertex, list);

        // Create a new Vertex in the GUI
        // Set a random initial position
        // Link the new Vertex with the corresponding Vertex in the GUI
        Vertex VertexVisual = new Vertex(Vertex);
//      VertexVisual.xpos = 150 +(int) (Math.random() * 520);
//      VertexVisual.ypos = 150 +(int) (Math.random() * 520);
        int i = Vertex % 10;
        int j = Vertex / 10;
        int s = 80;
        VertexVisual.xpos = 50 + i * s + (int) (Math.random() * 20);
        VertexVisual.ypos = 50 + j * s + (int) (Math.random() * 20);
        VertexList.put((Integer) Vertex, VertexVisual);

    }

    /**
     * The method adds an edge to the digraph The source of the edge is labeled
     * Vertex1 The target of the edge is labeled Vertex2
     *
     */
    public void addEdge(int Vertex1, int Vertex2)
    {
        if (Vertex1 == Vertex2)
        {
            return;
        }
        if (!data.containsKey((Integer) Vertex1) || !data.containsKey((Integer) Vertex2))
        {
            return;
        }
        List<Integer> list = data.get((Integer) Vertex1);
        if (!list.contains((Integer) Vertex2))
        {
            list.add((Integer) Vertex2);
        }
    }

    public void clear()
    {
        data.clear();
        VertexList.clear();
    }

    public void actionPerformed(ActionEvent evt)
    {
        String command = tf.getText();

        StringTokenizer st = new StringTokenizer(command);

        String token, opt;
        Integer Vertex1, Vertex2;
        if (st.hasMoreTokens())
        {
            token = st.nextToken();
            token = token.toLowerCase();
            switch (token)
            {
                case "load":
                    try
                    {
                        clear();
                        opt = st.nextToken();
                        load(opt);
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid command");
                    }
                    break;

                case "clear":
                    if (!st.hasMoreTokens())
                    {
                        clear();
                    }
                    else
                    {
                        System.out.println("Invalid command");
                    }
                    break;

                case "identity":

                    opt = st.nextToken();
                    double input = Double.parseDouble(opt);
                    if (input <= 1.0 || input > 0.0)
                    {
                        System.out.println("Running clustering method...");
                        clustering(input);
                        //System.out.println("Forest: " + hc.getComs());
                    }
                    else
                    {
                        System.out.println("Invalid command 111");
                    }
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }

        }
        repaint();

    }

    /**
     * Paint the digraph to the panel
     *
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //clear the previous screen
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 1500, 1500);
        g2d.setColor(Color.BLACK);

        List<Integer> outNeighbours;

        for (Integer i : VertexSet)
        {
            if (selectedVertex == i)
            {
                VertexList.get(i).draw(g, Color.RED);
            }
            else
            {
                VertexList.get(i).draw(g, Color.BLACK);
            }
            outNeighbours = data.get(i);
            for (int j = 0; j < outNeighbours.size(); j++)
            {
                drawEdge(i, outNeighbours.get(j), g, Color.lightGray, Color.BLUE);
            }
        }

        if (isClustered)
        {
            int count = 0;
            for (Community c : communityList)
            {
                HashSet<Integer> cSet = new HashSet<Integer>();
                for (Node n : c.getMembers())
                {
                    cSet.add(n.getID());
                }
                for (Node n : c.getMembers())
                {
                    Color color = COLOR_ARRAY[count % (COLOR_ARRAY.length)];
                    VertexList.get(n.getID()).draw(g, color);
                    for (Integer e : n.getEdges())
                    {
                        if (cSet.contains(e))
                        {
                            drawEdge(n.getID(), e, g, color, Color.BLUE);
                        }
                    }
                }
                count++;
            }
        }

    }

    /**
     * Draw a directed edge between 2 Vertexs with the specific color for the
     * line and the arrow.
     */
    public void drawEdge(Integer Vertex1, Integer Vertex2, Graphics g, Color colorLine, Color colorArrow)
    {

        Graphics2D g2 = (Graphics2D) g;

        int startX = VertexList.get(Vertex1).getEdgeX(VertexList.get(Vertex2));
        int startY = VertexList.get(Vertex1).getEdgeY(VertexList.get(Vertex2));

        int destX = VertexList.get(Vertex2).getEdgeX(VertexList.get(Vertex1));
        int destY = VertexList.get(Vertex2).getEdgeY(VertexList.get(Vertex1));

        g2.setStroke(new BasicStroke(1));
        g2.setColor(colorLine);

        g2.drawLine(startX, startY, destX, destY);

        double theta, x, y;
        g2.setPaint(colorArrow);
        theta = Math.atan2(destY - startY, destX - startX);
        drawArrow(g2, theta, destX, destY);

    }

    //draws the arrows on the edges
    private void drawArrow(Graphics2D g2, double theta, double x0, double y0)
    {
        g2.setStroke(new BasicStroke(1));
        double x = x0 - barb * Math.cos(theta + phi);
        double y = y0 - barb * Math.sin(theta + phi);
        g2.draw(new Line2D.Double(x0, y0, x, y));
        x = x0 - barb * Math.cos(theta - phi);
        y = y0 - barb * Math.sin(theta - phi);
        g2.draw(new Line2D.Double(x0, y0, x, y));
    }

    // Mouse Actions:
    // Moving a Vertex: The user may drag and drop a Vertex to any position
    // Add a Vertex: The user may add a Vertex by clicking a white area of the frame
    //				The newly added Vertex will be automatically selected
    // Selecting a Vertex: The user may select a Vertex by click on a Vertex
    // Add an edge: Once a Vertex is selected, the user may add an outgoing edge
    //				to the selected Vertex by clicking another Vertex
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (moveVertex >= 0)
        {
            Vertex Vertex = VertexList.get(moveVertex);
            Vertex.xpos = e.getPoint().x;
            Vertex.ypos = e.getPoint().y;
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Vertex Vertex;
        boolean onVertex = false;
        Integer clicked = -1;
        for (Integer i : VertexSet)
        {
            Vertex = VertexList.get(i);
            //Calculate the distance to the center of a Vertex
            double distance = Math.sqrt(Math.pow((e.getX() - Vertex.xpos), 2) + Math.pow((e.getY() - Vertex.ypos), 2));
            if (distance <= (1.0 * CIRCLEDIAMETER / 2))
            {
                onVertex = true;
                clicked = i;
            }
        }
        if (onVertex)
        {
            if (selectedVertex == -1)
            {
                selectedVertex = clicked;
            }
            else
            {
                if (clicked.equals(selectedVertex))
                {
                    selectedVertex = -1;
                }
                else
                {
                    addEdge(selectedVertex, clicked);
                    selectedVertex = -1;
                }
            }
        }
        if (!onVertex)
        {
            int newVertex = 0;
            while (VertexSet.contains((Integer) newVertex))
            {
                newVertex++;
            }
            add((Integer) newVertex);
            selectedVertex = newVertex;
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Vertex Vertex;
        for (Integer i : VertexSet)
        {
            Vertex = VertexList.get(i);
            //Calculate the distance to the center of a Vertex
            double distance = Math.sqrt(Math.pow((e.getX() - Vertex.xpos), 2) + Math.pow((e.getY() - Vertex.ypos), 2));
            if (distance <= (1.0 * CIRCLEDIAMETER / 2))
            {
                moveVertex = i;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        moveVertex = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    // An inner class storing information regarding the visualisation of a Vertex
    private class Vertex
    {

        public int xpos;
        public int ypos;
        public int VertexNum;
        public int inEdges, arraySpot;
        int dirX, dirY;

        public Vertex(int num)
        {
            xpos = 0;
            ypos = 0;
            VertexNum = num;
            inEdges = 0;
            arraySpot = 0;
            int c = 2;
            double rand = Math.random();

            if (rand < 0.25)
            {
                dirX = c;
                dirY = c;
            }
            else if (rand < 0.5)
            {
                dirX = -c;
                dirY = c;
            }
            else if (rand < 0.75)
            {
                dirX = c;
                dirY = -c;
            }
            else
            {
                dirX = -c;
                dirY = -c;
            }

        }

        // returns the label of the Vertex
        public int label()
        {
            return VertexNum;
        }

        // compute the x coordinate of the source of the edge from this to the specified Vertex
        public int getEdgeX(Vertex Vertex)
        {
            double direction = 1.0;
            if (Vertex.xpos < xpos)
            {
                direction = -1.0;
            }
            double x2subx1sqr = Math.pow((Vertex.xpos - xpos), 2);
            double y2suby1sqr = Math.pow((Vertex.ypos - ypos), 2);
            double rsqr = Math.pow(CIRCLEDIAMETER * 1.0 / 2, 2);
            double x = Math.sqrt((x2subx1sqr * rsqr / (x2subx1sqr + y2suby1sqr))) * direction + xpos;
            return (int) Math.round(x);
        }

        // compute the y coordinate of the source of the edge from this to the specified Vertex
        public int getEdgeY(Vertex Vertex)
        {
            double direction = 1.0;
            if (Vertex.ypos < ypos)
            {
                direction = -1.0;
            }
            double x2subx1sqr = Math.pow((Vertex.xpos - xpos), 2);
            double y2suby1sqr = Math.pow((Vertex.ypos - ypos), 2);
            double rsqr = Math.pow(CIRCLEDIAMETER * 1.0 / 2, 2);//Square root of radius
            double y = Math.sqrt((y2suby1sqr * rsqr / (x2subx1sqr + y2suby1sqr))) * direction + ypos;
            return (int) Math.round(y);
        }

        // draw the Vertex
        public void draw(Graphics g, Color color)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(color);
            g2d.drawOval(xpos - (CIRCLEDIAMETER / 2), ypos - (CIRCLEDIAMETER / 2), CIRCLEDIAMETER, CIRCLEDIAMETER);
            g2d.setColor(Color.BLUE);
            g2d.drawString("" + VertexNum, xpos - 7, ypos + 5);
            g2d.setColor(Color.BLACK);
        }

        public void move()
        {

            xpos = xpos - 2 * dirX;
            ypos = ypos - 2 * dirY;

            if (xpos < 50 || xpos > 360)
            {
                dirX = (-1) * dirX;
            }
            if (ypos < 50 || ypos > 360)
            {
                dirY = (-1) * dirY;
            }


        }
    }

    public void loadFromGML()
    {
        try
        {
            gml = new LoadGML();
            for (Node i : gml.getArray())
            {
                add(i.getID());
            }
            for (Node i : gml.getArray())
            {
                for (Integer n : i.getEdges())
                {
                    addEdge(i.getID(), n);
                }
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        repaint();
    }

    public void clustering(double userInput)
    {
        isClustered = true;
        HClustering hc = new HClustering(gml.getArray());
        QualityFunction qf = new DensityQualityFunction();

        communityList = hc.cluster(qf, userInput);
        int size = communityList.size();
        int column = (int) Math.sqrt(size) + 1;
        int row = size / column;
        int cc = 0;
        int hshift = WIDTH / column;
        int vshift = HEIGHT / (row + 1);

        for (Community c : communityList)
        {
            int cxshift = cc % column;
            int cyshift = cc / column;
            int counter = 0;
            int lshift = 60;
            for (Node n : c.getMembers())
            {
                int lxshift = counter % ((int) Math.sqrt(c.getMembers().size()) + 1);
                int lyshift = counter / ((int) Math.sqrt(c.getMembers().size()) + 1);
                VertexList.get(n.getID()).xpos = 20 + cxshift * hshift
                        + lxshift * lshift
                        + counter % 2 * 10;
                VertexList.get(n.getID()).ypos = 20 + cyshift * vshift
                        + lyshift * lshift
                        + (int) (Math.random() * 10)
                        + counter % 2 * 10
                        + cc % 2 * 50;
                counter++;
            }
            cc++;
        }
        System.out.println("size: " + communityList.size());
        repaint();

    }

    // The main method builds a digraph and add three initial Vertexs to the digraph labeled 0,1,2
    // and adds some edges among these Vertexs
    //
    public static void main(String[] args)
    {
        Digraph g = new Digraph();
        g.loadFromGML();


        JFrame frame = new JFrame("Directed Graph Implementation");
        frame.setSize(WIDTH,HEIGHT);
        frame.setSize(1366, 730);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.getContentPane().add(g);
        frame.setVisible(true);


    }
}