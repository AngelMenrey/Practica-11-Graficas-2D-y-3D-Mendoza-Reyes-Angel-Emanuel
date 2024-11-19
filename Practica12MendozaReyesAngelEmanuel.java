import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import javax.vecmath.AxisAngle4f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.image.TextureLoader;

public class Practica12MendozaReyesAngelEmanuel extends JFrame {
    private SimpleUniverse universo;
    private BranchGroup escena;
    private TransformGroup objectoGiro;
    private Canvas3D canvas3d;

    public Practica12MendozaReyesAngelEmanuel() {
        setTitle("Practica 12 - Mendoza Reyes Angel Emanuel");
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
            objectoGiro.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            objectoRaiz.addChild(objectoGiro);

            Appearance apariencia = new Appearance();
            Material material = new Material();
            material.setAmbientColor(new Color3f(Color.DARK_GRAY));
            material.setDiffuseColor(new Color3f(Color.WHITE));
            material.setSpecularColor(new Color3f(Color.WHITE));
            material.setShininess(20.0f);
            apariencia.setMaterial(material);

            TransformGroup tgMovimiento = new TransformGroup();
            tgMovimiento.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objectoGiro.addChild(tgMovimiento);

            if (tipoObjeto.equals("Cubo")) {
                tgMovimiento.addChild(crearCubo(0.2f, apariencia));
                Alpha alphaMovimiento = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 4000, 0, 0, 4000, 0, 0);
                Transform3D ejeMovimiento = new Transform3D();
                PositionInterpolator interpoladorMovimiento = new PositionInterpolator(alphaMovimiento, tgMovimiento, ejeMovimiento, -0.5f, 0.5f);
                interpoladorMovimiento.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
                objectoRaiz.addChild(interpoladorMovimiento);

                Alpha alphaRotacion = new Alpha(-1, 4000);
                RotationInterpolator rotacionAdicional = new RotationInterpolator(alphaRotacion, tgMovimiento);
                rotacionAdicional.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
                objectoRaiz.addChild(rotacionAdicional);
            } else if (tipoObjeto.equals("Cilindro")) {
                tgMovimiento.addChild(new Cylinder(0.2f, 0.6f, Cylinder.GENERATE_NORMALS, apariencia));
                Alpha alphaMovimiento = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 4000, 0, 0, 4000, 0, 0);
                Transform3D ejeMovimiento = new Transform3D();
                ejeMovimiento.rotX(Math.PI / 2);
                PositionInterpolator interpoladorMovimiento = new PositionInterpolator(alphaMovimiento, tgMovimiento, ejeMovimiento, -0.5f, 0.5f);
                interpoladorMovimiento.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
                objectoRaiz.addChild(interpoladorMovimiento);

                Alpha alphaRotacion = new Alpha(-1, 4000);
                RotationInterpolator rotacionAdicional = new RotationInterpolator(alphaRotacion, tgMovimiento);
                rotacionAdicional.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
                Transform3D transformAxis = new Transform3D();
                transformAxis.setRotation(new AxisAngle4f(0, 1, 0, (float) Math.PI));
                rotacionAdicional.setTransformAxis(transformAxis);
                objectoRaiz.addChild(rotacionAdicional);
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

        Color3f colorAmbiente = new Color3f(Color.DARK_GRAY);
        AmbientLight luzAmbiente = new AmbientLight(colorAmbiente);
        luzAmbiente.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100));
        objectoRaiz.addChild(luzAmbiente);

        Color3f colorLuz = new Color3f(Color.WHITE);
        Vector3f dirLuz = new Vector3f(-1.0f, -1.0f, -4.0f);
        DirectionalLight luz = new DirectionalLight(colorLuz, dirLuz);
        luz.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100));
        objectoRaiz.addChild(luz);

        return objectoRaiz;
    }

    private Node crearCubo(float tamaño, Appearance apariencia) {
        QuadArray cubo = new QuadArray(24, QuadArray.COORDINATES | QuadArray.NORMALS);
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

        Vector3f[] normals = new Vector3f[24];
        for (int i = 0; i < 24; i += 4) {
            Vector3f normal = new Vector3f(coords[i * 3], coords[i * 3 + 1], coords[i * 3 + 2]);
            normal.normalize();
            normals[i] = normal;
            normals[i + 1] = normal;
            normals[i + 2] = normal;
            normals[i + 3] = normal;
        }
        cubo.setNormals(0, normals);

        Shape3D shapeCubo = new Shape3D(cubo, apariencia);
        return shapeCubo;
    }

    public void limpiarEscena() {
        if (escena != null) {
            escena.detach();
        }
    }

    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        SwingUtilities.invokeLater(() -> {
            Practica12MendozaReyesAngelEmanuel frame = new Practica12MendozaReyesAngelEmanuel();
            frame.setVisible(true);
        });
    }
}