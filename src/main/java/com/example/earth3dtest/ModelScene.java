package com.example.earth3dtest;


import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ModelScene  {

    AnimationTimer timer = new AnimationTimer() {
        long last=0;
        final long second=1000000000;
        final long frameRate =30;
        public final double RotationSpeed = 0.3;

        @Override
        public void handle(long now) {
//                nano seconds
            if(now-last>second/frameRate) {
                model.setRotate(model.getRotate() + RotationSpeed);
                last=now;
            }
        }
    };
    private SubScene earthScene;
    private Group model=loadModel(getClass().getResource("/earth/earth.obj"));;

    private Group root=new Group(model,new AmbientLight(Color.WHITE));
    private boolean newStationActive;


    private Sphere prepareGalaxy() {
        Sphere galaxy = new Sphere(800);
        galaxy.setCullFace(CullFace.NONE);
        PhongMaterial material =  new PhongMaterial();
        material.setDiffuseMap(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/earth/Galaxy360.jpg"))));
        galaxy.setMaterial(material);
        return galaxy;
    }
    public ModelScene(SubScene subScene) {
        this.earthScene=subScene;

//        station.getTransforms().add(new Rotate(30,Rotate.X_AXIS));
//        station.getTransforms().add(new Rotate(30,Rotate.Y_AXIS));
        //camera setup
        PerspectiveCamera camera = PrepareCamera();

        //model setup
        
        model.setRotationAxis(Rotate.Y_AXIS);

        //onClick event setup
        model.setOnMouseClicked(e->{
            PickResult p = e.getPickResult();
            Point3D pr = e.getPickResult().getIntersectedPoint();
            System.out.println(newStationActive);

            if(newStationActive){
                AddStation(new Translate(pr.getX(),pr.getY(),pr.getZ()));
                setNewStationActive(false);
            }

            System.out.println(pr);
        });

        initMouseControl(root,earthScene);


        root.getChildren().add(prepareGalaxy());
        earthScene.setRoot(root);
        earthScene.setCamera(camera);
    }

    private void AddStation(Translate t) {
//        TODO:LOAD STATION MODEL HERE with all the parameters
        Box station = new Box(0.1,0.1,0.1);
        station.getTransforms().add(t);
        model.getChildren().add(station);
    }

//    camera setup
    private static PerspectiveCamera PrepareCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);
        camera.setNearClip(1);
        camera.setTranslateZ(-4);
        return camera;
    }

    public void startAnimation(){
        timer.start();
    }
    public  void stopAnimation(){
        timer.stop();
    }
//    mouse control variables TODO? put it in a different class maybe
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private void initMouseControl(Group group, SubScene scene) {
        Rotate xRotate;
        Rotate yRotate;
//        by doing this we bind the xy rotation of the group
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        //anchor is the saved button of where you clicked
        //TODO:handle which mouse click instead of any click
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

//       on the drag we compare the difference between where the mouse was and where it is now to spin
//        the 0.3 just to scale it down
        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY())*0.3);
            angleY.set(anchorAngleY + (anchorX - event.getSceneX())*0.3);
        });

//      handles scrolling to zoom
//        in this we are not moving the camera but the whole group instead
        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            double zoom = group.getTranslateZ() + delta*-0.01;
            if (zoom>=-2&&zoom<=4) group.translateZProperty().set(group.getTranslateZ() + delta*-0.01);
            System.out.println(delta);
            System.out.println(group.getTranslateZ());
        });

    }
    private Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }

    public void setNewStationActive(boolean state) {
        newStationActive=state;
    }
}
