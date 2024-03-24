package com.example.ai;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Package {
    private double destX, destY;
    private double weight;
    private boolean flag;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Package(double destX, double destY, double weight) {
        this.destX = destX;
        this.destY = destY;
        this.weight = weight;
        flag = false;
    }

    public double getDestX() {
        return destX;
    }

    public void setDestX(double destX) {
        this.destX = destX;
    }

    public double getDestY() {
        return destY;
    }

    public void setDestY(double destY) {
        this.destY = destY;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Package{" +
                " X = " + destX +
                " ; Y = " + destY +
                " ; Weight = " + weight +
                '}';
    }
}

class Vehicle {
    private ArrayList<Package> PackagesInCar = new ArrayList<>();
    private int VehicleNum;
    private double capacity;

    public double getCapacity() {
        return capacity;
    }

    public Vehicle(ArrayList<Package> packagesInCar, int vehicleNum, double capacity) {
        PackagesInCar = packagesInCar;
        VehicleNum = vehicleNum;
        this.capacity = capacity;
    }

    public ArrayList<Package> getPackagesInCar() {
        return PackagesInCar;
    }

    // add package to vehicle if there is space
    public void addPackage(Package p) {
        if (checkSpace(p)) {
            PackagesInCar.add(p);
        }
    }

    // function to check if there is space in the vehicle
    public boolean checkSpace(Package p) {
        int sum = 0;
        for (int i = 0; i < PackagesInCar.size(); i++) {
            sum += PackagesInCar.get(i).getWeight();
        }
        if (sum + p.getWeight() <= capacity) {
            return true;
        }
        return false;
    }

    public int getVehicleNum() {
        return VehicleNum;
    }

    public void setVehicleNum(int vehicleNum) {
        VehicleNum = vehicleNum;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "PackagesInCar=" + PackagesInCar +
                ", VehicleNum=" + VehicleNum +
                '}';
    }
}

public class HelloController {
    @FXML
    private AnchorPane background;

    @FXML
    private Label capacityLb;

    @FXML
    private TextField capacityTf;

    @FXML
    private Button cspBtn;

    @FXML
    private TextField packgesTf;

    @FXML
    private Label packgesLb;

    @FXML
    private Button simBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button clearBtn;

    @FXML
    private Label vehicleLb;

    @FXML
    private TextField vehiclesTf;

    @FXML
    private Label weightLb;

    @FXML
    private TextField weightTf;

    @FXML
    private Label welcomeLb;

    @FXML
    private Label xLb;

    @FXML
    private TextField xTf;

    @FXML
    private Label yLb;

    @FXML
    private TextField yTf;
    @FXML
    private Label outputLb;
    @FXML
    private Label outLb;
    private static final double INITIAL_TEMPERATURE = 1000;
    private static final double COOLING_RATE = 0.95;
    private static final int NUM_ITERATIONS = 1000;
int vehicleNum = 0;
int packageNum = 0;
double weight = 0;
double capacity = 0;
int Xstore= 0;
int Ystore = 0;
int destX = 0;
int destY = 0;


    List<Package> packages = new ArrayList<>();
    List<Vehicle> vehicles = new ArrayList<>();
    ArrayList<Vehicle> Vehicles2 = new ArrayList<>();
    ArrayList<Package> allPackages = new ArrayList<>();
    Package p = new Package(destX, destY, weight);


    @FXML
    void SimBtnClick(MouseEvent event) {
        List<Vehicle> bestSolution = simulatedAnnealing(packages, vehicles);

for (int i = 0; i < bestSolution.size(); i++) {
outputLb.setText( "Vehicle " + bestSolution.get(i).getVehicleNum() + " : " + bestSolution.get(i).getPackagesInCar());
System.out.println("Vehicle" +bestSolution.get(i).getVehicleNum() + " : " + bestSolution.get(i).getPackagesInCar());
}

    }
    @FXML
    void addClk(MouseEvent event) {
        vehicleNum = Integer.parseInt(vehiclesTf.getText());
        packageNum = Integer.parseInt(packgesTf.getText());
        capacity = Double.parseDouble(capacityTf.getText());

            vehicles.add(new Vehicle(new ArrayList<>(), vehicleNum, capacity));




        // Create Package objects with the entered values
        for (int i = 0; i < packageNum; i++) {
            weight = Double.parseDouble(weightTf.getText());
            destX = Integer.parseInt(xTf.getText());
            destY = Integer.parseInt(yTf.getText());
            packages.add(new Package(destX, destY, weight));
            allPackages.add(p);

        }
        //print arraylist
        for (int i = 0; i < packages.size(); i++) {
            System.out.println(packages.get(i));
        }


    }

    @FXML
    void clearClk(MouseEvent event) {
        vehiclesTf.clear();
        weightTf.clear();
        capacityTf.clear();
        xTf.clear();
        yTf.clear();
        packgesTf.clear();


    }
        @FXML
    void cspClick(MouseEvent event) {
            Vehicles2 = Solution(allPackages, vehicleNum, packageNum);
            for (int i = 0; i < Vehicles2.size(); i++) {
                outLb.setText("Vehicle " + Vehicles2.get(i).getVehicleNum() + " : " + Vehicles2.get(i).getPackagesInCar());
                System.out.println("Vehicle" + Vehicles2.get(i).getVehicleNum() + " : " + Vehicles2.get(i).getPackagesInCar());
            }


        }
    private static List<Vehicle> simulatedAnnealing(List<Package> packages, List<Vehicle> vehicles) {
        List<Vehicle> currentSolution = generateInitialSolution(packages, vehicles);
        List<Vehicle> bestSolution = new ArrayList<>(currentSolution);

        double temperature = INITIAL_TEMPERATURE;

        for (int i = 0; i < NUM_ITERATIONS; i++) {
            List<Vehicle> newSolution = generateNeighborSolution(currentSolution);

            double currentDistance = calculateTotalDistance(currentSolution);
            double newDistance = calculateTotalDistance(newSolution);

            if (acceptSolution(currentDistance, newDistance, temperature)) {
                currentSolution = newSolution;
            }

            if (newDistance < calculateTotalDistance(bestSolution)) {
                bestSolution = new ArrayList<>(newSolution);
            }

            temperature *= COOLING_RATE;
        }

        return bestSolution;
    }

    private static List<Vehicle> generateInitialSolution(List<Package> packages, List<Vehicle> vehicles) {
        List<Vehicle> initialSolution = new ArrayList<>(vehicles);

        Random random = new Random();
        for (Package p : packages) {
            int randomVehicleIndex = random.nextInt(initialSolution.size());
            Vehicle randomVehicle = initialSolution.get(randomVehicleIndex);
            if (randomVehicle.checkSpace(p)) {
                randomVehicle.addPackage(p);
            }
        }

        return initialSolution;
    }
    private static List<Vehicle> generateNeighborSolution(List<Vehicle> currentSolution) {
        List<Vehicle> neighborSolution = new ArrayList<>(currentSolution);

        Random random = new Random();
        int randomVehicleIndex = random.nextInt(neighborSolution.size());
        Vehicle randomVehicle = neighborSolution.get(randomVehicleIndex);

        if (randomVehicle.getPackagesInCar().size() > 1) {
            int randomPackageIndex1 = random.nextInt(randomVehicle.getPackagesInCar().size());
            int randomPackageIndex2 = random.nextInt(randomVehicle.getPackagesInCar().size());

            Package package1 = randomVehicle.getPackagesInCar().get(randomPackageIndex1);
            Package package2 = randomVehicle.getPackagesInCar().get(randomPackageIndex2);

            randomVehicle.getPackagesInCar().set(randomPackageIndex1, package2);
            randomVehicle.getPackagesInCar().set(randomPackageIndex2, package1);
        }

        return neighborSolution;
    }
    private static double calculateTotalDistance(List<Vehicle> solution) {
        double totalDistance = 0;

        for (Vehicle vehicle : solution) {
            List<Package> packagesInCar = vehicle.getPackagesInCar();
            if (packagesInCar.size() > 1) {
                for (int i = 0; i < packagesInCar.size() - 1; i++) {
                    Package package1 = packagesInCar.get(i);
                    Package package2 = packagesInCar.get(i + 1);
                    double distance = calculateDistance(package1.getDestX(), package1.getDestY(), package2.getDestX(), package2.getDestY());
                    totalDistance += distance;
                }
            }
        }

        return totalDistance;
    }
    public static double print_total_distance(List<Vehicle> solution) {
        double totalDistance = 0;
        totalDistance=calculateTotalDistance(solution);
        return totalDistance;
    }
    //calculate distance between two points by euclidean distance
    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static boolean acceptSolution(double currentDistance, double newDistance, double temperature) {
        if (newDistance < currentDistance) {
            return true;
        }
        //calculate acceptance probability
        double acceptanceProbability = Math.exp((currentDistance - newDistance) / temperature);
        return Math.random() < acceptanceProbability;
    }
    public  double Distance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double fitness = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return fitness;
    }
    public static boolean CheckifPossible(ArrayList<Package> arr, int numV){
        double r=0;
        for(int i=0 ; i<arr.size();i++){
            r+=arr.get(i).getWeight();

        }
        if(r>(numV*100)){
            return false;
        }
        else
            return true;
    }
    public static boolean IsPackageLoaded(Package p){
        if(p.getFlag()){
            return false;
        }
        else
            return true;
    }
    public static ArrayList<Vehicle> Solution(ArrayList<Package> packagee,int numV,int numP){
        ArrayList<Vehicle> V1 = new ArrayList<>();

        for(int i =0; i< numV ; i++){
            double capacity =100;
            ArrayList<Package> pk = new ArrayList<>();
            for(int j=0 ; j<numP ; j++){
                if(!IsPackageLoaded(packagee.get(j)) && capacity-packagee.get(j).getWeight()>=0 && CheckifPossible(packagee,numV)){
                    pk.add(packagee.get(j));
                    packagee.get(j).setFlag(true);
                    capacity -=packagee.get(j).getWeight();
                }
            }
            Vehicle v = new Vehicle(pk,i+1,capacity);
            V1.add(v);
        }
        return  V1;
    }
}