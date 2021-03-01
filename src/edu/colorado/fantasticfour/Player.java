package edu.colorado.fantasticfour;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private Board board;
    private Player opponent;
    private List<Ship> ships;

    public Board getTheirBoard() {
        return opponent.board;
    }
    public Board getMyBoard(){
        return this.board;
    }

    public List<Ship> getAllShips(){
        return ships;
    }

    public List<Ship> getAfloatShips(){
        return ships.stream().filter(ship -> !ship.isSunk() && ship.getCoordinates() != null).collect(Collectors.toList());
    }

    public boolean mustSurrender(){
        return getAfloatShips().size() == 0;
    }

    public List<Ship> getPlacedShips(){
        return ships.stream().filter(ship -> ship.getCoordinates() != null).collect(Collectors.toList());
    }

    public Ship getShipByName(String name) throws IllegalArgumentException{
        for(Ship ship : this.ships){
            if(ship.getName().equals(name)){
                return ship;
            }
        }
        throw new IllegalArgumentException("Ship not found");
    }

    public Ship getShipAt(Location location) throws IllegalArgumentException{
        for(Ship ship : getPlacedShips()){
           if(ship.getCoordinates().contains(new Cell(location.getX(), location.getY()))){
               return ship;
           }
        }
        throw new IllegalArgumentException("Ship not found");
    }

    public Player() {
        // player constructor creates its boards
        this.ships = List.of(
                new Ship("Minesweeper", 2),
                new Ship("Destroyer",3),
                new Ship("Battleship",4)
        );
        this.board = new Board(this);
    }

    public void setOpponent(Player opp){
        this.opponent = opp;
    }

    public String takeShot(int x, int y) throws IllegalArgumentException{
        return getTheirBoard().shootAt(x, y);
    }

    public void placeShip(String name, Location @NotNull ... locations) throws IllegalArgumentException{
        Ship ship = this.getShipByName(name);
        if(locations.length != ship.getLength()){
            throw new IllegalArgumentException("Number of cells must match ship length");
        }
        if(Board.inStraightLine(locations)){
            List<Cell> shipCells = this.getMyBoard().getCellsAtLocations(locations);
            ship.setCoordinates(shipCells);
            for(Cell cell : shipCells){
                cell.setShip(ship);
            }
        }else{
            throw new IllegalArgumentException("Cells are not on a straight line");
        }
    }

}
