import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.image.TextureLoader;

public class Practica11MendozaReyesAngelEmanuel extends JFrame {
    private SimpleUniverse universo;
    private BranchGroup escena;
    private TransformGroup objectoGiro;
    private Canvas3D canvas3d;

    public Practica11MendozaReyesAngelEmanuel() {
        setTitle("Practica 11 - Mendoza Reyes Angel Emanuel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Figuras 3D");
        JMenuItem cuboItem = new JMenuItem("Cubo");
        JMenuItem cilindroItem = new JMenuItem("Cilindro");

        cuboItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarOpcionesRotacion("Cubo");
            }
        });

        cilindroItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarOpcionesRotacion("Cilindro");
            }
        });

        menu.add(cuboItem);
        menu.add(cilindroItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas3d = new Canvas3D(config);
        setLayout(new BorderLayout());
        add(canvas3d, BorderLayout.CENTER);

        universo = new SimpleUniverse(canvas3d);
        universo.getViewingPlatform().setNominalViewingTransform();

        escena = crearGrafoEscena(null, null);
        escena.compile();
        universo.addBranchGraph(escena);
    }

    public void mostrarOpcionesRotacion(String tipoObjeto) {
        String[] opciones = {"Automática", "Mouse", "Teclado"};
        int seleccion = JOptionPane.showOptionDialog(this, "¿Qué tipo de rotación quieres realizar?", "Opciones de Rotación",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) {
            limpiarEscena();
            escena = crearGrafoEscena(tipoObjeto, "Automática");
            escena.compile();
            universo.addBranchGraph(escena);
        } else if (seleccion == 1) { 
            limpiarEscena();
            escena = crearGrafoEscena(tipoObjeto, "Mouse");
            escena.compile();
            universo.addBranchGraph(escena);
        } else if (seleccion == 2) { 
            limpiarEscena();
            escena = crearGrafoEscena(tipoObjeto, "Teclado");
            escena.compile();
            universo.addBranchGraph(escena);
        }
    }

    public BranchGroup crearGrafoEscena(String tipoObjeto, String tipoRotacion) {
        BranchGroup objectoRaiz = new BranchGroup();
        objectoRaiz.setCapability(BranchGroup.ALLOW_DETACH); 

        Background fondo = new Background();
        fondo.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        TextureLoader loader = new TextureLoader("fondo.jpg", this);
        ImageComponent2D imagen = loader.getImage();
        fondo.setImage(imagen);
        fondo.setImageScaleMode(Background.SCALE_FIT_ALL); 
        objectoRaiz.addChild(fondo);

        if (tipoObjeto != null) {
            objectoGiro = new TransformGroup();
            objectoGiro.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objectoRaiz.addChild(objectoGiro);

            Appearance apariencia = new Appearance();
            ColoringAttributes colorAtributos = new ColoringAttributes(new Color3f(Color.WHITE), ColoringAttributes.SHADE_FLAT);
            apariencia.setColoringAttributes(colorAtributos);

            if (tipoObjeto.equals("Cubo")) {
                objectoGiro.addChild(crearCuboConBordes(0.2f, apariencia));
            } else if (tipoObjeto.equals("Cilindro")) {
                Cylinder cilindro = new Cylinder(0.2f, 0.6f, apariencia);
                objectoGiro.addChild(cilindro);
            }

            if (tipoRotacion.equals("Automática")) {
                Alpha rotacionAlpha = new Alpha(-1, 4000);
                RotationInterpolator rotacion = new RotationInterpolator(rotacionAlpha, objectoGiro);
                rotacion.setSchedulingBounds(new BoundingSphere());
                objectoRaiz.addChild(rotacion);
            } else if (tipoRotacion.equals("Mouse")) {
                MouseRotate mouseRotate = new MouseRotate();
                mouseRotate.setTransformGroup(objectoGiro);
                mouseRotate.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000f));
                objectoRaiz.addChild(mouseRotate);
            } else if (tipoRotacion.equals("Teclado")) {
                KeyNavigatorBehavior knb = new KeyNavigatorBehavior(universo.getViewingPlatform().getViewPlatformTransform());
                knb.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
                objectoRaiz.addChild(knb);
            }
        }

        return objectoRaiz;
    }

    private Node crearCuboConBordes(float tamaño, Appearance apariencia) {
        TransformGroup tg = new TransformGroup();
        QuadArray cubo = new QuadArray(24, QuadArray.COORDINATES | QuadArray.COLOR_3);
        float[] coords = {
            -tamaño, -tamaño, tamaño,  tamaño, -tamaño, tamaño,
             tamaño,  tamaño, tamaño, -tamaño,  tamaño, tamaño,
            -tamaño, -tamaño, -tamaño, -tamaño,  tamaño, -tamaño,
             tamaño,  tamaño, -tamaño,  tamaño, -tamaño, -tamaño,
            -tamaño, -tamaño, -tamaño, -tamaño, -tamaño,  tamaño,
            -tamaño,  tamaño,  tamaño, -tamaño,  tamaño, -tamaño,
             tamaño, -tamaño, -tamaño,  tamaño,  tamaño, -tamaño,
             tamaño,  tamaño,  tamaño,  tamaño, -tamaño,  tamaño,
            -tamaño,  tamaño, -tamaño, -tamaño,  tamaño,  tamaño,
             tamaño,  tamaño,  tamaño,  tamaño,  tamaño, -tamaño,
            -tamaño, -tamaño, -tamaño,  tamaño, -tamaño, -tamaño,
             tamaño, -tamaño,  tamaño, -tamaño, -tamaño,  tamaño
        };
        cubo.setCoordinates(0, coords);

        Color3f[] colors = new Color3f[24];
        for (int i = 0; i < 24; i++) {
            colors[i] = new Color3f(Color.WHITE);
        }
        cubo.setColors(0, colors);

        Shape3D shapeCubo = new Shape3D(cubo, apariencia);
        tg.addChild(shapeCubo);

        LineArray lineas = new LineArray(24, LineArray.COORDINATES);
        float[] coordsLineas = {
            -tamaño, -tamaño, -tamaño,  tamaño, -tamaño, -tamaño,
             tamaño, -tamaño, -tamaño,  tamaño,  tamaño, -tamaño,
             tamaño,  tamaño, -tamaño, -tamaño,  tamaño, -tamaño,
            -tamaño,  tamaño, -tamaño, -tamaño, -tamaño, -tamaño,
            -tamaño, -tamaño,  tamaño,  tamaño, -tamaño,  tamaño,
             tamaño, -tamaño,  tamaño,  tamaño,  tamaño,  tamaño,
             tamaño,  tamaño,  tamaño, -tamaño,  tamaño,  tamaño,
            -tamaño,  tamaño,  tamaño, -tamaño, -tamaño,  tamaño,
            -tamaño, -tamaño, -tamaño, -tamaño, -tamaño,  tamaño,
             tamaño, -tamaño, -tamaño,  tamaño, -tamaño,  tamaño,
             tamaño,  tamaño, -tamaño,  tamaño,  tamaño,  tamaño,
            -tamaño,  tamaño, -tamaño, -tamaño,  tamaño,  tamaño
        };
        lineas.setCoordinates(0, coordsLineas);

        Appearance aparienciaLineas = new Appearance();
        ColoringAttributes colorAtributosLineas = new ColoringAttributes(new Color3f(Color.BLACK), ColoringAttributes.SHADE_FLAT);
        aparienciaLineas.setColoringAttributes(colorAtributosLineas);
        LineAttributes lineAttributes = new LineAttributes();
        lineAttributes.setLineWidth(2.0f);
        aparienciaLineas.setLineAttributes(lineAttributes);

        Shape3D shapeLineas = new Shape3D(lineas, aparienciaLineas);
        tg.addChild(shapeLineas);

        return tg;
    }

    public void limpiarEscena() {
        if (escena != null) {
            escena.detach();
        }
    }

    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        SwingUtilities.invokeLater(() -> {
            Practica11MendozaReyesAngelEmanuel frame = new Practica11MendozaReyesAngelEmanuel();
            frame.setVisible(true);
        });
    }
}