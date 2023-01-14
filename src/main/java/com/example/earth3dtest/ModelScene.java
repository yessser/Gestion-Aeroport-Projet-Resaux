package com.example.earth3dtest;


import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
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
import models.Plane;
import models.Position;
import models.Station;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class ModelScene  {

    Rotate stationx;
    Rotate stationy;
    HashMap<Station,Group> stationToModel=new HashMap<>();
    HashMap<Plane,Group> planeToModel=new HashMap<>();
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
//                AddStation(new Translate(pr.getX(),pr.getY(),pr.getZ()));
                setNewStationActive(false);
            }

            System.out.println(pr);
        });

        initMouseControl(root,earthScene);


        //root.getChildren().add(prepareGalaxy());
        earthScene.setRoot(root);
        earthScene.setCamera(camera);



    }

    public void startPlanes(){
        Plane p1 = new Plane(1000D,100D/6372.8,Math.toRadians(30D),20D,400D,new Position(90D,180D));
        Plane p2 = new Plane(1000D,100D/6372.8,Math.toRadians(30D),20D,400D,new Position(10D,10D));
        spawnPlane(p1);
        spawnPlane(p2);
//        p1.addDangerZonePlane(p2);
//        p2.addDangerZonePlane(p1);
//        to link flight and the plane
        Task<Position> t1 = p1.moveTo(new Position(10D,10D));
        t1.valueProperty().addListener((v,oldval,newval)->{
            planeToModel.get(p1).getTransforms().set(0,new Rotate(newval.positionlat.get(),Rotate.X_AXIS));
            planeToModel.get(p1).getTransforms().set(1,new Rotate(newval.positionLon.get(),Rotate.Y_AXIS));
//            todo: fix plane rotation
            planeToModel.get(p1).getTransforms().set(4,new Rotate(Math.toDegrees(p1.getCurrentRotation()),Rotate.Y_AXIS));
        });
        Thread th1 =new Thread(t1);
        th1.setDaemon(true);
        th1.start();
        Task<Position> t2 = p2.moveTo(new Position(0D,0D));
        t2.valueProperty().addListener((v,oldval,newval)->{
            planeToModel.get(p2).getTransforms().set(0,new Rotate(newval.positionlat.get(),Rotate.X_AXIS));
            planeToModel.get(p2).getTransforms().set(1,new Rotate(newval.positionLon.get(),Rotate.Y_AXIS));
//            todo: fix plane rotation
            planeToModel.get(p2).getTransforms().set(4,new Rotate(Math.toDegrees(p2.getCurrentRotation()),Rotate.Y_AXIS));
        });
        Thread th2 =new Thread(t2);
        th2.setDaemon(true);
        th2.start();
    }

    private void spawnPlane(Plane p){
        Group plane =  loadModel(getClass().getResource("/plane/airplane.obj"));
        planeToModel.put(p,plane);
        Rotate lat;
        Rotate lon;
        plane.getTransforms().addAll(lat=new Rotate(p.getPosition().positionlat.get(), Rotate.X_AXIS),
                                     lon=new Rotate(p.getPosition().positionLon.get(), Rotate.Y_AXIS));
        plane.getTransforms().add(new Translate(0,0,-1.001));
        plane.getTransforms().add(new Rotate(180,Rotate.X_AXIS));
        plane.getTransforms().add(new Rotate(0,Rotate.Y_AXIS));
        plane.getTransforms().add(new Rotate(90,Rotate.Z_AXIS));

        model.getChildren().add(plane);
    }
    private void  removePlane(Plane p){
        model.getChildren().remove(planeToModel.get(p));
        planeToModel.remove(p);
    }
    public void spawnStation(Station s) {
//        TODO:LOAD STATION MODEL HERE with all the parameters
        Group station = loadModel(getClass().getResource("/station/station.obj"));
        stationToModel.put(s,station);
//        TODO fix translation to lat and lon
//        Double lat = -1*Math.atan2(t.getY()*-1,Math.sqrt(Math.pow(t.getZ()*-1,2)+Math.pow(t.getX(),2)));
//        Double lon = -1*Math.atan2(t.getX(),t.getZ()*-1);
       // station.getTransforms().addAll(new Rotate(Math.toDegrees(s.getPosition().positionlat.get()), Rotate.X_AXIS),new Rotate(s.getPosition().positionLon.get(), Rotate.Y_AXIS));
        station.getTransforms().addAll(new Rotate(Math.toDegrees(s.getPosition().positionlat.get()), Rotate.X_AXIS),new Rotate(s.getPosition().positionLon.get(), Rotate.Y_AXIS));
        station.getTransforms().add(new Translate(0,0,-1.001));
        model.getChildren().add(station);
    }
    private void removeStation(Station s){
        model.getChildren().remove(stationToModel.get(s));
        stationToModel.remove(s);
    }
//    camera setup
    private static PerspectiveCamera PrepareCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);
        camera.setNearClip(0.0001);
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
